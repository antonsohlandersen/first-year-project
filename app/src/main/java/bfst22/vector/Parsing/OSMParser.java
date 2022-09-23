package bfst22.vector.Parsing;

import bfst22.vector.Collections.NodeMap;
import bfst22.vector.Collections.PartitionList;
import bfst22.vector.Data.OSMData.OSMHighway;
import bfst22.vector.Data.OSMData.OSMNode;
import bfst22.vector.Data.OSMData.OSMWay;
import bfst22.vector.Data.SpatialDataStructures.KDTree.KdTree;
import bfst22.vector.Data.SpatialDataStructures.RTree.RTree;
import bfst22.vector.Data.Street;
import bfst22.vector.Drawables.MultiPolygon;
import bfst22.vector.Drawables.PolyLine;
import bfst22.vector.Enum.WayType;
import bfst22.vector.Interfaces.Drawable;
import bfst22.vector.RouteFinding.LongInt;
import bfst22.vector.Model;
import bfst22.vector.RouteFinding.DirectedEdge;
import bfst22.vector.RouteFinding.EdgeWeightedDigraph;
import bfst22.vector.Utilities.MultiPolygonBuilder;
import javafx.scene.effect.Light.Spot;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.*;

public class OSMParser {
    private Set<WayType> zoomLevel1Types = new HashSet<>(
            Arrays.asList(WayType.PRIMARY, WayType.MOTORWAY, WayType.TRUNK));
    private Set<WayType> zoomLevel2Types = new HashSet<>(
            Arrays.asList(WayType.GRASS, WayType.GRASSLAND, WayType.PARK, WayType.RESIDENTIAL, WayType.INDUSTRIAL));
    private Set<WayType> zoomLevel3Types = new HashSet<>(
            Arrays.asList(WayType.FOREST, WayType.SECONDARY, WayType.BEACH, WayType.TERTIARY,
                    WayType.MEADOW, WayType.RIVER, WayType.WATER,
                    WayType.WETLAND, WayType.WOOD, WayType.SCRUB, WayType.FARMLAND,
                    WayType.CEMETERY, WayType.RECREATION_GROUND, WayType.GOLF, WayType.PITCH));
    private Set<WayType> zoomLevel4Types = new HashSet<>(
            Arrays.asList(WayType.BUILDING, WayType.UNCLASSIFIED_HIGHWAY, WayType.RESIDENTIAL_HIGHWAY, WayType.SERVICE,
                    WayType.BRIDLEWAY, WayType.BUS_GUIDEWAY, WayType.FOOTWAY, WayType.CYCLEWAY,
                    WayType.LIVING_STREET, WayType.TRACK, WayType.PATH));

    private Model model;

    public OSMParser(Model model, InputStream inputStream) throws XMLStreamException {
        this.model = model;
        loadOSM(inputStream);
    }

