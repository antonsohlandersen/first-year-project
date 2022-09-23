package bfst22.vector;

import bfst22.vector.Data.SpatialDataStructures.RTree.AbstractRNode;
import bfst22.vector.Data.SpatialDataStructures.RTree.RNodeLeaf;
import bfst22.vector.Data.SpatialDataStructures.RTree.RNodeParent;
import bfst22.vector.Interfaces.Drawable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bfst22.vector.exceptions.NoSlicesException;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class RNodeTest {
    ArrayList<Drawable> drawables;
    ArrayList<AbstractRNode> slices;
    RNodeLeaf node1;
    RNodeParent node2;
    RNodeParent node3;

    @BeforeEach
    public void makeInitialNodesTest(){
        drawables = new ArrayList<>();
        slices = new ArrayList<>();

        node1 = new RNodeLeaf(drawables, new float[]{10, -100, 30, -50});
        node2 = new RNodeParent(slices, new float[]{15, -55, 25, -65});
        node3 = new RNodeParent(null, new float[]{13, -53, 23, -63});
    }


    @Test
    public void slicesTest(){
        assertThrows(NoSlicesException.class, () -> {
            node3.getSlices();
        });
    }

    @Test
    public void dataTest(){
        assertEquals(drawables, node1.getData());
        assertEquals(null, node1.getSlices());
        assertEquals(null, node2.getData());
        assertEquals(slices, node2.getSlices());

        assertEquals(true, node1.hasData());
        assertEquals(false, node2.hasData());
    }

    @Test
    public void boundsTest(){
        float[] act = node1.getBottomLeft();
        float[] exp = new float[]{10, -100};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);

        act = node1.getTopRight();
        exp = new float[]{30, -50};
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);

        node1.killBounds();
        exp = null;
        act = node1.getBottomLeft();
        assertEquals(exp, act);

        exp = null;
        act = node1.getTopRight();
        assertEquals(exp, act);
    }

    
}
