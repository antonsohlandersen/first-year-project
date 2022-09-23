package bfst22.vector;

import bfst22.vector.RouteFinding.DijkstraSP;
import bfst22.vector.RouteFinding.DirectedEdge;
import bfst22.vector.RouteFinding.EdgeWeightedDigraph;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class DijkstraSPTest {
    EdgeWeightedDigraph EWD = new EdgeWeightedDigraph(0);
    float vLon = 55;
    float vLat = 55;
    float wLon = 56;
    float wLat = 56;
    double weight = 1;
    int maxSpeed = 50;
    double length = 1;
    boolean car = true;
    boolean bike = true;
    boolean walk = true;
    float[] coordinates = new float[] { 1, 2, 3, 4 };
    DijkstraSP dijkstra;

    @Test
    public void hasPathToTest() {
        TreeMap<Long, Integer> intersectNodes = new TreeMap<>();
        for (int i = 0; i < 5; i++) {
            EWD.addVertex((long) i, i, i);
            intersectNodes.put((long) i, intersectNodes.size());
        }
        EWD.convertMap(intersectNodes);
        for (int i = 0; i < EWD.getIntersectNodes().size() - 1; i++) {
            EWD.addEdge(
                    new DirectedEdge(i, i + 1, coordinates, weight, maxSpeed, length, car, bike,
                            walk));
        }
        dijkstra = new DijkstraSP(EWD, 0, 4, "car", false);
        assertTrue(dijkstra.hasPathTo(4));
        dijkstra = new DijkstraSP(EWD, 0, 4, "bike", false);
        assertTrue(dijkstra.hasPathTo(4));
        dijkstra = new DijkstraSP(EWD, 0, 4, "walk", false);
        assertTrue(dijkstra.hasPathTo(4));
    }

    @Test
    public void pathToTest() {
        TreeMap<Long, Integer> intersectNodes = new TreeMap<>();
        for (int i = 0; i < 5; i++) {
            EWD.addVertex((long) i, i, i);
            intersectNodes.put((long) i, intersectNodes.size());
        }
        EWD.convertMap(intersectNodes);

        for (int i = 0; i < EWD.getIntersectNodes().size() - 1; i++) {
            EWD.addEdge(new DirectedEdge(i, i + 1, coordinates, weight, maxSpeed, length, car,
                    bike, walk));
        }
        dijkstra = new DijkstraSP(EWD, 0, 4, "car", false);
        assertNotNull(dijkstra.pathTo(4));
    }
}
