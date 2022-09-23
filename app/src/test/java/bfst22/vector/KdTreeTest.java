package bfst22.vector;

import bfst22.vector.Collections.PartitionList;
import bfst22.vector.Data.OSMData.OSMNode;
import bfst22.vector.Data.SpatialDataStructures.KDTree.KdTree;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;

import javafx.geometry.Point2D;

public class KdTreeTest {
    static KdTree tree;
    static PartitionList<OSMNode> nodes = new PartitionList<>();
    static OSMNode[] nodeArray = new OSMNode[26];
    
    @BeforeAll
    public static void makeTree(){
        /*nodes.add(new OSMNode(0, 0, 0));
        nodes.add(new OSMNode(1, 100, 0));
        nodes.add(new OSMNode(2, 100, -100));
        nodes.add(new OSMNode(3, 0, -100));
        nodes.add(new OSMNode(4, 25, -25));
        nodes.add(new OSMNode(5, 75, -75));
        nodes.add(new OSMNode(6, 75, -25));
        nodes.add(new OSMNode(7, 50, -75));
        nodes.add(new OSMNode(8, 30, -200));
        nodes.add(new OSMNode(9, 99, -999));
        nodes.add(new OSMNode(10, 0, 0));*/

        nodeArray[0] = (new OSMNode(0, (float)8.23, (float)-55.32));
        nodeArray[1] = (new OSMNode(1, (float)8.35, (float)-55.29));
        nodeArray[2] = (new OSMNode(2, (float)8.49, (float)-55.3));
        nodeArray[3] = (new OSMNode(3, (float)8.24, (float)-55.23));
        nodeArray[4] = (new OSMNode(4, (float)8.36, (float)-55.18));
        nodeArray[5] = (new OSMNode(5, (float)8.49, (float)-55.14));
        nodeArray[6] = (new OSMNode(6, (float)8.24, (float)-55.11));
        nodeArray[7] = (new OSMNode(7, (float)8.35, (float)-55.06));
        nodeArray[8] = (new OSMNode(8, (float)8.5, (float)-55.02));
        nodeArray[9] = (new OSMNode(9, (float)8.24, (float)-55.99));
        nodeArray[10] = (new OSMNode(10, (float)8.45, (float)-55.59));
        nodeArray[11] = (new OSMNode(11, (float)8.30, (float)-55.25));
        nodeArray[12] = (new OSMNode(12, (float)8.33, (float)-55.39));
        nodeArray[13] = (new OSMNode(13, (float)8.46, (float)-55.04));
        nodeArray[14] = (new OSMNode(14, (float)8.69, (float)-55.16));
        nodeArray[15] = (new OSMNode(15, (float)8.65, (float)-55.27));
        nodeArray[16] = (new OSMNode(16, (float)8.6, (float)-55.68));
        nodeArray[17] = (new OSMNode(17, (float)8.41, (float)-55.69));
        nodeArray[18] = (new OSMNode(18, (float)8.4, (float)-55.46));
        nodeArray[19] = (new OSMNode(19, (float)8.26, (float)-55.28));
        nodeArray[20] = (new OSMNode(20, (float)8.3, (float)-55.29));
        nodeArray[21] = (new OSMNode(21, (float)8.12, (float)-55.55));
        nodeArray[22] = (new OSMNode(22, (float)7.54, (float)-55.78));
        nodeArray[23] = (new OSMNode(23, (float)8.13, (float)-55.14));
        nodeArray[24] = (new OSMNode(24, (float)7.37, (float)-54.56));
        nodeArray[25] = (new OSMNode(25, (float)8.35, (float)-54.87));

        for(OSMNode node : nodeArray){
            nodes.add(node);
        }

        tree = new KdTree(2, nodes, true);
    }

    @Test
    public void findResultTest(){
        Point2D[] pointsToTest = new Point2D[13];
        pointsToTest[0] = new Point2D(8.20, -55.99);
        pointsToTest[1] = new Point2D(8.31, -55.29);
        pointsToTest[2] = new Point2D(8.23, -55.76);
        pointsToTest[3] = new Point2D(7.62, -54.93);
        pointsToTest[4] = new Point2D(8.42, -54.93);
        pointsToTest[5] = new Point2D(8.43, -54.42);
        pointsToTest[6] = new Point2D(8.71, -55.09);
        pointsToTest[7] = new Point2D(8.9, -55.13);
        pointsToTest[8] = new Point2D(8.42, -55.91);
        pointsToTest[9] = new Point2D(7.31, -55.23);
        pointsToTest[10] = new Point2D(8.258, -55.292);
        pointsToTest[11] = new Point2D(7.64, -55.14);
        pointsToTest[12] = new Point2D(8.345, -55.46);

        for(int i = 0; i < pointsToTest.length; i++){
            float shortestDistance = Float.MAX_VALUE;
            Point2D currentPoint = pointsToTest[i];
            OSMNode closestPoint = null;
            for (OSMNode node : nodes) {
                float currentDistance = (float) Math.sqrt(Math.pow(Math.abs(currentPoint.getX() - node.getLon()), 2) + Math.pow(Math.abs(currentPoint.getY() - node.getLat()), 2));
                if (currentDistance < shortestDistance) {
                    shortestDistance = currentDistance;
                    closestPoint = node;
                }
            }
            assertEquals(closestPoint, tree.findResult(new bfst22.vector.Data.Point2D(currentPoint.getX(), currentPoint.getY())), ("failed at point with index: " + i));
        }


        /*assertEquals(nodeArray[0], tree.findResult(new Point2D(0,0)));
        assertEquals(nodeArray[0], tree.findResult(new Point2D(0,-25)));
        assertEquals(nodeArray[4], tree.findResult(new Point2D(5,-25)));
        assertEquals(nodeArray[6], tree.findResult(new Point2D(70, -20)));
        assertEquals(nodeArray[9], tree.findResult(new Point2D(0, -10000)));
        assertEquals(nodeArray[0], tree.findResult(new Point2D(-10, -10)));
        assertEquals(nodeArray[0], tree.findResult(new Point2D(-10, 10)));
        assertEquals(nodeArray[9], tree.findResult(new Point2D(1000, -1000)));
        assertEquals(nodeArray[8], tree.findResult(new Point2D(51, -200)));
        assertEquals(nodeArray[8], tree.findResult(new Point2D(76, -200)));
        assertEquals(nodeArray[0], tree.findResult(new Point2D(-10, 10)));
        assertEquals(nodeArray[0], tree.findResult(new Point2D(-10, 10)));
        assertEquals(nodeArray[0], tree.findResult(new Point2D(-10, 10)));*/
    }
}