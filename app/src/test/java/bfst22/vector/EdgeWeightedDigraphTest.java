package bfst22.vector;

import bfst22.vector.RouteFinding.DirectedEdge;
import bfst22.vector.RouteFinding.EdgeWeightedDigraph;
import org.junit.jupiter.api.Test;

import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

public class EdgeWeightedDigraphTest {

    int V = 5;
    float[] coordinates = new float[] { 1, 2, 3, 4 };
    DirectedEdge e = new DirectedEdge(0, 1, coordinates, 10, 50, 1, true, true, true);

    @Test
    public void createGraphTest() {
        EdgeWeightedDigraph EWD = new EdgeWeightedDigraph(V);
    }

    @Test
    public void createGraphWithNegativeVerticesTest() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            EdgeWeightedDigraph EWD = new EdgeWeightedDigraph(-1);
        });
        assertEquals(("Number of vertices in a Digraph must be non-negative"), exception.getMessage());
    }

    @Test
    public void addEdgeTest() {
        EdgeWeightedDigraph EWD = new EdgeWeightedDigraph(V);
        EWD.addEdge(e, 1000l);
        Iterable<DirectedEdge> list = EWD.edges();
        for (DirectedEdge de : list) {
            assertEquals(de, e);
        }
    }

    @Test
    public void getEdgesAndVerticesTest() {
        EdgeWeightedDigraph EWD = new EdgeWeightedDigraph(V);
        EWD.addEdge(e);
        assertEquals(EWD.V(), 5);
        assertEquals(EWD.E(), 1);
    }

    @Test
    public void validateVertexTestLessThan0() {
        EdgeWeightedDigraph EWD = new EdgeWeightedDigraph(V);
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            EWD.adj(-1);
        });
        assertEquals(("vertex " + -1 + " is not between 0 and " + (V - 1)), e.getMessage());

    }

    @Test
    public void validateVertexTestGreaterThanVertices() {
        EdgeWeightedDigraph EWD = new EdgeWeightedDigraph(V);
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            EWD.adj(V + 1);
        });
        assertEquals(("vertex " + (V + 1) + " is not between 0 and " + (V - 1)), e.getMessage());
    }

    @Test
    public void addVertexTest() {
        EdgeWeightedDigraph EWD = new EdgeWeightedDigraph(V);
        EWD.addVertex(1000l, 55, 55);
        assertEquals(EWD.V(), V + 1);
    }

    @Test
    public void getIntersectNodesTest() {
        TreeMap<Long, Integer> intersectNodes = new TreeMap<>();
        EdgeWeightedDigraph EWD = new EdgeWeightedDigraph(V);
        EWD.addVertex(1000l, 55, 55);
        intersectNodes.put(1000l, intersectNodes.size());
        EWD.convertMap(intersectNodes);
        assertEquals(1, EWD.getIntersectNodes().size());
    }

    @Test
    public void getWayIdsTest() {
        EdgeWeightedDigraph EWD = new EdgeWeightedDigraph(V);
        EWD.addEdge(e, 1000l);
        assertEquals(1000, EWD.getWayIds().get(0));
    }
}
