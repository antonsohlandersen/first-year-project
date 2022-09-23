package bfst22.vector.Data.OSMData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OSMWay implements Serializable {
    public static final long serialVersionUID = 42;
    private List<OSMNode> nodes;
    private boolean member = false;
    private long id;

    public OSMWay(List<OSMNode> nodes) {
        this.nodes = new ArrayList<>(nodes);
    }

    public OSMWay(List<OSMNode> nodes, long id) {
        this.nodes = new ArrayList<>(nodes);
        this.id = id;
    }

    public void setMember() {
        member = true;
    }

    public boolean isMember() {
        return member;
    }

    public long getFirstNodeId() {
        return nodes.get(0).getId();
    }

    public long getLastNodeId() {
        return nodes.get(nodes.size() - 1).getId();
    }

    public boolean isCycle() {
        return getFirstNodeId() == getLastNodeId();
    }

    public void close() {
        nodes.add(nodes.get(0));
    }

    public long getId() {
        return id;
    }

    public List<OSMNode> getNodes() {
        return nodes;
    }
}
