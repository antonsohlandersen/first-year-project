package bfst22.vector.Drawables;

import bfst22.vector.Data.Point2D;
import bfst22.vector.Interfaces.Drawable;
import bfst22.vector.Enum.WayType;
import javafx.scene.canvas.GraphicsContext;

public class Line implements Drawable {
    Point2D from, to;

    Line(String line) {
        var parts = line.split(" ");
        var x1 = Double.parseDouble(parts[1]);
        var y1 = Double.parseDouble(parts[2]);
        var x2 = Double.parseDouble(parts[3]);
        var y2 = Double.parseDouble(parts[4]);
        from = new Point2D(x1, y1);
        to = new Point2D(x2, y2);
    }

    public Line(Point2D from, Point2D to) {
        this.from = from;
        this.to = to;
    }

    public void trace(GraphicsContext gc) {
        gc.beginPath();
        gc.moveTo(from.getX(), from.getY());
        gc.lineTo(to.getX(), to.getY());
        gc.stroke();
    }

    @Override
    public float[] getTopRight() {
        return null;
    }

    @Override
    public float[] getBottomLeft() {
        return null;
    }

    @Override
    public void killBounds() {

    }

    @Override
    public WayType getType() {
        return null;
    }

    public Point2D getFrom() {
        return from;
    }

    public Point2D getTo() {
        return to;
    }
}