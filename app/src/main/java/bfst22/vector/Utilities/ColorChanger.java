package bfst22.vector.Utilities;

import bfst22.vector.Enum.ThemeType;
import bfst22.vector.Enum.WayType;
import bfst22.vector.exceptions.WayTypeColorNotFoundException;
import javafx.scene.paint.Color;

public class ColorChanger {
    static Color backGround;
    static Color ways;
    static Color water;
    static Color coastline_industrial;
    static Color grass;
    static Color pitch;
    static Color meadow_scrub;
    static Color park;
    static Color cemetery;
    static Color forest;
    static Color residential;
    static Color beach;
    static Color wetland;
    static Color building;
    static Color motorway;
    static Color trunk;
    static Color primary;
    static Color secondary;
    static ThemeType theme;

    public static void setNormalTheme(){
        theme = ThemeType.NORMAL;
        backGround = Color.rgb(170,211,223);
        ways = Color.WHITE;
        water = Color.rgb(170,211,223);
        coastline_industrial = Color.rgb(242, 239, 233);
        grass = Color.rgb(205,235,176);
        pitch = Color.rgb(170, 224, 203);
        meadow_scrub = Color.rgb(200,215,171);
        park = Color.rgb(200, 250, 204);
        cemetery = Color.rgb(170, 203, 175);
        forest = Color.rgb(173, 209, 158);
        residential = Color.rgb(224, 223, 223);
        beach = Color.rgb(255, 241, 186);
        wetland = Color.rgb(214, 217, 159);
        building = Color.DARKGRAY;
        motorway = Color.rgb(233,144,160);
        trunk = Color.rgb(251,192,172);
        primary = Color.rgb(253,215,161);
        secondary = Color.rgb(246,250,187);
    }

    public static Color getColor(WayType colortype) {
        switch(colortype) {
            case BACKGROUND:{return backGround;}
            case WATER: {return water;}
            case RIVER: {return water;}
            case GRASS: {return grass;}
            case WETLAND: {return wetland;}
            case GRASSLAND: {return grass;}
            case WOOD: {return forest;}
            case BEACH: {return beach;}
            case FOREST: {return forest;}
            case MEADOW: {return meadow_scrub;}
            case RESIDENTIAL: {return residential;}
            case INDUSTRIAL: {return coastline_industrial;}
            case COASTLINE: {return coastline_industrial;}
            case CEMETERY: {return cemetery;}
            case RECREATION_GROUND: {return grass;}
            case BUILDING: {return building;}
            case PARK: {return park;}
            case GOLF: {return grass;}
            case PITCH: {return pitch;}
            case MOTORWAY: {return motorway;}
            case TRUNK: {return trunk;}
            case PRIMARY: {return primary;}
            case SECONDARY: {return secondary;}
            case TERTIARY: {return ways;}
            case UNCLASSIFIED_HIGHWAY: {return ways;}
            case RESIDENTIAL_HIGHWAY: {return ways;}
            case BRIDLEWAY: {return ways;}
            case BUS_GUIDEWAY: {return ways;}
            case FOOTWAY: {return ways;}
            case CYCLEWAY: {return ways;}
            case LIVING_STREET: {return ways;}
            case TRACK: {return ways;}
            case SERVICE: {return ways;}
            case PATH: {return ways;}
        }
        throw new WayTypeColorNotFoundException(colortype);
    }

