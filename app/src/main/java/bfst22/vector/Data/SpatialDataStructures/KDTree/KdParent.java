package bfst22.vector.Data.SpatialDataStructures.KDTree;

import bfst22.vector.Data.OSMData.OSMNode;

//A KdParent extends the functionality of a KdNode, but also knows which nodes are its children.
public class KdParent extends KdNode{
    private KdNode topOrLeftChild;
    private KdNode bottomOrRightChild;
    public static final long serialVersionUID = 9082424;

    public KdParent(boolean splitOnX, OSMNode splitPoint, KdNode topOrLeftChild, KdNode bottomOrRightChild) {
        super(splitOnX, splitPoint);
        this.topOrLeftChild = topOrLeftChild;
        this.bottomOrRightChild = bottomOrRightChild;
    }

    @Override
    public KdNode getTopOrLeftChild(){
        return topOrLeftChild;
    }

    @Override
    public KdNode getBottomOrRightChild() {
        return bottomOrRightChild;
    }

    @Override
    public boolean hasData(){
        return false;
    }
}