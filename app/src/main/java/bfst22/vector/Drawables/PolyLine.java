package bfst22.vector.Drawables;

import java.util.List;

import bfst22.vector.Data.OSMData.OSMNode;
import bfst22.vector.Enum.WayType;
import javafx.scene.canvas.GraphicsContext;

public class PolyLine extends MapElement {
    public static final long serialVersionUID = 134123;
    float[] coords;

    public PolyLine(List<OSMNode> nodes, WayType wayType) {
        super(wayType);
        coords = new float[nodes.size() * 2];
        int i = 0;
        float x_low = nodes.get(0).getLon();
        float y_low = nodes.get(0).getLat();
        float x_high = nodes.get(0).getLon();
        float y_high = nodes.get(0).getLat();
        for (var node : nodes) {
            coords[i++] = node.getLon();
            coords[i++] = node.getLat();

            //finds the bounds of the polyline
            if (x_low > node.getLon()) {
                    x_low = node.getLon();
                }
            if (y_low < node.getLat()) {
                    y_low = node.getLat();
                }
            if (x_high < node.getLon()) {
                    x_high = node.getLon();
                }
            if (y_high > node.getLat()) {
                    y_high = node.getLat();
                }
            }
            setTopRight(x_high, y_high);
            setBottomLeft(x_low, y_low);
        }

    @Override
    public void trace(GraphicsContext gc) {
        gc.moveTo(coords[0], coords[1]);
        for (var i = 2 ; i < coords.length ; i += 2) {
            gc.lineTo(coords[i], coords[i+1]);
        }
    }

    public float[] getCoords(){
        return coords;
    }
}
