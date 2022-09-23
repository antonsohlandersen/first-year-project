package bfst22.vector.Interfaces;

import java.util.Comparator;

//Boundable represents spatial data as a a minimum bounding rectangle.
public interface Boundable {
    public static final Comparator<Boundable> ByLeftX = new Boundable.ByLeftX();
    public static final Comparator<Boundable> ByLeftY = new Boundable.ByLeftY();
    public static final Comparator<Boundable> ByRightX = new Boundable.ByRightX();
    public static final Comparator<Boundable> ByRightY = new Boundable.ByRightY();

    float[] getTopRight();
    float[] getBottomLeft();

    //sets the bounds of the rectangle to null to save memory, if no longer needed
    void killBounds();

    //checks if two minimum bounding rectangles are overlapping
    default boolean isOverLapping(Boundable other){
        if (-this.getTopRight()[1] < -other.getBottomLeft()[1]
                || -this.getBottomLeft()[1] > -other.getTopRight()[1]) {
            return false;
        }
        if (this.getTopRight()[0] < other.getBottomLeft()[0]
                || this.getBottomLeft()[0] > other.getTopRight()[0]) {
            return false;
        }
        return true;
    }

    static class ByLeftX implements Comparator<Boundable>{
        @Override
        public int compare(Boundable v, Boundable w){
            if(v.getBottomLeft()[0] < w.getBottomLeft()[0]){
                return -1;
            } else if(v.getBottomLeft()[0] > w.getBottomLeft()[0]){
                return 1;
            }
            return 0;
        }
    }

    static class ByRightX implements Comparator<Boundable>{
        @Override
        public int compare(Boundable v, Boundable w){
            if(v.getTopRight()[0] < w.getTopRight()[0]){
                return -1;
            } else if(v.getTopRight()[0] > w.getTopRight()[0]){
                return 1;
            }
            return 0;
        }
    }
    static class ByRightY implements Comparator<Boundable>{
        @Override
        public int compare(Boundable v, Boundable w){
            if(-v.getTopRight()[1] < -w.getTopRight()[1]){
                return -1;
            } else if(-v.getTopRight()[1] > -w.getTopRight()[1]){
                return 1;
            }
            return 0;
        }
    }

    static class ByLeftY implements Comparator<Boundable>{
        @Override
        public int compare(Boundable v, Boundable w){
            if(-v.getBottomLeft()[1] < -w.getBottomLeft()[1]){
                return -1;
            } else if(-v.getBottomLeft()[1] > -w.getBottomLeft()[1]){
                return 1;
            }
            return 0;
        }
    }
}

