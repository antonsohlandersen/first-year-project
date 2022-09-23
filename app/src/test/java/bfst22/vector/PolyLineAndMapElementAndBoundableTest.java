package bfst22.vector;

import bfst22.vector.Data.OSMData.OSMNode;
import bfst22.vector.Drawables.MapElement;
import bfst22.vector.Drawables.PolyLine;
import bfst22.vector.Enum.WayType;
import bfst22.vector.Interfaces.Boundable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PolyLineAndMapElementAndBoundableTest {
    //The values chosen for the different arrays, are chosen such that they check all possible if-statements in the comparators in the Boundable interface
    OSMNode[] nodes1 = new OSMNode[]{new OSMNode(1, 10, -10), new OSMNode(2, 15, -5), new OSMNode(3, 20, 0), new OSMNode(4, 18, -20), new OSMNode(5, 8, -20)};
    PolyLine pl1 = new PolyLine(new ArrayList<>(Arrays.asList(nodes1)), WayType.WATER);
    OSMNode[] nodes2 = new OSMNode[]{new OSMNode(6, 10, -150), new OSMNode(7, 35, 0), new OSMNode(8, 0, -55)};
    PolyLine pl2 = new PolyLine(new ArrayList<>(Arrays.asList(nodes2)), WayType.BEACH);
    OSMNode[] nodes3 = new OSMNode[]{new OSMNode(9, 0, -60), new OSMNode(10, 100, -150)};
    PolyLine pl3 = new PolyLine(new ArrayList<>(Arrays.asList(nodes3)), WayType.PRIMARY);
    OSMNode[] nodes4 = new OSMNode[]{new OSMNode(9, 21, -10), new OSMNode(10, 100, -200)};
    PolyLine pl4 = new PolyLine(new ArrayList<>(Arrays.asList(nodes4)), WayType.HIGHWAY);

    @Test
    public void makePolyLineTest(){
        //Here, we check that the nodes stored in the PolyLine are correct
        assertEquals(nodes1[0].getLon(), pl1.getCoords()[0]);
        assertEquals(nodes1[0].getLat(), pl1.getCoords()[1]);
        assertEquals(nodes1[1].getLon(), pl1.getCoords()[2]);
        assertEquals(nodes1[1].getLat(), pl1.getCoords()[3]);
        assertEquals(nodes1[2].getLon(), pl1.getCoords()[4]);
        assertEquals(nodes1[2].getLat(), pl1.getCoords()[5]);
        assertEquals(nodes1[3].getLon(), pl1.getCoords()[6]);
        assertEquals(nodes1[3].getLat(), pl1.getCoords()[7]);
        assertEquals(nodes1[4].getLon(), pl1.getCoords()[8]);
        assertEquals(nodes1[4].getLat(), pl1.getCoords()[9]);
    }

    public void getTypeTest(){
        //Here, we check that the type of the PolyLine is stored and returned correctly
        assertEquals(WayType.WATER, pl1.getType());
    }

    public void getCornerCoordinatesTest(){
        //Here, we test that the polylines bounding box is constructed correctly.
        //It should be said that because the PolyLines y-coords are negative, the polylines bounding box has been turned around.
        //This means that bottomleft, is actually topleft and topright is actually bottomright, if you were to draw the box.
        assertEquals(pl1.getBottomLeft()[0], 8);
        assertEquals(pl1.getBottomLeft()[1], 0);
        assertEquals(pl1.getTopRight()[0], 20);
        assertEquals(pl1.getTopRight()[1], -20);
    }

    @Test
    public void sortBoundablesTest(){
        //Here, we test that the boundables are sorted correctly, with the different comparators
        ArrayList<MapElement> boundables = new ArrayList<>();
        boundables.add(pl1);
        boundables.add(pl2);
        boundables.add(pl3);
        boundables.add(pl4);
        assertEquals(WayType.WATER, boundables.get(0).getType());
        assertEquals(WayType.BEACH, boundables.get(1).getType());
        assertEquals(WayType.PRIMARY, boundables.get(2).getType());
        assertEquals(WayType.HIGHWAY, boundables.get(3).getType());

        Collections.sort(boundables, Boundable.ByLeftX);
        assertEquals(WayType.BEACH, boundables.get(0).getType());
        assertEquals(WayType.PRIMARY, boundables.get(1).getType());
        assertEquals(WayType.WATER, boundables.get(2).getType());
        assertEquals(WayType.HIGHWAY, boundables.get(3).getType());

        Collections.sort(boundables, Boundable.ByRightX);
        assertEquals(WayType.WATER, boundables.get(0).getType());
        assertEquals(WayType.BEACH, boundables.get(1).getType());
        assertEquals(WayType.PRIMARY, boundables.get(2).getType());
        assertEquals(WayType.HIGHWAY, boundables.get(3).getType());

        Collections.sort(boundables, Boundable.ByLeftY);
        assertEquals(WayType.WATER, boundables.get(0).getType());
        assertEquals(WayType.BEACH, boundables.get(1).getType());
        assertEquals(WayType.HIGHWAY, boundables.get(2).getType());
        assertEquals(WayType.PRIMARY, boundables.get(3).getType());

        Collections.sort(boundables, Boundable.ByRightY);
        assertEquals(WayType.WATER, boundables.get(0).getType());
        assertEquals(WayType.BEACH, boundables.get(1).getType());
        assertEquals(WayType.PRIMARY, boundables.get(2).getType());
        assertEquals(WayType.HIGHWAY, boundables.get(3).getType());
    }

    @Test
    public void isOverlappingTest(){
        //Here, we test the different possible outcomes from the isOverLapping method
        //pl1 has quiet a small bounding box, and therefore, it does not overlap with pl3 and pl4
        assertEquals(false, pl1.isOverLapping(pl3));
        assertEquals(false, pl3.isOverLapping(pl1));
        assertEquals(false, pl1.isOverLapping(pl4));
        assertEquals(false, pl4.isOverLapping(pl1));
        assertEquals(true, pl2.isOverLapping(pl4));
    }
}
