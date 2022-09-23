package bfst22.vector;

import bfst22.vector.Enum.WayType;
import bfst22.vector.Utilities.ColorChanger;
import org.junit.jupiter.api.Test;

import bfst22.vector.exceptions.WayTypeColorNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import javafx.scene.paint.Color;
import java.util.Random;

public class ColorChangerTest{

    @Test
    public void getColorTest(){
        ColorChanger.setNormalTheme();
        assertEquals(Color.rgb(170,211,223), ColorChanger.getColor(WayType.WATER));
        assertEquals(Color.rgb(205,235,176), ColorChanger.getColor(WayType.GRASS));
        assertEquals(Color.rgb(214, 217, 159), ColorChanger.getColor(WayType.WETLAND));
        assertEquals(Color.rgb(205,235,176), ColorChanger.getColor(WayType.GRASSLAND));
        assertEquals(Color.rgb(173, 209, 158), ColorChanger.getColor(WayType.WOOD));
        assertEquals(Color.rgb(255, 241, 186), ColorChanger.getColor(WayType.BEACH));
        assertEquals(Color.rgb(173, 209, 158), ColorChanger.getColor(WayType.FOREST));
        assertEquals(Color.rgb(200,215,171), ColorChanger.getColor(WayType.MEADOW));
        assertEquals(Color.rgb(224, 223, 223), ColorChanger.getColor(WayType.RESIDENTIAL));
        assertEquals(Color.rgb(242, 239, 233), ColorChanger.getColor(WayType.INDUSTRIAL));
        assertEquals(Color.rgb(242,239,233), ColorChanger.getColor(WayType.COASTLINE));
        assertEquals(Color.rgb(170, 203, 175), ColorChanger.getColor(WayType.CEMETERY));
        assertEquals(Color.rgb(205,235,176), ColorChanger.getColor(WayType.RECREATION_GROUND));
        assertEquals(Color.DARKGRAY, ColorChanger.getColor(WayType.BUILDING));
        assertEquals(Color.rgb(200, 250, 204), ColorChanger.getColor(WayType.PARK));
        assertEquals(Color.rgb(205, 235, 176), ColorChanger.getColor(WayType.GOLF));
        assertEquals(Color.rgb(170, 224, 203), ColorChanger.getColor(WayType.PITCH));
        assertEquals(Color.rgb(233,144,160), ColorChanger.getColor(WayType.MOTORWAY));
        assertEquals(Color.rgb(251,192,172), ColorChanger.getColor(WayType.TRUNK));
        assertEquals(Color.rgb(253,215,161), ColorChanger.getColor(WayType.PRIMARY));
        assertEquals(Color.WHITE, ColorChanger.getColor(WayType.TERTIARY));
        assertEquals(Color.WHITE, ColorChanger.getColor(WayType.RESIDENTIAL_HIGHWAY));
        assertEquals(Color.WHITE, ColorChanger.getColor(WayType.UNCLASSIFIED_HIGHWAY));
        assertEquals(Color.WHITE, ColorChanger.getColor(WayType.SERVICE));
        assertEquals(Color.WHITE, ColorChanger.getColor(WayType.BRIDLEWAY));
        assertEquals(Color.WHITE, ColorChanger.getColor(WayType.BUS_GUIDEWAY));
        assertEquals(Color.WHITE, ColorChanger.getColor(WayType.FOOTWAY));
        assertEquals(Color.WHITE, ColorChanger.getColor(WayType.CYCLEWAY));
        assertEquals(Color.WHITE, ColorChanger.getColor(WayType.LIVING_STREET));
        assertEquals(Color.WHITE, ColorChanger.getColor(WayType.TRACK));
        assertThrows(WayTypeColorNotFoundException.class, () -> {
            ColorChanger.getColor(WayType.UNKNOWN);
        });
    }

    @Test
    public void setColorTest(){
        ColorChanger.setNormalTheme();
        Random rand = new Random();
        for(WayType type : WayType.values()){
            if(type != WayType.UNKNOWN && type != WayType.FARMLAND && type != WayType.HIGHWAY && type != WayType.ISLAND && type != WayType.PEDESTRIAN && type != WayType.SCRUB && type != WayType.STEPS){
                int r = rand.nextInt(256);
                int g = rand.nextInt(256);
                int b = rand.nextInt(256);
                ColorChanger.setColor(type, Color.rgb(r, g, b));
                assertEquals(Color.rgb(r, g, b), ColorChanger.getColor(type), "Failed at type: " + type);
            } else {
                int r = rand.nextInt(256);
                int g = rand.nextInt(256);
                int b = rand.nextInt(256);
                assertThrows(WayTypeColorNotFoundException.class, () -> {
                    ColorChanger.setColor(type, Color.rgb(r, g, b));
                });
                assertThrows(WayTypeColorNotFoundException.class, () -> {
                    ColorChanger.getColor(type);
                });
            }
        }
    }
}