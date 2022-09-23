package bfst22.vector.Collections;

import bfst22.vector.Data.NumLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class AddressMap extends ArrayList<NumLocation> {
    boolean sorted;

    public boolean add(NumLocation numLoc) {
        sorted = false;
        return super.add(numLoc);
    }

    //find location using housenumber as key
    public float[] get(String housenumber) {
        if (!sorted) {
            sort(Comparator.comparing(numLoc -> numLoc.getHousenumber()));
            sorted = true;
        }
        int lo = Collections.binarySearch(this, housenumber);
        if (lo<0){
            return null;
        }
        var node = get(lo);
        return node.getHousenumber().equals(housenumber) ? node.getLoc() : null;
    }
}