    public static void setColor(WayType colortype, Color newColor) {
        switch(colortype) {
            case BACKGROUND: {backGround = newColor; return;}
            case WATER: {water = newColor; return;}
            case RIVER: {water = newColor; return;}
            case GRASS: {grass = newColor; return;}
            case WETLAND: {wetland = newColor; return;}
            case GRASSLAND: {grass = newColor; return;}
            case WOOD: {forest = newColor; return;}
            case BEACH: {beach = newColor; return;}
            case FOREST: {forest = newColor;} return;
            case MEADOW: {meadow_scrub = newColor; return;}
            case RESIDENTIAL: {residential = newColor; return;}
            case INDUSTRIAL: {coastline_industrial = newColor; return;}
            case COASTLINE: {coastline_industrial = newColor; return;}
            case CEMETERY: {cemetery = newColor; return;}
            case RECREATION_GROUND: {grass = newColor; return;}
            case BUILDING: {building = newColor; return;}
            case PARK: {park = newColor; return;}
            case GOLF: {grass = newColor; return;}
            case PITCH: {pitch = newColor; return;}
            case MOTORWAY: {motorway = newColor; return;}
            case TRUNK: {trunk = newColor; return;}
            case PRIMARY: {primary = newColor; return;}
            case SECONDARY: {secondary = newColor; return;}
            case TERTIARY: {ways = newColor; return;}
            case UNCLASSIFIED_HIGHWAY: {ways = newColor; return;}
            case RESIDENTIAL_HIGHWAY: {ways = newColor; return;}
            case BRIDLEWAY: {ways = newColor; return;}
            case BUS_GUIDEWAY: {ways = newColor; return;}
            case FOOTWAY: {ways = newColor; return;}
            case CYCLEWAY: {ways = newColor; return;}
            case LIVING_STREET: {ways = newColor; return;}
            case TRACK: {ways = newColor; return;}
            case SERVICE: {ways = newColor; return;}
            case PATH: {ways = newColor; return;}
        }
        throw new WayTypeColorNotFoundException(colortype);
    }
    public static void setNoirTheme(){
        theme = ThemeType.NOIR;
        backGround = backGround.grayscale();
        ways = ways.grayscale();
        water = water.grayscale();
        coastline_industrial = coastline_industrial.grayscale();
        grass = grass.grayscale();
        pitch = pitch.grayscale();
        meadow_scrub = meadow_scrub.grayscale();
        park = park.grayscale();
        cemetery = cemetery.grayscale();
        forest = forest.grayscale();
        residential = residential.grayscale();
        beach = beach.grayscale();
        wetland = wetland.grayscale();
        building = building.grayscale();
        motorway = motorway.grayscale();
        trunk = trunk.grayscale();
        primary = primary.grayscale();
        secondary = secondary.grayscale();
    }

    public static void setTheme(ThemeType themetype){
        if (theme == themetype){
            if (theme != ThemeType.RANDOM){
                return;
            }
        }
        switch (themetype){
            case NORMAL:
                setNormalTheme();
                break;
            case INVERT:
                setInvertTheme();
                break;

            case NOIR:
                setNoirTheme();
                break;
            case RANDOM:
                setRandomTheme();
                break;
        }
    }

    public static void setInvertTheme(){
        theme = ThemeType.INVERT;
        backGround = backGround.invert();
        ways = ways.invert();
        water = water.invert();
        coastline_industrial = coastline_industrial.invert();
        grass = grass.invert();
        pitch = pitch.invert();
        meadow_scrub = meadow_scrub.invert();
        park = park.invert();
        cemetery = cemetery.invert();
        forest = forest.invert();
        residential = residential.invert();
        beach = beach.invert();
        wetland = wetland.invert();
        building = building.invert();
        motorway = motorway.invert();
        trunk = trunk.invert();
        primary = primary.invert();
        secondary = secondary.invert();
    }

    public static void setRandomTheme(){
        theme = ThemeType.RANDOM;
        backGround = Color.color(Math.random(), Math.random(), Math.random());
        ways = Color.color(Math.random(), Math.random(), Math.random());
        water = Color.color(Math.random(), Math.random(), Math.random());
        coastline_industrial = Color.color(Math.random(), Math.random(), Math.random());
        grass = Color.color(Math.random(), Math.random(), Math.random());
        pitch = Color.color(Math.random(), Math.random(), Math.random());
        meadow_scrub = Color.color(Math.random(), Math.random(), Math.random());
        park = Color.color(Math.random(), Math.random(), Math.random());
        cemetery = Color.color(Math.random(), Math.random(), Math.random());
        forest = Color.color(Math.random(), Math.random(), Math.random());
        residential = Color.color(Math.random(), Math.random(), Math.random());
        beach = Color.color(Math.random(), Math.random(), Math.random());
        wetland = Color.color(Math.random(), Math.random(), Math.random());
        building = Color.color(Math.random(), Math.random(), Math.random());
        motorway = Color.color(Math.random(), Math.random(), Math.random());
        trunk = Color.color(Math.random(), Math.random(), Math.random());
        primary = Color.color(Math.random(), Math.random(), Math.random());
        secondary = Color.color(Math.random(), Math.random(), Math.random());
    }
}
