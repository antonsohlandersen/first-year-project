package bfst22.vector.Data.SpatialDataStructures.KDTree;

import bfst22.vector.Data.OSMData.OSMNode;

import java.util.List;

// A KdLeaf extends the initial functionality of KdNode, but also needs to know which data is on the left/top and right/bottom side of its splitline.
public class KdLeaf extends KdNode{
    public static final long serialVersionUID = 9082425;
    private List<OSMNode> topOrLeftData;
    private List<OSMNode> bottomOrRightData;

    public KdLeaf(boolean splitOnX, OSMNode splitPoint, List<OSMNode> topOrLeftData, List<OSMNode> bottomOrRightData) {
        super(splitOnX, splitPoint);
        this.topOrLeftData = topOrLeftData;
        this.bottomOrRightData = bottomOrRightData;
    }

    @Override
    public List<OSMNode> getTopOrLeftData() {
        return topOrLeftData;
    }

    @Override
    public List<OSMNode> getBottomOrRightData() {
        return bottomOrRightData;
    }

    @Override
    public boolean hasData(){
        return true;
    }
}
