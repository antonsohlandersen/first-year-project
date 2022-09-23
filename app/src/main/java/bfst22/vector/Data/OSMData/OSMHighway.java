package bfst22.vector.Data.OSMData;

import java.util.List;

public class OSMHighway extends OSMWay {

    private int maxSpeed;
    private boolean car, bike, walk;
    private boolean oneway;

    public OSMHighway(List<OSMNode> nodes, long id, int maxSpeed, boolean car, boolean bike, boolean walk,
            boolean oneway) {
        super(nodes, id);
        this.maxSpeed = maxSpeed;
        this.car = car;
        this.bike = bike;
        this.walk = walk;
        this.oneway = oneway;
    }

    public boolean getCar() {
        return car;
    }

    public boolean getBike() {
        return bike;
    }

    public boolean getWalk() {
        return walk;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public boolean getOneWay() {
        return oneway;
    }
}