    private void loadOSM(InputStream input) throws XMLStreamException, FactoryConfigurationError {
        Queue<OSMWay> coastlines = new ArrayDeque<>();
        List<Drawable> lines = new ArrayList<>();
        ArrayList<OSMHighway> highways = new ArrayList<>();
        var reader = XMLInputFactory.newInstance().createXMLStreamReader(new BufferedInputStream(input));
        var id2node = new NodeMap();
        var id2way = new HashMap<Long, OSMWay>();
        var nodes = new ArrayList<OSMNode>();
        var rel = new ArrayDeque<OSMWay>();
        long relID = 0;
        String street = "";
        String housenumber = "";
        String postcode = "";
        String city = "";
        float longtitude = 0;
        float latitude = 0;

        boolean road = false;
        int maxSpeed = 0;
        boolean car = false, bike = false, walk = true;
        boolean oneway = false;

        var type = WayType.UNKNOWN;
        while (reader.hasNext()) {
            switch (reader.next()) {
                case XMLStreamConstants.START_ELEMENT:
                    var name = reader.getLocalName();
                    switch (name) {
                        case "bounds":
                            model.setMaxlat(-Float.parseFloat(reader.getAttributeValue(null, "minlat")));
                            model.setMinlon(0.56f * Float.parseFloat(reader.getAttributeValue(null, "minlon")));
                            model.setMinlat(-Float.parseFloat(reader.getAttributeValue(null, "maxlat")));
                            model.setMaxlon(0.56f * Float.parseFloat(reader.getAttributeValue(null, "maxlon")));
                            model.setZoomLevel1Tree(new RTree(model.getMaxBoundingBox()));
                            model.setZoomLevel2Tree(new RTree(model.getMaxBoundingBox()));
                            model.setZoomLevel3Tree(new RTree(model.getMaxBoundingBox()));
                            model.setZoomLevel4Tree(new RTree(model.getMaxBoundingBox()));
                            break;
                        case "node":
                            var id = Long.parseLong(reader.getAttributeValue(null, "id"));
                            var lat = Float.parseFloat(reader.getAttributeValue(null, "lat"));
                            var lon = Float.parseFloat(reader.getAttributeValue(null, "lon"));
                            id2node.add(new OSMNode(id, 0.56f * lon, -lat));
                            longtitude = 0.56f * lon;
                            latitude = -lat;
                            break;
                        case "nd":
                            var ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
                            id2node.get(ref).addParentWay();
                            nodes.add(id2node.get(ref));
                            break;
                        case "way":
                            relID = Long.parseLong(reader.getAttributeValue(null, "id"));
                            road = false;
                            type = WayType.UNKNOWN;
                            car = false;
                            bike = true;
                            walk = true;
                            oneway = false;
                            break;
                        case "tag":
                            var k = reader.getAttributeValue(null, "k");
                            var v = reader.getAttributeValue(null, "v");
                            switch (k) {
                                case "natural": {
                                    switch (v) {
                                        case "coastline": {
                                            type = WayType.COASTLINE;
                                            break;
                                        }
                                        case "water": {
                                            type = WayType.WATER;
                                            break;
                                        }
                                    }
                                    break;
                                }
                                case "landuse": {
                                    switch (v) {
                                        case "grass": {
                                            type = WayType.GRASS;
                                            break;
                                        }
                                        case "forest": {
                                            type = WayType.FOREST;
                                            break;
                                        }
                                        case "meadow": {
                                            type = WayType.MEADOW;
                                            break;
                                        }
                                        case "residential": {
                                            type = WayType.RESIDENTIAL;
                                            break;
                                        }
                                        case "industrial": {
                                            type = WayType.INDUSTRIAL;
                                            break;
                                        }
                                        case "cemetery": {
                                            type = WayType.CEMETERY;
                                            break;
                                        }
                                        case "recreation_ground": {
                                            type = WayType.RECREATION_GROUND;
                                            break;
                                        }
                                    }
                                    break;
                                }

                                case "leisure": {
                                    switch (v) {
                                        case "park": {
                                            type = WayType.PARK;
                                            break;
                                        }
                                        case "golf_course": {
                                            type = WayType.GOLF;
                                            break;
                                        }
                                        case "pitch": {
                                            type = WayType.PITCH;
                                            break;
                                        }
                                    }
                                    break;
                                }

                                case "waterway": {
                                    switch (v) {
                                        case "river": {
                                            type = WayType.RIVER;
                                            break;
                                        }
                                    }
                                    break;
                                }

                                case "building": {
                                    type = WayType.BUILDING;
                                    break;
                                }

                                case "name": {
                                    for (OSMNode node : nodes) {
                                        node.setStreetName(v);
                                    }
                                }

                                case "addr:street": {
                                    street = v;
                                    break;
                                }
                                case "addr:postcode": {
                                    postcode = v;
                                    break;
                                }
                                case "addr:city": {
                                    city = v;
                                    break;
                                }
                                case "addr:housenumber": {
                                    housenumber = v;
                                    break;
                                }
                                case "maxSpeed": {
                                    maxSpeed = Integer.parseInt(v);
                                    break;
                                }
                                case "motor_vehicle": {
                                    switch (v) {
                                        case "yes":
                                            car = true;
                                            break;
                                        case "no":
                                            car = false;
                                            break;
                                    }
                                    break;
                                }
                                case "bicycle": {
                                    switch (v) {
                                        case "yes":
                                            bike = true;
                                            break;
                                        case "no":
                                            bike = false;
                                            break;
                                    }
                                    break;
                                }
                                case "foot": {
                                    switch (v) {
                                        case "yes":
                                            walk = true;
                                            break;
                                        case "no":
                                            walk = false;
                                            break;
                                    }
                                    break;
                                }
                                case "oneway": {
                                    if (v.equals("yes")) {
                                        oneway = true;
                                    }
                                    break;
                                }
                                case "junction": {
                                    if (v.equals("roundabout")) {
                                        oneway = true;
                                    }
                                    break;
                                }
                                case "highway": {
                                    maxSpeed = 0;
                                    road = true;
                                    type = WayType.HIGHWAY;
                                    switch (v) {
                                        case "motorway": {
                                            type = WayType.MOTORWAY;
                                            car = true;
                                            walk = false;
                                            bike = false;
                                            if (maxSpeed == 0)
                                                maxSpeed = 130;
                                            break;
                                        }
                                        case "motorway_link": {
                                            type = WayType.MOTORWAY;
                                            car = true;
                                            walk = false;
                                            bike = false;
                                            if (maxSpeed == 0)
                                                maxSpeed = 130;
                                            break;
                                        }
                                        case "trunk": {
                                            type = WayType.TRUNK;
                                            car = true;
                                            if (maxSpeed == 0)
                                                maxSpeed = 80;
                                            break;
                                        }
                                        case "trunk_link": {
                                            type = WayType.TRUNK;
                                            car = true;
                                            if (maxSpeed == 0)
                                                maxSpeed = 80;
                                            break;
                                        }
                                        case "primary": {
                                            type = WayType.PRIMARY;
                                            car = true;
                                            if (maxSpeed == 0)
                                                maxSpeed = 80;
                                            break;
                                        }
                                        case "primary_link": {
                                            type = WayType.PRIMARY;
                                            car = true;
                                            bike = true;
                                            if (maxSpeed == 0)
                                                maxSpeed = 80;
                                            break;
                                        }
                                        case "secondary": {
                                            type = WayType.SECONDARY;
                                            car = true;
                                            if (maxSpeed == 0)
                                                maxSpeed = 80;
                                            break;
                                        }
                                        case "secondary_link": {
                                            type = WayType.SECONDARY;
                                            car = true;
                                            bike = true;
                                            if (maxSpeed == 0)
                                                maxSpeed = 80;
                                            break;
                                        }
                                        case "residential": {
                                            type = WayType.RESIDENTIAL_HIGHWAY;
                                            car = true;
                                            bike = true;
                                            walk = true;
                                            if (maxSpeed == 0)
                                                maxSpeed = 50;
                                            break;
                                        }
                                        case "tertiary": {
                                            type = WayType.TERTIARY;
                                            car = true;
                                            bike = true;
                                            walk = true;
                                            if (maxSpeed == 0)
                                                maxSpeed = 50;
                                            break;
                                        }
                                        case "tertiary_link": {
                                            type = WayType.TERTIARY;
                                            car = true;
                                            if (maxSpeed == 0)
                                                maxSpeed = 50;
                                            break;
                                        }
                                        case "unclassified": {
                                            type = WayType.UNCLASSIFIED_HIGHWAY;
                                            car = true;
                                            if (maxSpeed == 0)
                                                maxSpeed = 50;
                                            break;
                                        }
                                        case "living_street": {
                                            type = WayType.LIVING_STREET;
                                            car = true;
                                            bike = true;
                                            walk = true;
                                            if (maxSpeed == 0)
                                                maxSpeed = 50;
                                            break;
                                        }
                                        case "service": {
                                            type = WayType.SERVICE;
                                            car = true;
                                            if (maxSpeed == 0)
                                                maxSpeed = 20;
                                            break;
                                        }
                                        case "pedestrian": {
                                            type = WayType.PEDESTRIAN;
                                            walk = true;
                                            if (car)
                                                maxSpeed = 10;
                                            break;
                                        }
                                        case "track": {
                                            type = WayType.TRACK;
                                            car = true;
                                            bike = true;
                                            walk = true;
                                            if (maxSpeed == 0)
                                                maxSpeed = 40;
                                            break;
                                        }
                                        case "bus_guideway": {
                                            type = WayType.BUS_GUIDEWAY;
                                            break;
                                        }
                                        case "footway": {
                                            type = WayType.FOOTWAY;
                                            walk = true;
                                            break;
                                        }
                                        case "path": {
                                            type = WayType.PATH;
                                            walk = true;
                                            break;
                                        }
                                        case "bridleway": {
                                            type = WayType.BRIDLEWAY;
                                            walk = true;
                                            break;
                                        }
                                        case "steps": {
                                            type = WayType.STEPS;
                                            walk = true;
                                            break;
                                        }
                                        case "cycleway": {
                                            type = WayType.CYCLEWAY;
                                            bike = true;
                                            break;
                                        }
                                    }

                                    if (nodes.size() == 0)
                                        break;

                                    break;
                                }
                            }
                            break;
                        case "member":
                            ref = Long.parseLong(reader.getAttributeValue(null, "ref"));
                            var elm = id2way.get(ref);
                            if (elm != null) {
                                rel.add(elm);
                            }
                            break;
                        case "relation":
                            type = WayType.UNKNOWN;

                            break;
                    }
                case XMLStreamConstants.END_ELEMENT:
                    switch (reader.getLocalName()) {
                        case "node":
                            if ((street + housenumber + postcode +
                                    city).length() > 0) {
                                if (!street.isBlank() && !postcode.isBlank()) {
                                    Street newStreet = new Street(street, postcode, city);
                                    newStreet.addAddress(housenumber, new float[]{longtitude, latitude});
                                    model.getTernarySearchTree().addStreet(newStreet);
                                }
                            }
                            street = "";
                            housenumber = "";
                            postcode = "";
                            city = "";
                            break;
                        case "way":
                            if (!nodes.isEmpty()) {
                                if (type == WayType.COASTLINE) {
                                    OSMWay coastline = new OSMWay(nodes, relID);
                                    coastlines.add(coastline);
                                } else if (type == WayType.UNKNOWN) {
                                    id2way.put(relID, new OSMWay(nodes, relID));
                                } else if (road) {
                                    id2way.put(relID, new OSMHighway(nodes, relID, maxSpeed, car, bike, walk, oneway));
                                    highways.add((OSMHighway) id2way.get(relID));
                                    lines.add(new PolyLine(nodes, type));
                                } else if (!road) {
                                    id2way.put(relID, new OSMWay(nodes));
                                    lines.add(new PolyLine(nodes, type));
                                }
                            }
                            nodes.clear();
                            break;
                        case "relation":
                            if (!rel.isEmpty()) {
                                MultiPolygonBuilder.makeNewPolygons(rel);

                                lines.add(new MultiPolygon(MultiPolygonBuilder.getFinishedPolygons(), type));
                            }
                            rel.clear();
                            break;
                    }
                    break;
            }
        }
        constructCoastlines(coastlines);
        constructRTrees(lines);
        model.getTernarySearchTree().compressTree();
        createGraph(highways);
        constructKDTree(id2node);
    }

