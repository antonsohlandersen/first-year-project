package bfst22.vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bfst22.vector.Collections.SpecialList;
import bfst22.vector.Data.OSMData.OSMNode;
import bfst22.vector.Data.OSMData.OSMWay;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SpecialListTest {

    SpecialList specialList;
    ArrayList<OSMNode> nodes = new ArrayList<>();
    OSMWay connectedFirstFirst;
    OSMWay connectedLastLast;
    OSMWay connectedLastFirst;
    OSMWay connectedFirstLast;
    OSMWay notConnected;
    OSMWay cycle;
    OSMNode firstNode;
    OSMNode lastNode;

    @BeforeEach
    void setup(){
        firstNode = new OSMNode(24932215, 15.0965532f, -55.0003745f);
        lastNode = new OSMNode(24932218,15.0956869f, -54.9995152f);
        nodes.add(firstNode);
        nodes.add(lastNode);
        OSMWay firstWay = new OSMWay(nodes);
        nodes.clear();
        nodes.add(lastNode);
        nodes.add(firstNode);
        cycle = new OSMWay(nodes);
        nodes.clear();
        nodes.add(firstNode);
        nodes.add(new OSMNode(24932235, 15.0900650f, -54.9937793f));
        connectedFirstFirst = new OSMWay(nodes);
        nodes.clear();
        nodes.add(new OSMNode(24932240, 15.0849688f, -54.9891938f));
        nodes.add(lastNode);
        connectedLastLast = new OSMWay(nodes);
        nodes.clear();
        nodes.add(lastNode);
        nodes.add(new OSMNode(24932244, 15.0844538f, -54.9889106f));
        connectedLastFirst = new OSMWay(nodes);
        nodes.clear();
        nodes.add(new OSMNode(24932248, 15.0822008f, -54.9881412f));
        nodes.add(firstNode);
        connectedFirstLast = new OSMWay(nodes);
        nodes.clear();
        nodes.add(new OSMNode(24932244, 15.0844538f, -54.9889106f));
        nodes.add(new OSMNode(24932248, 15.0822008f, -54.9881412f));
        notConnected = new OSMWay(nodes);
        nodes.clear();
        specialList = new SpecialList(firstWay);
    }

    @Test
    void testFirstID(){
        long exp = 24932215;
        long act = specialList.getFirstID();
        assertEquals(exp, act);
    }

    @Test
    void testLastID(){
        long exp = 24932218;
        long act = specialList.getLastID();
        assertEquals(exp, act);
    }

    @Test
    void testMatchAndInOrder(){
        assertEquals(true, specialList.matchAndAddInOrder(connectedFirstFirst));
    }

    @Test
    void testMatchAndInOrder2(){
        assertEquals(true, specialList.matchAndAddInOrder(connectedLastLast));
    }

    @Test
    void testMatchAndInOrder3(){
        assertEquals(true, specialList.matchAndAddInOrder(connectedFirstLast));
    }

    @Test
    void testMatchAndInOrder4(){
        assertEquals(true, specialList.matchAndAddInOrder(connectedLastFirst));
    }

    @Test
    void testMatchAndInOrder5(){
        assertEquals(false, specialList.matchAndAddInOrder(notConnected));
    }

    @Test
    void testMatchAndInOrderIDFirstFirst(){
        specialList.matchAndAddInOrder(connectedFirstFirst);
        long exp = lastNode.getId();
        long act = specialList.getFirstID();
        assertEquals(exp, act);
        exp = connectedFirstFirst.getLastNodeId();
        act = specialList.getLastID();
        assertEquals(exp, act);
    }

    @Test
    void testMatchAndInOrderIDLastLast(){
        long expLast = connectedLastLast.getFirstNodeId();
        specialList.matchAndAddInOrder(connectedLastLast);
        long expFirst = firstNode.getId();
        long act = specialList.getFirstID();
        assertEquals(expFirst, act);
        act = specialList.getLastID();
        assertEquals(expLast, act);
    }

    @Test
    void testMatchAndInOrderIDLastFirst(){
        specialList.matchAndAddInOrder(connectedLastFirst);
        long exp = firstNode.getId();
        long act = specialList.getFirstID();
        assertEquals(exp, act);
        exp = connectedLastFirst.getLastNodeId();
        act = specialList.getLastID();
        assertEquals(exp, act);
    }

    @Test
    void testMatchAndInOrderIDFirstLast(){
        specialList.matchAndAddInOrder(connectedFirstLast);
        long exp = connectedFirstLast.getFirstNodeId();
        long act = specialList.getFirstID();
        assertEquals(exp, act);
        exp = lastNode.getId();
        act = specialList.getLastID();
        assertEquals(exp, act);
    }

    @Test
    void testIsCycle(){
        specialList.matchAndAddInOrder(cycle);
        assertEquals(true, specialList.isCycle());
    }

    @Test
    void testIsNotCycle(){
        specialList.matchAndAddInOrder(connectedLastFirst);
        assertEquals(false, specialList.isCycle());
    }

}


