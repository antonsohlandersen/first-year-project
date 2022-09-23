package bfst22.vector.Data;

import java.io.Serializable;

//NumLocation represents a housenumber, and it's location on a certain street
public class NumLocation implements Comparable<String>, Serializable {
    public static final long serialVersionUID = 9083567;

    float x;
    float y;
    String housenumber;

    public NumLocation(String housenumber, float x, float y){
        this.housenumber = housenumber.intern();
        this.x = x;
        this.y = y;
    }

    public float[] getLoc(){
        return new float[]{x, y};
    }

    public String getHousenumber(){
        return housenumber;
    }

    @Override
    public int compareTo(String o) {
        return housenumber.compareTo(o);
    }
}
