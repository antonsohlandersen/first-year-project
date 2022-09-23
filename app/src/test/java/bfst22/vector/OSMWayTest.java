package bfst22.vector;

import bfst22.vector.Collections.NodeMap;
import bfst22.vector.Data.OSMData.OSMNode;
import bfst22.vector.Data.OSMData.OSMWay;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OSMWayTest {

    @Test
    public void testSetMember() {
        NodeMap nodes1 = new NodeMap();
        nodes1.add(new OSMNode(32407507l, 14.7142694f, -55.0878006f));
        nodes1.add(new OSMNode(32407369l, 14.7146613f, -55.0880375f));
        nodes1.add(new OSMNode(32407485l, 14.7153474f, -55.0876525f));
        nodes1.add(new OSMNode(32407356l, 14.7157779f, -55.0874130f));
        OSMWay osmWay1 = new OSMWay(nodes1);
        osmWay1.setMember();
        assertEquals(true, osmWay1.isMember());
        NodeMap nodes2 = new NodeMap();
        nodes2.add(new OSMNode(32407507l, 14.7142694f, -55.0878006f));
        nodes2.add(new OSMNode(32407369l, 14.7146613f, -55.0880375f));
        nodes2.add(new OSMNode(32407485l, 14.7153474f, -55.0876525f));
        nodes2.add(new OSMNode(32407356l, 14.7157779f, -55.0874130f));
        OSMWay osmWay2 = new OSMWay(nodes2);
        assertEquals(false, osmWay2.isMember());
    }

    @Test
    public void testIsMember() {
        NodeMap nodes = new NodeMap();
        nodes.add(new OSMNode(32407507l, 14.7142694f, -55.0878006f));
        nodes.add(new OSMNode(32407369l, 14.7146613f, -55.0880375f));
        nodes.add(new OSMNode(32407485l, 14.7153474f, -55.0876525f));
        nodes.add(new OSMNode(32407356l, 14.7157779f, -55.0874130f));
        OSMWay osmWay = new OSMWay(nodes);
        assertEquals(false, osmWay.isMember());
    }

    @Test
    public void testGetFirstNodeId() {
        NodeMap nodes = new NodeMap();
        nodes.add(new OSMNode(32407507l, 14.7142694f, -55.0878006f));
        nodes.add(new OSMNode(32407369l, 14.7146613f, -55.0880375f));
        nodes.add(new OSMNode(32407485l, 14.7153474f, -55.0876525f));
        nodes.add(new OSMNode(32407356l, 14.7157779f, -55.0874130f));
        OSMWay osmWay = new OSMWay(nodes);
        assertEquals(32407507l, osmWay.getFirstNodeId());
    }

    @Test
    public void testGetLastNodeId() {
        NodeMap nodes = new NodeMap();
        nodes.add(new OSMNode(32407507l, 14.7142694f, -55.0878006f));
        nodes.add(new OSMNode(32407369l, 14.7146613f, -55.0880375f));
        nodes.add(new OSMNode(32407485l, 14.7153474f, -55.0876525f));
        nodes.add(new OSMNode(32407356l, 14.7157779f, -55.0874130f));
        OSMWay osmWay = new OSMWay(nodes);
        assertEquals(32407356l, osmWay.getLastNodeId());
    }

    @Test
    public void testIsCycle() {
        NodeMap nodes = new NodeMap();
        nodes.add(new OSMNode(32407507l, 14.7142694f, -55.0878006f));
        nodes.add(new OSMNode(32407369l, 14.7146613f, -55.0880375f));
        nodes.add(new OSMNode(32407485l, 14.7153474f, -55.0876525f));
        nodes.add(new OSMNode(32407507l, 14.7157779f, -55.0874130f));
        OSMWay osmWay = new OSMWay(nodes);
        assertEquals(true, osmWay.isCycle());
    }

    @Test
    public void testGetId() {
        NodeMap nodes = new NodeMap();
        nodes.add(new OSMNode(32407507l, 14.7142694f, -55.0878006f));
        nodes.add(new OSMNode(32407369l, 14.7146613f, -55.0880375f));
        nodes.add(new OSMNode(32407485l, 14.7153474f, -55.0876525f));
        nodes.add(new OSMNode(32407507l, 14.7157779f, -55.0874130f));
        OSMWay osmWay = new OSMWay(nodes, 5269321);
        assertEquals(5269321, osmWay.getId());
    }
}