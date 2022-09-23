package bfst22.vector;

import bfst22.vector.Data.Point2D;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

//Icon represents an icon that can be placed on the MapCanvas
public class Icon {
    Image image;
    Point2D point;
    double factor;
    Affine trans;

    public Icon(Image image, float x, float y, Affine trans) {
        factor = 0.1;
        this.trans = trans;
        this.image = image;
        point = new Point2D(x, y);
    }

    //places the icon on the map, while making sure that the icon stays the same size regardless of zoom-level
    public void placeIcon(GraphicsContext gc) {
        gc.drawImage(image,
                point.getX() - (image.getWidth() * 0.5) * (factor / (Math.sqrt(trans.determinant()))),
                point.getY() - image.getHeight() * (factor / (Math.sqrt(trans.determinant()))),
                (image.getWidth()) * (factor / (Math.sqrt(trans.determinant()))),
                (image.getHeight()) * (factor / (Math.sqrt(trans.determinant()))));
    }

    public Point2D getPoint(){
        return point;
    }

}
