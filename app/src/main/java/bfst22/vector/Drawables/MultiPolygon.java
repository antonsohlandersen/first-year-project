package bfst22.vector.Drawables;

import java.util.ArrayList;
import java.util.List;

import bfst22.vector.Data.OSMData.OSMWay;
import bfst22.vector.Enum.WayType;
import javafx.scene.canvas.GraphicsContext;

public class MultiPolygon extends MapElement {
    public static final long serialVersionUID = 1325234;
    List<PolyLine> parts = new ArrayList<>();

    public MultiPolygon(ArrayList<OSMWay> rel, WayType wayType) {
        super(wayType);
        float x_low = -1;
        float y_low = 1;
        float x_high = -1;
        float y_high = 1;
        for (var way : rel) {
            var polyLine = new PolyLine(way.getNodes(), null);
            parts.add(polyLine);
            //finds the bounds of the multipolygon
            if(x_low == -1){
                x_low = polyLine.getBottomLeft()[0];
                y_low = polyLine.getBottomLeft()[1];
                x_high = polyLine.getTopRight()[0];
                y_high = polyLine.getTopRight()[1];
            } else {
                if (x_low > polyLine.getBottomLeft()[0]) {
                    x_low = polyLine.getBottomLeft()[0];
                }
                if (-y_low > -polyLine.getBottomLeft()[1]) {
                    y_low = polyLine.getBottomLeft()[1];
                }
                if (x_high < polyLine.getTopRight()[0]) {
                    x_high = polyLine.getTopRight()[0];
                }
                if (-y_high < -polyLine.getTopRight()[1]) {
                    y_high = polyLine.getTopRight()[1];
                }
            }
            polyLine.killBounds();
        }
        setBottomLeft(x_low, y_low);
        setTopRight(x_high, y_high);
    }

    public void trace(GraphicsContext gc) {
        for (var part : parts) part.trace(gc);
    }



}