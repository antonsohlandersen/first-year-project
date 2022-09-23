package bfst22.vector.exceptions;

import bfst22.vector.Enum.WayType;

//Exception for when an attempt is made to get the color of a WayType that has no color associated with it
public class WayTypeColorNotFoundException extends RuntimeException {
    
    public WayTypeColorNotFoundException(WayType type){
        super("The waytype: " + type + " does not have an associated color");
    }
}