    public void constructKDTree(NodeMap id2node) {
        PartitionList<OSMNode> nodeList = new PartitionList<>();
        for (LongInt entry : model.getHighwayGraph().getIntersectNodes()) {
            nodeList.add(id2node.get(entry.getKey()));
        }
        model.setPointTree(new KdTree((int) Math.sqrt(model.getHighwayGraph().getIntersectNodes().size()), nodeList, true));
    }

    public void constructCoastlines(Queue<OSMWay> coastlines) {
        MultiPolygonBuilder.makeNewPolygons(coastlines);
        model.setFinishedCoastlines(new MultiPolygon(MultiPolygonBuilder.getFinishedPolygons(), WayType.COASTLINE));
        MultiPolygonBuilder.clearPolygons();
    }

    public void constructRTrees(List<Drawable> lines) {
        PartitionList<Drawable> zoomLevel1List = new PartitionList<>();
        PartitionList<Drawable> zoomLevel2List = new PartitionList<>();
        PartitionList<Drawable> zoomLevel3List = new PartitionList<>();
        PartitionList<Drawable> zoomLevel4List = new PartitionList<>();
        for (Drawable line : lines) {
            if (zoomLevel1Types.contains(line.getType())) {
                zoomLevel1List.add(line);
            } else if (zoomLevel2Types.contains(line.getType())) {
                zoomLevel2List.add(line);
            } else if (zoomLevel3Types.contains(line.getType())) {
                zoomLevel3List.add(line);
            } else if (zoomLevel4Types.contains(line.getType())) {
                zoomLevel4List.add(line);
            }
        }
        model.getZoomLevel1Tree().constructLeafs(zoomLevel1List, 5);
        model.getZoomLevel2Tree().constructLeafs(zoomLevel2List, 10);
        model.getZoomLevel3Tree().constructLeafs(zoomLevel3List, 10);
        model.getZoomLevel4Tree().constructLeafs(zoomLevel4List, 100);
    }

