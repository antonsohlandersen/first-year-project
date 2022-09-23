package bfst22.vector.Collections;

import bfst22.vector.Data.OSMData.OSMNode;
import bfst22.vector.Data.OSMData.OSMWay;
import bfst22.vector.Enum.Node_Match_Type;

import java.util.ArrayList;
import java.util.Collections;

//SpecialList is responsible for holding OSMNodes in the correct order,
//while keeping track of the first and last node.
//Used in MultiPolygonBuilder
public class SpecialList extends ArrayList<OSMNode> {
    public static final long serialVersionUID = 9082416;
    long firstID;
    long lastID;

    public SpecialList(OSMWay firstWay) {
        super();
        addFirst(firstWay);
    }

    private void addAndOrder(Node_Match_Type type, OSMWay way) {
        switch (type) {
            case LAST_AND_FIRST: {
                this.addAll(way.getNodes());
                break;
            }
            case FIRST_AND_LAST: {
                this.addAll(0, way.getNodes());
                break;
            }
            case LAST_AND_LAST: {
                Collections.reverse(way.getNodes());
                this.addAll(way.getNodes());
                break;
            }
            case FIRST_AND_FIRST: {
                Collections.reverse(this);
                this.addAll(way.getNodes());
                break;
            }
        }
        setNewID();
    }

    //returns true if an OSMWay connects to the first or last node in the list.
    //the nodes of the OSMWay are then added to the list in the correct order
    public boolean matchAndAddInOrder(OSMWay way) {
        if (lastID == way.getFirstNodeId()) {
            addAndOrder(Node_Match_Type.LAST_AND_FIRST, way);
            return true;
        }
        if (firstID == way.getLastNodeId()) {
            addAndOrder(Node_Match_Type.FIRST_AND_LAST, way);
            return true;
        }
        if (lastID == way.getLastNodeId()) {
            addAndOrder(Node_Match_Type.LAST_AND_LAST, way);
            return true;
        }
        if (firstID == way.getFirstNodeId()) {
            addAndOrder(Node_Match_Type.FIRST_AND_FIRST, way);
            return true;
        } else {
            return false;
        }
    }

    public void addFirst(OSMWay way) {
        if (this.isEmpty()) {
            super.addAll(way.getNodes());
            setNewID();
        }
    }

    public long getFirstID() {
        return firstID;
    }

    public long getLastID() {
        return lastID;
    }

    public void setNewID() {
        firstID = this.get(0).getId();
        lastID = this.get(this.size() - 1).getId();
    }

    public boolean isCycle() {
        return firstID == lastID;
    }
}
