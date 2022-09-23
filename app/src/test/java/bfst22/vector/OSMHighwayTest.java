package bfst22.vector;

import bfst22.vector.Collections.NodeMap;
import bfst22.vector.Data.OSMData.OSMHighway;
import bfst22.vector.Data.OSMData.OSMNode;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class OSMHighwayTest {

    @Test
    public void testGetCar() {
        var nodes = new NodeMap();
        nodes.add(new OSMNode(32407507l, 14.7142694f, -55.0878006f));
        nodes.add(new OSMNode(32407369l, 14.7146613f, -55.0880375f));
        nodes.add(new OSMNode(32407485l, 14.7153474f, -55.0876525f));
        int maxSpeed = 80;
        boolean car1 = true;
        boolean bike = true;
        boolean walk = false;
        boolean oneway = false;
        long id = 1231231;
        OSMHighway osmHighway1 = new OSMHighway(nodes, id, maxSpeed, car1, bike, walk, oneway);
        assertEquals(true, osmHighway1.getCar());
        boolean car2 = false;
        OSMHighway osmHighway2 = new OSMHighway(nodes, id, maxSpeed, car2, bike, walk, oneway);
        assertEquals(false, osmHighway2.getCar());
    }

    @Test
    public void testGetBike() {
        var nodes = new NodeMap();
        nodes.add(new OSMNode(32407507l, 14.7142694f, -55.0878006f));
        nodes.add(new OSMNode(32407369l, 14.7146613f, -55.0880375f));
        nodes.add(new OSMNode(32407485l, 14.7153474f, -55.0876525f));
        int maxSpeed = 80;
        boolean car = true;
        boolean bike1 = true;
        boolean walk = false;
        boolean oneway = false;
        long id = 1231231;
        OSMHighway osmHighway1 = new OSMHighway(nodes, id, maxSpeed, car, bike1, walk, oneway);
        assertEquals(true, osmHighway1.getBike());
        boolean bike2 = false;
        OSMHighway osmHighway2 = new OSMHighway(nodes, id, maxSpeed, car, bike2, walk, oneway);
        assertEquals(false, osmHighway2.getBike());
    }

    @Test
    public void testGetWalk() {
        var nodes = new NodeMap();
        nodes.add(new OSMNode(32407507l, 14.7142694f, -55.0878006f));
        nodes.add(new OSMNode(32407369l, 14.7146613f, -55.0880375f));
        nodes.add(new OSMNode(32407485l, 14.7153474f, -55.0876525f));
        int maxSpeed = 80;
        boolean car = true;
        boolean bike = true;
        boolean walk1 = false;
        boolean oneway = false;
        long id = 1231231;
        OSMHighway osmHighway1 = new OSMHighway(nodes, id, maxSpeed, car, bike, walk1, oneway);
        assertEquals(false, osmHighway1.getWalk());
        boolean walk2 = true;
        OSMHighway osmHighway2 = new OSMHighway(nodes, id, maxSpeed, car, bike, walk2, oneway);
        assertEquals(true, osmHighway2.getWalk());
    }

    @Test
    public void testGetMaxSpeed() {
        var nodes = new NodeMap();
        nodes.add(new OSMNode(32407507l, 14.7142694f, -55.0878006f));
        nodes.add(new OSMNode(32407369l, 14.7146613f, -55.0880375f));
        nodes.add(new OSMNode(32407485l, 14.7153474f, -55.0876525f));
        int maxSpeed = 80;
        boolean car = true;
        boolean bike = true;
        boolean walk = false;
        boolean oneway = false;
        long id = 1231231;
        OSMHighway osmHighway = new OSMHighway(nodes, id, maxSpeed, car, bike, walk, oneway);
        assertEquals(80, osmHighway.getMaxSpeed());
    }

    @Test
    public void testGetOneWay() {
        var nodes = new NodeMap();
        nodes.add(new OSMNode(32407507l, 14.7142694f, -55.0878006f));
        nodes.add(new OSMNode(32407369l, 14.7146613f, -55.0880375f));
        nodes.add(new OSMNode(32407485l, 14.7153474f, -55.0876525f));
        int maxSpeed = 80;
        boolean car = true;
        boolean bike = true;
        boolean walk = false;
        boolean oneway1 = false;
        long id = 1231231;
        OSMHighway osmHighway1 = new OSMHighway(nodes, id, maxSpeed, car, bike, walk, oneway1);
        assertEquals(false, osmHighway1.getOneWay());
        boolean oneway2 = true;
        OSMHighway osmHighway2 = new OSMHighway(nodes, id, maxSpeed, car, bike, walk, oneway2);
        assertEquals(true, osmHighway2.getOneWay());
    }
}