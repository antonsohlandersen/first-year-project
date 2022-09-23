package bfst22.vector;

import bfst22.vector.Collections.PartitionList;
import bfst22.vector.Data.OSMData.OSMNode;
import bfst22.vector.Data.Point2D;
import bfst22.vector.Data.SpatialDataStructures.RTree.RTree;
import bfst22.vector.Drawables.PolyLine;
import bfst22.vector.Interfaces.Boundable;
import bfst22.vector.Interfaces.Drawable;
import bfst22.vector.Utilities.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RTreeTest {
    RTree rTree;
    Drawable d1, d2, d3, d4, d5, d6, d7, d8;
    PartitionList<Drawable> drawables;

    @BeforeEach
    void setup() throws Exception{
        rTree = new RTree(new float[]{1, -1, 16, -16});
        OSMNode node1 = new OSMNode(0, 1, -1);
        OSMNode node2 = new OSMNode(0, 2, -2);
        ArrayList<OSMNode> nodes1 = new ArrayList<>();
        nodes1.add(node1);
        nodes1.add(node2);
        OSMNode node3 = new OSMNode(0, 3, -3);
        OSMNode node4 = new OSMNode(0, 4, -4);
        ArrayList<OSMNode> nodes2 = new ArrayList<>();
        nodes2.add(node3);
        nodes2.add(node4);
        OSMNode node5 = new OSMNode(0, 5, -5);
        OSMNode node6 = new OSMNode(0, 6, -6);
        ArrayList<OSMNode> nodes3 = new ArrayList<>();
        nodes3.add(node5);
        nodes3.add(node6);
        OSMNode node7 = new OSMNode(0, 7, -7);
        OSMNode node8 = new OSMNode(0, 8, -8);
        ArrayList<OSMNode> nodes4 = new ArrayList<>();
        nodes4.add(node7);
        nodes4.add(node8);
        OSMNode node9 = new OSMNode(0, 9, -9);
        OSMNode node10 = new OSMNode(0, 10, -10);
        ArrayList<OSMNode> nodes5 = new ArrayList<>();
        nodes5.add(node9);
        nodes5.add(node10);
        OSMNode node11 = new OSMNode(0, 11, -11);
        OSMNode node12 = new OSMNode(0, 12, -12);
        ArrayList<OSMNode> nodes6 = new ArrayList<>();
        nodes6.add(node11);
        nodes6.add(node12);
        OSMNode node13 = new OSMNode(0, 13, -13);
        OSMNode node14 = new OSMNode(0, 14, -14);
        ArrayList<OSMNode> nodes7 = new ArrayList<>();
        nodes7.add(node13);
        nodes7.add(node14);
        OSMNode node15 = new OSMNode(0, 15, -15);
        OSMNode node16 = new OSMNode(0, 16, -16);
        ArrayList<OSMNode> nodes8 = new ArrayList<>();
        nodes8.add(node15);
        nodes8.add(node16);
        d1 = new PolyLine(nodes1, null);
        d2 = new PolyLine(nodes2, null);
        d3 = new PolyLine(nodes3, null);
        d4 = new PolyLine(nodes4, null);
        d5 = new PolyLine(nodes5, null);
        d6 = new PolyLine(nodes6, null);
        d7 = new PolyLine(nodes7, null);
        d8 = new PolyLine(nodes8, null);
        drawables = new PartitionList<>();
        drawables.add(d8);
        drawables.add(d1);
        drawables.add(d7);
        drawables.add(d2);
        drawables.add(d6);
        drawables.add(d3);
        drawables.add(d5);
        drawables.add(d4);

    }

    @Test
    void constructTreeTest(){
        rTree.constructLeafs(drawables, 2);
        Boundable screen = new Screen(new Point2D(1.5, -2), new Point2D(1.5, -2));
        rTree.getDataWithin(screen);
        List<Drawable> result = rTree.getResult();

        int expLevel = 3;
        int actLevel = rTree.getTreeLevel();
        assertEquals(expLevel, actLevel);

        int expSize = 2;
        int actSize = result.size();
        assertEquals(expSize, actSize);

        Drawable expDraw = d1;
        Drawable actDraw = result.get(0);

        assertEquals(expDraw, actDraw);

        expDraw = d2;
        actDraw = result.get(1);

        assertEquals(expDraw, actDraw);

        rTree.clearResult();
        screen = new Screen(new Point2D(15, -16), new Point2D(16, -15));
        rTree.getDataWithin(screen);
        result = rTree.getResult();

        expSize = 2;
        actSize = result.size();
        assertEquals(expSize, actSize);

        expDraw = d7;
        actDraw = result.get(0);

        assertEquals(expDraw, actDraw);

        expDraw = d8;
        actDraw = result.get(1);

        assertEquals(expDraw, actDraw);

    }

    @Test
    void findMBRTest(){
        float[] actMBR = rTree.findMBR(drawables);

        int exp1 = 1;
        int exp2 = -1;
        int exp3 = 16;
        int exp4 = -16;
        assertEquals(exp1, actMBR[0]);
        assertEquals(exp2, actMBR[1]);
        assertEquals(exp3, actMBR[2]);
        assertEquals(exp4, actMBR[3]);

        Collections.reverse(drawables);

        actMBR = rTree.findMBRAndKillBounds(drawables);
        assertEquals(exp1, actMBR[0]);
        assertEquals(exp2, actMBR[1]);
        assertEquals(exp3, actMBR[2]);
        assertEquals(exp4, actMBR[3]);


    }


}

