package bfst22.vector.Data.SpatialDataStructures.RTree;

import bfst22.vector.Interfaces.Boundable;
import bfst22.vector.Interfaces.Drawable;
import bfst22.vector.Collections.PartitionList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//The RTree stores all static elements that are drawn on the map, i.e. drawables (except for coastlines).
//Based on implementation from https://medium.com/analytics-vidhya/bulk-loading-r-trees-and-how-to-store-higher-dimension-data-c7da26e4f853
public class RTree implements Serializable {
    public static final long serialVersionUID = 9082417;
    private int treeLevel;
    private List<Drawable> result = new ArrayList<>();
    private ArrayList<AbstractRNode> allNodes;
    private float[] maxBoundingBox;
    private AbstractRNode root;

    public RTree(float[] maxBoundingBox) {
        allNodes = new ArrayList<>();
        this.maxBoundingBox = maxBoundingBox;
    }

    //Divides a list into smaller sorted slices and returns a list containing the slices
    private <E> List<List<E>> sliceAndDeliver(PartitionList<E> mbrList, int numberOfSlices, int nodeCanHold){

        List<List<E>> slices = mbrList.partition((int) Math.ceil(numberOfSlices * nodeCanHold));

        for (List slice : slices) {
            Collections.sort(slice, Drawable.ByLeftY);
        }
        mbrList = new PartitionList<>();
        for (List<E> slice : slices) {
            for (E mbr : slice) {
                mbrList.add(mbr);
            }
        }

        slices = mbrList.partition(nodeCanHold);
        return slices;
    }

    //Constructs the RTree recursively from the bottom up using STR bulk-loading
    //Drawables are grouped by coordinates to minimize overlap and put into nodes
    public void constructLeafs(PartitionList<Drawable> mbrList, int nodeCanHold) {

        Collections.sort(mbrList, Drawable.ByLeftX);

        int length = mbrList.size();

        int numberOfNodes = (int) Math.ceil((double) length / (double) nodeCanHold);

        int numberOfSlices = (int) Math.ceil(Math.sqrt(numberOfNodes));

        if (numberOfSlices > 1) {
            List<List<Drawable>> slices = sliceAndDeliver(mbrList, numberOfSlices, nodeCanHold);

            PartitionList<AbstractRNode> nodes = new PartitionList<>();
            // find the bounds of each slice/RLeaf
            for (int i = 0; i < slices.size(); i++) {
                RNodeLeaf node = new RNodeLeaf(slices.get(i), findMBRAndKillBounds(slices.get(i)));
                nodes.add(node);
                allNodes.add(node);
            }
            treeLevel += 1;
            // recursively construct from bottom to top
            constructNodes(nodes, nodeCanHold);
        } else {

            Collections.sort(mbrList, Boundable.ByLeftY);
            RNodeLeaf node = new RNodeLeaf(mbrList, maxBoundingBox);
            root = node;
            treeLevel += 1;
        }
    }

    //Continues the construction of the RTree
    private void constructNodes(PartitionList<AbstractRNode> mbrList, int nodeCanHold) {

        Collections.sort(mbrList, Boundable.ByLeftX);

        int length = mbrList.size();

        int numberOfNodes = (int) Math.ceil((double) length / (double) nodeCanHold);

        int numberOfSlices = (int) Math.ceil(Math.sqrt(numberOfNodes));

        if (numberOfSlices > 1) {

            List<List<AbstractRNode>> slices = sliceAndDeliver(mbrList, numberOfSlices, nodeCanHold);

            PartitionList<AbstractRNode> nodes = new PartitionList<>();
            for (int i = 0; i < slices.size(); i++) {
                RNodeParent node = new RNodeParent(slices.get(i), findMBR(slices.get(i)));
                nodes.add(node);
            }
            treeLevel += 1;
            // recursively construct from bottom to top
            constructNodes(nodes, nodeCanHold);

        } else {

            Collections.sort(mbrList, Boundable.ByLeftY);
            AbstractRNode node = new RNodeParent(mbrList, maxBoundingBox) {
            };
            root = node;
            treeLevel += 1;
        }

    }

    public void clearResult() {
        result.clear();
    }

    //finds data within the RTree that overlaps with a Boundable object
    public void getDataWithin(Boundable screen){
        getDataWithin(root, screen);
    }

    private void getDataWithin(AbstractRNode root, Boundable screen) {

        if (root.hasData()) {
            result.addAll(root.getData());
        } else {
            if (root.getSlices() == null) {
                return;
            }
            for (AbstractRNode node : root.getSlices()) {
                if (screen.isOverLapping(node)) {
                    getDataWithin(node, screen);
                }
            }
        }
    }

    public int getTreeLevel(){
        return treeLevel;
    }

    //finds the minimum bounding recangle of a list of Boundable objects
    public float[] findMBR(List<? extends Boundable> mbrList) {
        float xLow = mbrList.get(0).getBottomLeft()[0];
        float yLow = mbrList.get(0).getBottomLeft()[1];
        float xHigh = mbrList.get(0).getTopRight()[0];
        float yHigh = mbrList.get(0).getTopRight()[1];
        for (Boundable mbr : mbrList) {
            if (xLow > mbr.getBottomLeft()[0]) {
                xLow = mbr.getBottomLeft()[0];
            }
            if (-yLow > -mbr.getBottomLeft()[1]) {
                yLow = mbr.getBottomLeft()[1];
            }
            if (xHigh < mbr.getTopRight()[0]) {
                xHigh = mbr.getTopRight()[0];
            }
            if (-yHigh < -mbr.getTopRight()[1]) {
                yHigh = mbr.getTopRight()[1];
            }
        }

        return new float[] { xLow, yLow, xHigh, yHigh };
    }

    //finds the minimum bounding recangle of a list of Boundable objects
    //and sets the bounds of the individual objects to null to save space
    public float[] findMBRAndKillBounds(List<? extends Boundable> mbrList) {
        float xLow = mbrList.get(0).getBottomLeft()[0];
        float yLow = mbrList.get(0).getBottomLeft()[1];
        float xHigh = mbrList.get(0).getTopRight()[0];
        float yHigh = mbrList.get(0).getTopRight()[1];
        for (Boundable mbr : mbrList) {
            if (xLow > mbr.getBottomLeft()[0]) {
                xLow = mbr.getBottomLeft()[0];
            }
            if (-yLow > -mbr.getBottomLeft()[1]) {
                yLow = mbr.getBottomLeft()[1];
            }
            if (xHigh < mbr.getTopRight()[0]) {
                xHigh = mbr.getTopRight()[0];
            }
            if (-yHigh < -mbr.getTopRight()[1]) {
                yHigh = mbr.getTopRight()[1];
            }
            mbr.killBounds();
        }

        return new float[] { xLow, yLow, xHigh, yHigh };
    }

    public List<Drawable> getResult() {
        return result;
    }

}
