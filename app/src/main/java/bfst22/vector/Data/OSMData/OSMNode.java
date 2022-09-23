package bfst22.vector.Data.OSMData;

import java.io.Serializable;
import java.util.Comparator;

public class OSMNode implements Serializable {
    public static final long serialVersionUID = 9082413;
    private long id;
    private float lon, lat;
    private String streetName;
    private int parentWays;

    public OSMNode(long id, float lon, float lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        parentWays = 0;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName.intern();
    }

    public void addParentWay() {
        parentWays += 1;
    }

    public boolean hasMultipleParents() {
        if (parentWays > 1) {
            return true;
        }
        return false;
    }

    public int getParentWays() {
        return parentWays;
    }

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }

    public long getId() {
        return id;
    }

    public String getStreetName() {
        return streetName;
    }

    public static final Comparator<OSMNode> ByX = new OSMNode.ByX();
    public static final Comparator<OSMNode> ByY = new OSMNode.ByY();

    static class ByX implements Comparator<OSMNode> {
        @Override
        public int compare(OSMNode v, OSMNode w) {
            if (v.lon < w.lon) {
                return -1;
            } else if (v.lon > w.lon) {
                return 1;
            }
            return 0;
        }
    }

    static class ByY implements Comparator<OSMNode> {
        @Override
        public int compare(OSMNode v, OSMNode w) {
            if (v.lat < w.lat) {
                return -1;
            } else if (v.lat > w.lat) {
                return 1;
            }
            return 0;
        }
    }
}
