package bfst22.vector.Utilities;

import bfst22.vector.Interfaces.Boundable;
import bfst22.vector.Data.Point2D;

//Screen represents the bounds of the program window
public class Screen implements Boundable {
    float[] topRight = new float[2];
    float[] bottomLeft = new float[2];

    public Screen(Point2D topRight2, Point2D bottomLeft2) {
        this.topRight[0] = (float) topRight2.getX();
        this.topRight[1] = (float) topRight2.getY();
        this.bottomLeft[0] = (float) bottomLeft2.getX();
        this.bottomLeft[1] = (float) bottomLeft2.getY();
    }

    @Override
    public float[] getTopRight() {
        return topRight;
    }

    @Override
    public float[] getBottomLeft() {
        return bottomLeft;
    }

    @Override
    public void killBounds() {

    }
}
