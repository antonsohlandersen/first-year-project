package bfst22.vector;

import bfst22.vector.Collections.Path;
import bfst22.vector.Collections.Stack;
import bfst22.vector.Data.OSMData.OSMNode;
import bfst22.vector.Data.Point2D;
import bfst22.vector.Data.SpatialDataStructures.KDTree.KdTree;
import bfst22.vector.Data.SpatialDataStructures.RTree.RTree;
import bfst22.vector.Data.TSTree.TernarySearchTree;
import bfst22.vector.Drawables.Line;
import bfst22.vector.Drawables.MultiPolygon;
import bfst22.vector.Interfaces.Drawable;
import bfst22.vector.Parsing.OSMParser;
import bfst22.vector.RouteFinding.AStar;
import bfst22.vector.RouteFinding.DijkstraSP;
import bfst22.vector.RouteFinding.DirectedEdge;
import bfst22.vector.RouteFinding.EdgeWeightedDigraph;

import java.io.*;
import java.util.*;
import java.util.zip.ZipInputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

public class Model {
    String filename1 = "data/addressdata.txt";

    private float[] maxBoundingBox = new float[4];
    private float minlat, minlon, maxlat, maxlon;
    private ArrayList<Icon> icons = new ArrayList<>();
    private List<Runnable> observers = new ArrayList<>();
    private ArrayList<RTree> currentTrees = new ArrayList<>();
    private ArrayList<Icon> pointsOfInterest = new ArrayList<>();
    private Drawable finishedCoastlines;
    private RTree zoomLevel1Tree;
    private RTree zoomLevel2Tree;
    private RTree zoomLevel3Tree;
    private RTree zoomLevel4Tree;
    private EdgeWeightedDigraph highwayGraph;
    private ArrayList<Path<Drawable>> dijkstraPaths = new ArrayList<>();
    private ArrayList<Path<Drawable>> aStarPaths = new ArrayList<>();
    private ArrayList<Drawable> allVisitedEdges = new ArrayList<>();
    private TernarySearchTree ternarySearchTree = new TernarySearchTree();
    private KdTree pointTree;
    private Point2D routeStartPoint;
    private OSMNode routeStartNode;
    private OSMNode routeEndNode;
    private DijkstraSP dijkstra;
    private AStar aStar;
    private boolean isAStar;
    private boolean allVisitedEdgesMode;
    private String transportation = "car";

    @SuppressWarnings("unchecked")
    public Model(String filename)
            throws IOException, XMLStreamException, FactoryConfigurationError, ClassNotFoundException {
        var time = -System.nanoTime();
        if (filename.endsWith(".zip")) {
            var zip = new ZipInputStream(new FileInputStream(filename));
            zip.getNextEntry();
            new OSMParser(this, zip);
        } else if (filename.endsWith(".osm")) {
            new OSMParser(this, new FileInputStream(filename));
        } else if (filename.endsWith(".obj")) {
            ObjectInputStream stream = null;
            stream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
            try (var input = stream) {
                minlat = input.readFloat();
                minlon = input.readFloat();
                maxlat = input.readFloat();
                maxlon = input.readFloat();
                finishedCoastlines = (MultiPolygon) input.readObject();
                ternarySearchTree = (TernarySearchTree) input.readObject();
                zoomLevel2Tree = (RTree) input.readObject();
                zoomLevel3Tree = (RTree) input.readObject();
                zoomLevel4Tree = (RTree) input.readObject();
                zoomLevel1Tree = (RTree) input.readObject();
                highwayGraph = (EdgeWeightedDigraph) input.readObject();
                pointTree = (KdTree) input.readObject();
            }
        }
        time += System.nanoTime();
        System.out.println("Load time: " + (long) (time / 1e6) + " ms");
        if (!filename.endsWith(".obj")) {
            save(filename);
        }
    }

    public void save(String basename) throws FileNotFoundException, IOException {
        try (var out = new ObjectOutputStream(new FileOutputStream(basename + ".obj"))) {
            out.writeFloat(minlat);
            out.writeFloat(minlon);
            out.writeFloat(maxlat);
            out.writeFloat(maxlon);
            out.writeObject(finishedCoastlines);
            out.writeObject(ternarySearchTree);
            out.writeObject(zoomLevel2Tree);
            out.writeObject(zoomLevel3Tree);
            out.writeObject(zoomLevel4Tree);
            out.writeObject(zoomLevel1Tree);
            out.writeObject(highwayGraph);
            out.writeObject(pointTree);
        }
    }

    public void addObserver(Runnable observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (var observer : observers) {
            observer.run();
        }
    }

    public Drawable getCoastLines() {
        return finishedCoastlines;
    }

