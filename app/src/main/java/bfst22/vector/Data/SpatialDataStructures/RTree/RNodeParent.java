package bfst22.vector.Data.SpatialDataStructures.RTree;

import bfst22.vector.Interfaces.Drawable;
import java.util.List;

import bfst22.vector.exceptions.NoSlicesException;

//Concrete instance of AbstractNode that can hold slices (references to other nodes)
public class RNodeParent extends AbstractRNode {
    public static final long serialVersionUID = 9082422;
    private List<AbstractRNode> slices;

    public RNodeParent(List<AbstractRNode> slices, float[] bounds) {
        super(bounds);
        this.slices = slices;
    }

    @Override
    public List<AbstractRNode> getSlices() throws NoSlicesException {
        if(slices == null){
            throw new NoSlicesException();
        }
        return slices;
    }

    @Override
    public List<Drawable> getData(){
        return null;
    }

    @Override
    public boolean hasData() {
        return false;
    }
}

