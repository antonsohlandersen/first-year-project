package bfst22.vector;

import bfst22.vector.Collections.Path;
import bfst22.vector.Data.SpatialDataStructures.RTree.RTree;
import bfst22.vector.Drawables.Line;
import bfst22.vector.Enum.ThemeType;
import bfst22.vector.Enum.WayType;
import bfst22.vector.Interfaces.Boundable;
import bfst22.vector.Interfaces.Drawable;
import bfst22.vector.Utilities.ColorChanger;
import bfst22.vector.Utilities.Screen;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.transform.Affine;
import javafx.scene.transform.NonInvertibleTransformException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapCanvas extends Canvas {

    private Model model;
    private ThemeType theme;
    private Affine trans = new Affine();
    private boolean rTreedebugMode = false;
    private boolean kdTreeDebugMode = false;
    private boolean aStarDebugMode = false;
    private boolean visitedEdgesMode = false;
    private Boundable screen;
    private Drawable coastlines;
    private bfst22.vector.Data.Point2D topRight;
    private bfst22.vector.Data.Point2D bottomLeft;

    Set<WayType> shouldBeDrawn = new HashSet<>(Arrays.asList(WayType.SECONDARY, WayType.MOTORWAY, WayType.TRUNK,
            WayType.TERTIARY, WayType.PRIMARY, WayType.RESIDENTIAL_HIGHWAY, WayType.UNCLASSIFIED_HIGHWAY,
            WayType.SERVICE, WayType.BRIDLEWAY, WayType.BUS_GUIDEWAY, WayType.FOOTWAY, WayType.CYCLEWAY,
            WayType.LIVING_STREET, WayType.TRACK, WayType.PATH, WayType.RIVER));
    Set<WayType> shouldBeFilled = new HashSet<>(Arrays.asList(WayType.BEACH, WayType.FOREST, WayType.GRASS,
            WayType.GRASSLAND, WayType.MEADOW, WayType.PARK, WayType.WATER,
            WayType.WETLAND, WayType.WOOD, WayType.SCRUB, WayType.RESIDENTIAL, WayType.INDUSTRIAL, WayType.CEMETERY,
            WayType.RECREATION_GROUND,
            WayType.GOLF, WayType.PITCH, WayType.BUILDING));

    void init(Model model, double width, double height) {
        this.model = model;
        pan(-model.getMinlon(), -model.getMinlat());
        zoom(1500 / (model.getMaxlon() - model.getMinlon()), 0, 0);
        model.addObserver(this::repaint);
        this.widthProperty().unbind();
        this.heightProperty().unbind();
        setWidth(width);
        setHeight(height);
        theme = ThemeType.NORMAL;
        repaint();
    }

    void repaint() {
        ColorChanger.setTheme(theme);
        var gc = getGraphicsContext2D();
        gc.setTransform(new Affine());
        gc.setFill(ColorChanger.getColor(WayType.BACKGROUND));
        gc.fillRect(0, 0, getWidth(), getHeight());
        gc.setLineWidth(1);
        gc.beginPath();
        gc.setTransform(trans);
        screen = setScreen();
        gc.setFillRule(FillRule.EVEN_ODD);
        fillCoastlines(gc);
        drawMapWithinScreen(gc);
        drawDebugModeElements(gc);
        if(!getAStarDebugMode()) {
            drawDijkstraPath();
        }
        if(getAStarDebugMode()) {
            drawAStarPath();
        }
        drawRouteStartDot(gc);
        placeIcons(gc);
    }

    private void drawRouteStartDot(GraphicsContext gc) {
        gc.setFill(Color.RED);
        if(model.hasRouteStartNode() && !model.hasRouteEndNode()) {
            gc.fillOval(model.getRouteStartPoint().getX(), model.getRouteStartPoint().getY(),
                    2 / Math.sqrt(trans.determinant()) + 0.000025, 2 / Math.sqrt(trans.determinant()) + 0.000025);
        }
    }

    private void drawDebugModeElements(GraphicsContext gc) {
        if (kdTreeDebugMode) {
            gc.setStroke(Color.RED);
            model.getPointTree().getLines();
            List<Line> lines = model.getPointTree().getLines();
            for (Line line : lines) {
                line.draw(gc);
            }
        }
        if (rTreedebugMode) {
            gc.setStroke(Color.BLACK);
            Point2D topLeft = null;
            Point2D bottomRight = null;
            double width = 0;
            double height = 0;
            try {
                topLeft = trans.inverseTransform(getWidth() * 0.33, getHeight() * 0.33);
                bottomRight = trans.inverseTransform(getWidth() * 0.66, getHeight() * 0.66);
                width = bottomRight.getX() - topLeft.getX();
                height = bottomRight.getY() - topLeft.getY();
            } catch (NonInvertibleTransformException e) {
                e.printStackTrace();
            }
            gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
            gc.strokeRect(topLeft.getX(), topLeft.getY(), width, height);
        }
        if(visitedEdgesMode) {
            drawAllVisitedEdges();
        }
    }

    private void drawMapWithinScreen(GraphicsContext gc){
        gc.setLineWidth((2 / Math.sqrt(trans.determinant()) + 0.00002));
        for (RTree tree : model.getCurrentTrees()) {
            tree.getDataWithin(screen);
            for (Drawable d : tree.getResult()) {
                WayType dType = d.getType();
                if (shouldBeDrawn.contains(dType)) {
                    gc.setStroke(ColorChanger.getColor(dType));
                    d.draw(gc);
                } else if (shouldBeFilled.contains(dType)) {
                    gc.setFill(ColorChanger.getColor(dType));
                    d.fill(gc);
                }
            }
            tree.clearResult();
        }
    }

    private void fillCoastlines(GraphicsContext gc){
        gc.setLineWidth(1 / Math.sqrt(trans.determinant()));
        gc.setFill(ColorChanger.getColor(WayType.COASTLINE));
        coastlines = model.getCoastLines();
        coastlines.fill(gc);
    }

    private Boundable setScreen() {

        if (!rTreedebugMode) {
            try {
                Point2D temp = trans.inverseTransform(getWidth(), 0);
                topRight = new bfst22.vector.Data.Point2D(temp.getX(), temp.getY());
                temp = trans.inverseTransform(0, getHeight());
                bottomLeft = new bfst22.vector.Data.Point2D(temp.getX(), temp.getY());
            } catch (NonInvertibleTransformException e) {
                e.printStackTrace();
            }
        } else {
            try {
                Point2D temp = trans.inverseTransform(getWidth() * 0.66, getHeight() * 0.33);
                topRight = new bfst22.vector.Data.Point2D(temp.getX(), temp.getY());
                temp = trans.inverseTransform(getWidth() * 0.33, getHeight() * 0.66);
                bottomLeft = new bfst22.vector.Data.Point2D(temp.getX(), temp.getY());
            } catch (NonInvertibleTransformException e) {
                e.printStackTrace();
            }
        }
        return new Screen(topRight, bottomLeft);
    }

    private void placeIcons(GraphicsContext gc){
        if (!model.getIcons().isEmpty()) {
            for (Icon icon : model.getIcons()) {
                icon.placeIcon(gc);
            }
        }
        if (!model.getPointsOfInterest().isEmpty()) {
            for (Icon icon : model.getPointsOfInterest()) {
                icon.placeIcon(gc);
            }
        }
    }

    void pan(double dx, double dy) {
        trans.prependTranslation(dx, dy);
        model.setTrees(getZoomLevel());
    }

    void zoom(double factor, double x, double y) {
        trans.prependTranslation(-x, -y);
        trans.prependScale(factor, factor);
        trans.prependTranslation(x, y);
        model.setTrees(getZoomLevel());
    }

    void setRTreeDebugMode(boolean debugMode) {
        this.rTreedebugMode = debugMode;
        repaint();
    }

    void setKdTreeDebugMode(boolean debugMode) {
        this.kdTreeDebugMode = debugMode;
        repaint();
    }

    void setAStarDebugMode(boolean debugMode) {
        this.aStarDebugMode = debugMode;
        repaint();
    }

    void setVisitedEdgesMode(boolean debugMode) {
        visitedEdgesMode = debugMode;
        repaint();
    }

    public boolean getRTreeDebugMode() {
        return rTreedebugMode;
    }

    public boolean getKdTreeDebugMode() {
        return kdTreeDebugMode;
    }

    public boolean getAStarDebugMode() {
        return aStarDebugMode;
    }

    public boolean getVisitedEdgesMode() {
        return visitedEdgesMode;
    }

    public Point2D mouseToModel(Point2D point) {
        try {
            return trans.inverseTransform(point);
        } catch (NonInvertibleTransformException e) {
            throw new RuntimeException(e);
        }
    }

    public double getZoomLevel() {
        return Math.sqrt(trans.determinant());
    }

    public void setTheme(ThemeType theme) {
        this.theme = theme;
        repaint();
    }

    public void drawDijkstraPath() {
        var gc = getGraphicsContext2D();
        gc.setLineCap(StrokeLineCap.ROUND);
        ArrayList<Path<Drawable>> dijkstraPaths = model.getDijkstraPaths();
        if (dijkstraPaths.isEmpty())
            return;
        for (Path<Drawable> dijkstraPath : dijkstraPaths) {
            if (model.getTransportation().equals("car")) {
                if (dijkstraPath.getCar())
                    gc.setStroke(Color.RED);
                else if (!dijkstraPath.getCar())
                    gc.setStroke(Color.BLUE);
            } else if (model.getTransportation().equals("bike"))
                gc.setStroke(Color.GREEN);
            else
                gc.setStroke(Color.BLUE);
            for (var line : dijkstraPath) {
                line.draw(gc);
            }
        }
    }

    public void drawAStarPath() {
        var gc = getGraphicsContext2D();
        gc.setLineCap(StrokeLineCap.ROUND);
        ArrayList<Path<Drawable>> aStarPaths = model.getAStarPaths();
        if (aStarPaths.isEmpty())
            return;
        for (Path<Drawable> aStarPath : aStarPaths) {
            if (model.getTransportation().equals("car")) {
                if (aStarPath.getCar())
                    gc.setStroke(Color.RED);
                else if (!aStarPath.getCar())
                    gc.setStroke(Color.BLUE);
            } else if (model.getTransportation().equals("bike"))
                gc.setStroke(Color.GREEN);
            else
                gc.setStroke(Color.BLUE);
            for (var line : aStarPath) {
                line.draw(gc);
            }
        }
    }

    // Debug method for drawing all edges visited by A Star
    public void drawAllVisitedEdges() {
        var gc = getGraphicsContext2D();
        if (model.getAllVisitedEdges().isEmpty()) {
            return;
        }
        gc.setLineCap(StrokeLineCap.ROUND);
        gc.setStroke(Color.BLACK);
        for (var line : model.getAllVisitedEdges()) {
            line.draw(gc);
        }
    }

    public Boundable getScreen() {
        return screen;
    }

    public Affine getTrans() {
        return trans;
    }
}