package bfst22.vector;

import bfst22.vector.Data.TSTree.TSTNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TSTNodeTest {

    @Test
    void findLonelyChildTest(){
        TSTNode node1 = new TSTNode(null);
        TSTNode node2 = new TSTNode(null);
        TSTNode node3 = new TSTNode(null);

        node1.setRight(node2);

        TSTNode exp = node2;
        TSTNode act = node1.getLonelyChild();
        assertEquals(exp, act);

        node1.setRight(null);
        node1.setMiddle(node2);
        exp = node2;
        act = node1.getLonelyChild();
        assertEquals(exp, act);

        node1.setMiddle(null);
        node1.setLeft(node2);
        exp = node2;
        act = node1.getLonelyChild();
        assertEquals(exp, act);

        node1.setMiddle(node3);
        exp = null;
        act = node1.getLonelyChild();
        assertEquals(exp, act);

        node1.setMiddle(null);
        node1.setLeft(null);
        exp = null;
        act = node1.getLonelyChild();
        assertEquals(exp, act);

    }

    @Test
    void dataAsStringTest(){
        TSTNode node1 = new TSTNode(new char[]{'a', 'b', 'c'});
        String exp = "[abc]";
        String act = node1.dataAsString();
        assertEquals(exp, act);
    }
}
