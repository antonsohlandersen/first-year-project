package bfst22.vector;

import bfst22.vector.RouteFinding.DirectedEdge;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectedEdgeTest {
    int v = 0;
    float vLon = 55;
    float vLat = 55;
    int w = 1;
    float wLon = 56;
    float wLat = 56;
    double weight = 10.0;
    float[] coordinates = new float[] { 55, 55, 56, 56 };
    int maxSpeed = 50;
    double length = 1;
    boolean car = true;
    boolean bike = true;
    boolean walk = true;

    @Test
    public void createEdgeTest() {
        DirectedEdge DE = new DirectedEdge(v, w, coordinates, weight, maxSpeed, length, car,
                bike, walk);
    }

    @Test
    public void testFromLessThan0() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DirectedEdge DE = new DirectedEdge(-1, w, coordinates, weight, maxSpeed, length,
                    car, bike,
                    walk);
        });
        assertEquals(("Vertex names must be non-negative integers"), exception.getMessage());
    }

    @Test
    public void testToLessThan0() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DirectedEdge DE = new DirectedEdge(v, -1, coordinates, weight, maxSpeed, length,
                    car, bike,
                    walk);
        });
        assertEquals(("Vertex names must be non-negative integers"), exception.getMessage());
    }

    @Test
    public void testWeightIsNaN() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DirectedEdge DE = new DirectedEdge(v, w, coordinates, Double.NaN, maxSpeed, length,
                    car, bike,
                    walk);
        });
        assertEquals(("Weight is NaN"), exception.getMessage());
    }

    @Test
    public void testGetWeight() {
        DirectedEdge DE = new DirectedEdge(v, w, coordinates, weight, maxSpeed, length, car,
                bike, walk);
        assertEquals(DE.weight(), weight);
        assertEquals(DE.getCarWeight(), 82);
        assertEquals(DE.getBikeWeight(), 190);
        assertEquals(DE.getWalkWeight(), 730);
    }

    @Test
    public void testGetPoints() {
        DirectedEdge DE = new DirectedEdge(v, w, coordinates, weight, maxSpeed, length, car,
                bike, walk);
        assertEquals(DE.getPoint2DFrom().getX(), 55);
        assertEquals(DE.getPoint2DFrom().getY(), 55);
        assertEquals(DE.getPoint2DTo().getX(), 56);
        assertEquals(DE.getPoint2DTo().getY(), 56);
    }

    @Test
    public void testGetBooleans() {
        DirectedEdge DE = new DirectedEdge(v, w, coordinates, weight, maxSpeed, length, car,
                bike, walk);
        assertEquals(DE.getCar(), true);
        assertEquals(DE.getBike(), true);
        assertEquals(DE.getWalk(), true);
    }

    public void getToAndFrom() {
        DirectedEdge DE = new DirectedEdge(v, w, coordinates, weight, maxSpeed, length, car,
                bike, walk);
        assertEquals(DE.from(), 0);
        assertEquals(DE.to(), 1);
    }
}
