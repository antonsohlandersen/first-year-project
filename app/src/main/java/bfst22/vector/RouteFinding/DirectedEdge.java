package bfst22.vector.RouteFinding;

import bfst22.vector.Data.Point2D;

import java.io.Serializable;

/**
 * The {@code DirectedEdge} class represents a weighted edge in an
 * {@link EdgeWeightedDigraph}. Each edge consists of two integers
 * (naming the two vertices) and a real-value weight. The data type
 * provides methods for accessing the two endpoints of the directed edge and
 * the weight.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */

public class DirectedEdge implements Serializable {
    public static final long serialVersionUID = 9082422;
    private final int v;
    private final int w;
    private final double weight;
    private final boolean car;
    private final boolean bike;
    private final boolean walk;
    private final int maxSpeed;
    private final double length;
    private float[] coordinates;

    /**
     * Initializes a directed edge from vertex {@code v} to vertex {@code w} with
     * the given {@code weight}.
     * 
     * @param v      the tail vertex
     * @param w      the head vertex
     * @param coords
     * @param weight the weight of the directed edge
     * @throws IllegalArgumentException if either {@code v} or {@code w}
     *                                  is a negative integer
     * @throws IllegalArgumentException if {@code weight} is {@code NaN}
     */
    public DirectedEdge(int v, int w, float[] coordinates,
            double weight,
            int maxSpeed,
            double length,
            boolean car,
            boolean bike, boolean walk) {
        if (v < 0)
            throw new IllegalArgumentException("Vertex names must be non-negative integers");
        if (w < 0)
            throw new IllegalArgumentException("Vertex names must be non-negative integers");
        if (Double.isNaN(weight))
            throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.weight = weight;
        this.car = car;
        this.bike = bike;
        this.walk = walk;
        this.maxSpeed = maxSpeed;
        this.length = length;
        this.coordinates = coordinates;
    }

    /**
     * Returns the tail vertex of the directed edge.
     * 
     * @return the tail vertex of the directed edge
     */
    public int from() {
        return v;
    }

    /**
     * Returns the head vertex of the directed edge.
     * 
     * @return the head vertex of the directed edge
     */
    public int to() {
        return w;
    }

    public float getvLon() {
        return coordinates[0];
    }

    public float getvLat() {
        return coordinates[1];
    }

    public float getwLon() {
        return coordinates[coordinates.length - 2];
    }

    public float getwLat() {
        return coordinates[coordinates.length - 1];
    }

    public float[] getCoordinates() {
        return coordinates;
    }

    /**
     * Returns the weight of the directed edge.
     * 
     * @return the weight of the directed edge
     */
    public double weight() {
        return weight;
    }

    public double getCarWeight() {
        return weight + length / maxSpeed * 3600;
    }

    public double getBikeWeight() {
        return weight + length / 20 * 3600;
    }

    public double getWalkWeight() {
        return weight + length / 5 * 3600;
    }

    public Point2D getPoint2DFrom() {
        return new Point2D(coordinates[0], coordinates[1]);
    }

    public Point2D getPoint2DTo() {
        return new Point2D(coordinates[coordinates.length - 1], coordinates[coordinates.length - 2]);
    }

    public boolean getCar() {
        return car;
    }

    public boolean getBike() {
        return bike;
    }

    public boolean getWalk() {
        return walk;
    }

    public double getLength() {
        return length;
    }
}

/******************************************************************************
 * Copyright 2002-2020, Robert Sedgewick and Kevin Wayne.
 *
 * This file is part of algs4.jar, which accompanies the textbook
 *
 * Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 * Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 * http://algs4.cs.princeton.edu
 *
 *
 * algs4.jar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * algs4.jar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with algs4.jar. If not, see http://www.gnu.org/licenses.
 ******************************************************************************/