    public void createGraph(ArrayList<OSMHighway> highways) {
        EdgeWeightedDigraph graph = new EdgeWeightedDigraph(0);
        TreeMap<Long, Integer> intersectNodes = new TreeMap<>();
        for (OSMHighway way : highways) {
            long firstNode = way.getFirstNodeId();
            long lastNode = way.getLastNodeId();
            long previousNode = 0;
            float previousLat = 0;
            float previousLon = 0;
            int nodeIndex = 0;
            int previousNodeAddedIndex = 0;
            OSMNode previous = null;
            double length = 0;
            for (OSMNode node : way.getNodes()) {
                double weight = 0;
                if(previous != null){
                    double latDistanceKm = Math.abs((previous.getLat()) - (node.getLat())) * 110.754;
                    double lonDistanceKm = Math.abs(previous.getLon() - node.getLon()) * 111.320;
                    length += Math.sqrt(Math.pow((latDistanceKm), 2) + Math.pow((lonDistanceKm), 2));
                }
                
                if (node.hasMultipleParents() || node.getId() == firstNode || node.getId() == lastNode) {
                    if (!intersectNodes.containsKey(node.getId())) {
                        graph.addVertex(node.getId(), node.getLon(), node.getLat());
                        intersectNodes.put(node.getId(), intersectNodes.size());
                    }
                    if (previousNode != 0) {
                        int maxSpeed = way.getMaxSpeed();
                        if (maxSpeed == 0) {
                            maxSpeed = 20;
                        }
                        float[] wayCoordinates = new float[(nodeIndex - previousNodeAddedIndex + 1) * 2];
                        
                        int k = previousNodeAddedIndex;
                        for (int i = 0; i <= wayCoordinates.length - 2; i += 2) {
                            wayCoordinates[i] = way.getNodes().get(k).getLon();
                            wayCoordinates[i + 1] = way.getNodes().get(k).getLat();
                            k++;
                        }
                        float[] wayCoordinatesReversed = reverse(wayCoordinates,wayCoordinates.length);
                        wayCoordinatesReversed = swap(wayCoordinatesReversed, wayCoordinatesReversed.length);
                        graph.addEdge(
                                new DirectedEdge(intersectNodes.get(previousNode),
                                        intersectNodes.get(node.getId()), wayCoordinates, weight,
                                        way.getMaxSpeed(), length,
                                        way.getCar(), way.getBike(), way.getWalk()),
                                way.getId());
                        boolean car = way.getCar();
                        if (way.getOneWay()) {
                            car = false;
                        }
                        graph.addEdge(new DirectedEdge(intersectNodes.get(node.getId()),
                                intersectNodes.get(previousNode), wayCoordinatesReversed,
                                weight,
                                way.getMaxSpeed(), length,
                                car, way.getBike(), way.getWalk()));
                        length = 0;
                    }
                    previousNode = node.getId();
                    previousLat = node.getLat();
                    previousLon = node.getLon();
                    previousNodeAddedIndex = nodeIndex;
                }
                nodeIndex++;
                previous = node;
            }
        }
        graph.convertMap(intersectNodes);
        model.setHighwayGraph(graph);
    }
    static float[] reverse(float a[], int n)
    {
        float[] b = new float[n];
        int j = n;
        for (int i = 0; i < n; i++) {
            b[j - 1] = a[i];
            j = j - 1;
        }
        return b;
    }
    static float[] swap(float a[], int n){
        for(int i = 0; i<n;i+=2){
            float temp = a[i];
            a[i] = a[i+1];
            a[i+1] = temp;
        }
        return a;
    }
}
