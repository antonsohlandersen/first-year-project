package bfst22.vector.Data.SpatialDataStructures.RTree;

import java.util.List;
import bfst22.vector.Interfaces.Drawable;
import bfst22.vector.exceptions.NoSlicesException;

//Concrete instance of AbstractNode that can hold data (drawables)
public class RNodeLeaf extends AbstractRNode {
    public static final long serialVersionUID = 9883631;
    private List<Drawable> data;

    public RNodeLeaf(List<Drawable> data, float[] bounds) {
        super(bounds);
        this.data = data;
    }

    public List<AbstractRNode> getSlices() throws NoSlicesException {
        return null;
    }

    @Override
    public List<Drawable> getData(){
        return data;
    }

    @Override
    public boolean hasData() {
        return true;
    }
}
