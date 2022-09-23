package bfst22.vector.Data.SpatialDataStructures.KDTree;

import bfst22.vector.Interfaces.Nodeable;
import bfst22.vector.Data.OSMData.OSMNode;

import java.io.Serializable;
import java.util.List;

// A KdNode in our program knows two things: Whether the node splits the dataset for the leaves by its y- or x-coordinate, and also that exact coordinate. 
public abstract class KdNode implements Serializable, Nodeable {
    public static final long serialVersionUID = 9082423;
    private float splitCoord;
    private boolean splitOnX;

    public KdNode(boolean splitOnX, OSMNode splitPoint){
        this.splitOnX = splitOnX;
        if (splitOnX == true) {
            this.splitCoord = (float) splitPoint.getLon();
        } else {
            this.splitCoord = (float) splitPoint.getLat();
        }
    }

    public float getSplitCord() {
        return splitCoord;
    }

    public boolean getSplitOnX() {
        return splitOnX;
    }

    public abstract boolean hasData();

    public List<OSMNode> getTopOrLeftData(){
        return null;
    }

    public List<OSMNode> getBottomOrRightData(){
        return null;
    }

    public KdNode getTopOrLeftChild() {
        return null;
    }

    public KdNode getBottomOrRightChild() {
        return null;
    }
}