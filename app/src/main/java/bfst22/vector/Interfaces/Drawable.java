package bfst22.vector.Interfaces;

import bfst22.vector.Enum.WayType;
import javafx.scene.canvas.GraphicsContext;

import java.util.Comparator;

public interface Drawable extends Boundable, Comparable<Drawable> {

    public static final Comparator<Drawable> ByWayType = new Drawable.ByWayType();

    static class ByWayType implements Comparator<Drawable>{
        @Override
        public int compare(Drawable v, Drawable w){
            return v.getType().compareTo(w.getType());
        }
    }
    @Override
    default int compareTo(Drawable o) {
        return this.getType().compareTo(o.getType());
    }

    default void draw(GraphicsContext gc) {
        gc.beginPath();
        trace(gc);
        gc.stroke();
    }
    default void fill(GraphicsContext gc) {
        gc.beginPath();
        trace(gc);
        gc.fill();
    }
    void trace(GraphicsContext gc);

    WayType getType();
}