    public void setTrees(double zoomLevel) {
        currentTrees.clear();
        if (zoomLevel > 5000) {
            currentTrees.add(zoomLevel2Tree);
        }
        if (zoomLevel > 15000) {
            currentTrees.add(zoomLevel3Tree);
        }
        if (zoomLevel > 150000) {
            currentTrees.add(zoomLevel4Tree);
        }
        currentTrees.add(zoomLevel1Tree);
        notifyObservers();
    }

    public ArrayList<RTree> getCurrentTrees() {
        return currentTrees;
    }

    public void addPointOfInterest(Icon icon) {
        pointsOfInterest.add(icon);
    }

    public ArrayList<Icon> getPointsOfInterest() {
        return pointsOfInterest;
    }

    public void removePointOfInterest(int index) {
        if (!pointsOfInterest.isEmpty()){
            pointsOfInterest.remove(pointsOfInterest.get(index));
        }
    }
    
    public DijkstraSP doDijkstra(EdgeWeightedDigraph graph, long startId, long endId, String transportation, boolean addToVisitedEdges) {
        int source = highwayGraph.getIntersectNodes().get(startId);
        int target = highwayGraph.getIntersectNodes().get(endId);
        DijkstraSP dijkstra = new DijkstraSP(graph, source, target, transportation, addToVisitedEdges);
        return dijkstra;
    }

    public AStar doAstar(EdgeWeightedDigraph graph, long startId, long endId, OSMNode targetNode,
            String transportation) {
        int source = highwayGraph.getIntersectNodes().get(startId);
        int target = highwayGraph.getIntersectNodes().get(endId);
        AStar aStar = new AStar(graph, source, target, targetNode, transportation, allVisitedEdgesMode);
        return aStar;
    }

    public void dijkstraGetPaths(int destination) {
        dijkstraPaths.clear();
        for (Stack<DirectedEdge> stack : dijkstra.pathTo(destination)) {
            Path<Drawable> dijkstraPath = new Path<>();
            Iterator<DirectedEdge> iterator = stack.iterator();
            while (iterator.hasNext()) {
                DirectedEdge currentEdge = (DirectedEdge) iterator.next();
                for (int j = 0; j < currentEdge.getCoordinates().length - 3; j += 2) {
                    dijkstraPath.add(
                            new Line(new Point2D(currentEdge.getCoordinates()[j], currentEdge.getCoordinates()[j + 1]),
                                    new Point2D(currentEdge.getCoordinates()[j + 2],
                                            currentEdge.getCoordinates()[j + 3])));
                    if (dijkstraPath.getCar() && !iterator.hasNext() && !currentEdge.getCar())
                        dijkstraPath.setCar(false);
                }
            }
            dijkstraPaths.add(dijkstraPath);
        }
    }

    public void aStarGetPaths(int destination) {
        aStarPaths.clear();
        for (Stack<DirectedEdge> stack : aStar.pathTo(destination)) {
            Path<Drawable> aStarPath = new Path<>();
            Iterator<DirectedEdge> iterator = stack.iterator();
            while (iterator.hasNext()) {
                DirectedEdge currentEdge = (DirectedEdge) iterator.next();
                for (int j = 0; j < currentEdge.getCoordinates().length - 3; j += 2) {
                    aStarPath.add(
                            new Line(new Point2D(currentEdge.getCoordinates()[j], currentEdge.getCoordinates()[j + 1]),
                                    new Point2D(currentEdge.getCoordinates()[j + 2],
                                            currentEdge.getCoordinates()[j + 3])));
                    if (aStarPath.getCar() && !iterator.hasNext() && !currentEdge.getCar())
                        aStarPath.setCar(false);
                }

            }
            aStarPaths.add(aStarPath);
        }
    }

    public void allVisitedEdgesGetPaths() {
        allVisitedEdges.clear();
        if(isAStar) {
            for (DirectedEdge e : aStar.getAllVisitedEdges()) {
                for (int j = 0; j < e.getCoordinates().length - 3; j += 2) {
                    allVisitedEdges.add(
                            new Line(new Point2D(e.getCoordinates()[j], e.getCoordinates()[j + 1]),
                                    new Point2D(e.getCoordinates()[j + 2],
                                            e.getCoordinates()[j + 3])));
                }
            }
        }
        else {
            for (DirectedEdge e : dijkstra.getAllVisitedEdges()) {
                for (int j = 0; j < e.getCoordinates().length - 3; j += 2) {
                    allVisitedEdges.add(
                            new Line(new Point2D(e.getCoordinates()[j], e.getCoordinates()[j + 1]),
                                    new Point2D(e.getCoordinates()[j + 2],
                                            e.getCoordinates()[j + 3])));
                }
            }
        }
    }

    public ArrayList<Path<Drawable>> getDijkstraPaths() {
        return dijkstraPaths;
    }

