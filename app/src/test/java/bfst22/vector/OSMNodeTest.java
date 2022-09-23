package bfst22.vector;

import bfst22.vector.Data.OSMData.OSMNode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OSMNodeTest {

    @Test
    public void testAddParentWay() {
        OSMNode osmNode = new OSMNode(25055474l, 15.1178151f, -55.0189260f);
        osmNode.addParentWay();
        assertEquals(1, osmNode.getParentWays());
    }

    @Test
    public void testHasMultipleParents() {
        OSMNode osmNode1 = new OSMNode(25055474l, 15.1178151f, -55.0189260f);
        osmNode1.addParentWay();
        osmNode1.addParentWay();
        osmNode1.addParentWay();
        assertEquals(true, osmNode1.hasMultipleParents());
        OSMNode osmNode2 = new OSMNode(25055474l, 15.1178151f, -55.0189260f);
        assertEquals(false, osmNode2.hasMultipleParents());
    }

    @Test
    public void TestGetParentWays() {
        OSMNode osmNode = new OSMNode(25055474l, 15.1178151f, -55.0189260f);
        osmNode.addParentWay();
        osmNode.addParentWay();
        osmNode.addParentWay();
        osmNode.addParentWay();
        assertEquals(4, osmNode.getParentWays());
    }
}