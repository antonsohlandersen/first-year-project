package bfst22.vector.Drawables;

import java.io.Serializable;

import bfst22.vector.Interfaces.Drawable;
import bfst22.vector.Enum.WayType;
import javafx.scene.canvas.GraphicsContext;

//MapElement represents a static element on the map
public abstract class MapElement implements Drawable, Serializable {
    WayType type;
    float[] bounds = new float[4];

    public MapElement(WayType type) {
        this.type = type;
    }

    @Override
    public float[] getTopRight() {
        return new float[]{bounds[2], bounds[3]};
    }

    @Override
    public float[] getBottomLeft() {
        return new float[]{bounds[0], bounds[1]};
    }

    @Override
    public abstract void trace(GraphicsContext gc);

    @Override
    public WayType getType() {
        return type;
    }

    public void setTopRight(float x, float y) {
        bounds[2] = x;
        bounds[3] = y;
    }

    public void setBottomLeft(float x, float y) {
        bounds[0] = x;
        bounds[1] = y;
    }

    @Override
    public void killBounds(){
        bounds = null;
    }
}
