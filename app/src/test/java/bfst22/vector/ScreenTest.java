package bfst22.vector;

import static org.junit.jupiter.api.Assertions.assertEquals;

import bfst22.vector.Data.Point2D;
import bfst22.vector.Utilities.Screen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScreenTest {

    Screen screen;
    Point2D bottomLeft;
    Point2D topRight;

    @BeforeEach
    void setUp() {
        bottomLeft = new Point2D(0, 10);
        topRight = new Point2D(10, 0);
        screen = new Screen(topRight, bottomLeft);
    }

    @Test
    void getTopRight() {
        float[] exp = new float[] { 10, 0 };
        float[] act = screen.getTopRight();
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);
    }

    @Test
    void getBottomLeft() {
        float[] exp = new float[] { 0, 10 };
        float[] act = screen.getBottomLeft();
        assertEquals(exp[0], act[0]);
        assertEquals(exp[1], act[1]);
    }
}