    public ArrayList<Path<Drawable>> getAStarPaths() {
        return aStarPaths;
    }

    public ArrayList<Drawable> getAllVisitedEdges() {
        return allVisitedEdges;
    }

    public KdTree getPointTree() {
        return pointTree;
    }

    public void setRouteStartNode(Point2D point, OSMNode node) {
        routeStartPoint = point;
        routeStartNode = node;
        routeEndNode = null;
        icons.clear();
        dijkstraPaths.clear();
        aStarPaths.clear();
        allVisitedEdges.clear();
    }

    public Point2D getRouteStartPoint() {
        return routeStartPoint;
    }

    public OSMNode getRouteStartNode() {
        return routeStartNode;
    }

    public boolean hasRouteStartNode() {
        if (routeStartNode == null)
            return false;
        return true;
    }

    public void setAStarActivated(boolean isAStar) {
        this.isAStar = isAStar;
    }

    public boolean getAStarActivated() {
        return isAStar;
    }

    public void setRouteEndNode(OSMNode node) {
        routeEndNode = node;
        if(!allVisitedEdgesMode) {
            allVisitedEdges.clear();
        }
        if (!getAStarActivated()) {
            dijkstra = doDijkstra(highwayGraph, getRouteStartNode().getId(), node.getId(), transportation, allVisitedEdgesMode);
            dijkstraGetPaths(highwayGraph.getIntersectNodes().get(node.getId()));
        } else {
            aStar = doAstar(highwayGraph, getRouteStartNode().getId(), node.getId(), getRouteEndNode(), transportation);
            aStarGetPaths(highwayGraph.getIntersectNodes().get(node.getId()));
        }
        if(allVisitedEdgesMode) {
            allVisitedEdgesGetPaths();
        }
    }

    public OSMNode getRouteEndNode() {
        return routeEndNode;
    }

    public boolean hasRouteEndNode() {
        if (routeEndNode == null)
            return false;
        return true;
    }

    public void clearRoute() {
        dijkstraPaths.clear();
        aStarPaths.clear();
        allVisitedEdges.clear();
        icons.clear();
        routeStartPoint = null;
        routeStartNode = null;
        routeEndNode = null;
    }

    public void setTransportation(String transportation) {
        this.transportation = transportation;
    }

    public String getTransportation() {
        return transportation;
    }

    public float getMinlat() {
        return minlat;
    }

    public float getMaxlat() {
        return maxlat;
    }

    public float getMaxlon() {
        return maxlon;
    }

    public float getMinlon() {
        return minlon;
    }

    public EdgeWeightedDigraph getHighwayGraph() {
        return highwayGraph;
    }

    public RTree getZoomLevel1Tree() {
        return zoomLevel1Tree;
    }

    public RTree getZoomLevel2Tree() {
        return zoomLevel2Tree;
    }

    public RTree getZoomLevel3Tree() {
        return zoomLevel3Tree;
    }

    public RTree getZoomLevel4Tree() {
        return zoomLevel4Tree;
    }

    public void setMinlat(float minlat) {
        this.minlat = minlat;
        maxBoundingBox[1] = minlat;
    }

    public void setMaxlat(float maxlat) {
        this.maxlat = maxlat;
        maxBoundingBox[3] = maxlat;
    }

    public void setMaxlon(float maxlon) {
        this.maxlon = maxlon;
        maxBoundingBox[2] = maxlon;
    }

    public void setMinlon(float minlon) {
        this.minlon = minlon;
        maxBoundingBox[0] = minlon;
    }

    public ArrayList<Icon> getIcons() {
        return icons;
    }

    public TernarySearchTree getTernarySearchTree() {
        return ternarySearchTree;
    }

    public void setZoomLevel1Tree(RTree zoomLevel1Tree) {
        this.zoomLevel1Tree = zoomLevel1Tree;
    }

    public void setZoomLevel2Tree(RTree zoomLevel2Tree) {
        this.zoomLevel2Tree = zoomLevel2Tree;
    }

    public void setZoomLevel3Tree(RTree zoomLevel3Tree) {
        this.zoomLevel3Tree = zoomLevel3Tree;
    }

    public void setZoomLevel4Tree(RTree zoomLevel4Tree) {
        this.zoomLevel4Tree = zoomLevel4Tree;
    }

    public float[] getMaxBoundingBox() {
        return maxBoundingBox;
    }

    public void setHighwayGraph(EdgeWeightedDigraph graph) {
        highwayGraph = graph;
    }

    public void setPointTree(KdTree pointTree) {
        this.pointTree = pointTree;
    }

    public void setFinishedCoastlines(Drawable finishedCoastlines) {
        this.finishedCoastlines = finishedCoastlines;
    }

    public void setAllVisitedEdges(boolean debugMode) {
        allVisitedEdgesMode = debugMode;
    }
}
