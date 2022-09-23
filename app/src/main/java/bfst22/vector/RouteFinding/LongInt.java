package bfst22.vector.RouteFinding;

import java.io.Serializable;

public class LongInt implements Comparable<Long>, Serializable {

    public static final long serialVersionUID = 9083568;
    long key;
    int value;

    public LongInt(long key, int value){

        this.key = key;
        this.value = value;

    }

    public long getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Long o) {
        if (key<o){
            return -1;
        }
        if (key>o){
            return 1;
        }
        return 0;
    }
}
