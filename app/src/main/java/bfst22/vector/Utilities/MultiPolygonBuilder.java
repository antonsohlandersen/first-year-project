package bfst22.vector.Utilities;

import bfst22.vector.Data.OSMData.OSMWay;
import bfst22.vector.Collections.SpecialList;

import java.util.ArrayList;
import java.util.Queue;

public class MultiPolygonBuilder {
    private static ArrayList<OSMWay> finishedPolygons = new ArrayList<>();

    public static void makeNewPolygons(Queue<OSMWay> polygons){
        if (finishedPolygons.isEmpty()){
            makePolygons(polygons);
        }
        else{
            clearPolygons();
            makePolygons(polygons);
        }
    }

    //constructs polygons recursively
    private static void makePolygons(Queue<OSMWay> polygons) {
        ArrayList<SpecialList> connectedPolygons = new ArrayList<>();
        int startSize = polygons.size();
        OSMWay firstWay = polygons.remove();
        connectedPolygons.add(new SpecialList(firstWay));
        for (OSMWay way : polygons) {
            int iterations =  connectedPolygons.size();
            boolean match = false;
            for (int i = 0; i < iterations; i++) {
                if (connectedPolygons.get(i).matchAndAddInOrder(way)) {
                    match = true;
                    break;
                }
            }
            if (!match) {
                connectedPolygons.add(new SpecialList(way));
            }
        }
        polygons.clear();
        for (SpecialList nodes : connectedPolygons) {
            if (nodes.isCycle()) {
                finishedPolygons.add(new OSMWay(nodes));
            }
            else {
                polygons.add(new OSMWay(nodes));
            }
        }
        if (polygons.size() == startSize) {
            finishedPolygons.addAll(polygons);
            return;
        }
        else if (polygons.isEmpty()) {
            return;
        }
        else {
            makePolygons(polygons);
        }
    }

    public static ArrayList<OSMWay> getFinishedPolygons() {
        return finishedPolygons;
    }

    public static void clearPolygons(){
        finishedPolygons.clear();
    }
}
