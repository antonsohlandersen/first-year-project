package bfst22.vector.Data.SpatialDataStructures.RTree;

import bfst22.vector.Interfaces.Boundable;
import bfst22.vector.Interfaces.Drawable;
import bfst22.vector.Interfaces.Nodeable;
import java.io.Serializable;
import java.util.List;

//AbstractRNode represents a node in the RTree, which can either hold data (drawables)
//or refer to RNodes on a lower level of the tree
public abstract class AbstractRNode implements Boundable, Nodeable, Serializable {
    public static final long serialVersionUID = 9982420;

    private float[] topRight = new float[2];
    private float[] bottomLeft = new float[2];

    public AbstractRNode(float[] bounds) {
        bottomLeft[0] = bounds[0];
        bottomLeft[1] = bounds[1];
        topRight[0] = bounds[2];
        topRight[1] = bounds[3];
    }

    public abstract List<AbstractRNode> getSlices();

    public abstract List<Drawable> getData();

    @Override
    public float[] getTopRight() {
        return topRight;
    }

    @Override
    public float[] getBottomLeft() {
        return bottomLeft;
    }

    @Override
    public void killBounds() {
        topRight = null;
        bottomLeft = null;
    }
}
