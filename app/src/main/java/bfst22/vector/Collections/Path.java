package bfst22.vector.Collections;

import java.util.ArrayList;

public class Path<E> extends ArrayList<E> {
    boolean car = true;

    public void setCar(boolean car) {
        this.car = car;
    }

    public boolean getCar() {
        return car;
    }
}
