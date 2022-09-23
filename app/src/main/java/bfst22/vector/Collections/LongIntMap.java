package bfst22.vector.Collections;

import bfst22.vector.RouteFinding.LongInt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LongIntMap extends ArrayList<LongInt> {

    boolean sorted;

    public boolean add(LongInt longInt) {
        sorted = false;
        return super.add(longInt);
    }

    //find location using housenumber as key
    public Integer get(long key) {
        if (!sorted) {
            sort(Comparator.comparing(longInt -> longInt.getKey()));
            sorted = true;
        }
        int lo = Collections.binarySearch(this, key);
        if (lo<0){
            return null;
        }
        var node = get(lo);
        return node.getKey() == key ? node.getValue() : null;
    }

    public boolean containsKey(long key){
        return get(key) != null;
    }

}
