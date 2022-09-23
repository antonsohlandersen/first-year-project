package bfst22.vector.RouteFinding;

import bfst22.vector.Collections.IndexMinPQ;
import bfst22.vector.Collections.Stack;

import java.util.ArrayList;

/**
 * The {@code DijkstraSP} class represents a data type for solving the
 * single-source shortest paths problem in edge-weighted digraphs
 * where the edge weights are non-negative.
 * <p>
 * This implementation uses <em>Dijkstra's algorithm</em> with a
 * <em>binary heap</em>. The constructor takes
 * &Theta;(<em>E</em> log <em>V</em>) time in the worst case,
 * where <em>V</em> is the number of vertices and <em>E</em> is
 * the number of edges. Each instance method takes &Theta;(1) time.
 * It uses &Theta;(<em>V</em>) extra space (not including the
 * edge-weighted digraph).
 * <p>
 * This correctly computes shortest paths if all arithmetic performed is
 * without floating-point rounding error or arithmetic overflow.
 * This is the case if all edge weights are integers and if none of the
 * intermediate results exceeds 2<sup>52</sup>. Since all intermediate
 * results are sums of edge weights, they are bounded by <em>V C</em>,
 * where <em>V</em> is the number of vertices and <em>C</em> is the maximum
 * weight of any edge.
 * <p>
 * For additional documentation,
 * see <a href="https://algs4.cs.princeton.edu/44sp">Section 4.4</a> of
 * <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 * @author Robert Sedgewick
 * @author Kevin Wayne
 */
public class DijkstraSP {
    private double[] distTo; // distTo[v] = distance of shortest s->v path
    private DirectedEdge[] edgeTo; // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq; // priority queue of vertices
    private ArrayList<Stack<DirectedEdge>> paths; // List of paths from pathto
    private String transportation;
    private int source;
    private ArrayList<DirectedEdge> allVisitedEdges;

    /**
     * Computes a shortest-paths tree from the source vertex {@code s} to every
     * other
     * vertex in the edge-weighted digraph {@code G}.
     *
     * @param G              the edge-weighted digraph
     * @param s              the source vertex
     * @param target
     * @param transportation
     * @throws IllegalArgumentException if an edge weight is negative
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     */
    public DijkstraSP(EdgeWeightedDigraph G, int s, int target, String transportation, boolean addToVisitedEdges) {
        var time = -System.nanoTime();
        this.transportation = transportation;
        this.source = s;
        boolean foundTarget = false;
        paths = new ArrayList<>();
        if(addToVisitedEdges) {
            allVisitedEdges = new ArrayList<>();
        }
        for (DirectedEdge e : G.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }
        distTo = new double[G.V()];
        edgeTo = new DirectedEdge[G.V()];
        validateVertex(s);

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty() && !foundTarget) {
            int v = pq.delMin();
            for (DirectedEdge e : G.adj(v)) {
                relax(e);
                if(addToVisitedEdges) {
                    allVisitedEdges.add(e);
                }
                if (e.to() == target) {
                    foundTarget = true;
                }
            }
        }
        time += System.nanoTime();
        System.out.println("Load time: " + (long) (time / 1e6) + " ms");
    }

    // relax edge e and update pq if changed
    private void relax(DirectedEdge e) {
        int v = e.from(), w = e.to();
        double weight = 0;
        if (transportation.equals("car") && !e.getCar())
            weight = e.getWalkWeight();
        else if (transportation.equals("car") && e.getCar())
            weight = e.getCarWeight();
        else if (transportation.equals("bike"))
            weight = e.getBikeWeight();
        else if (transportation.equals("walk"))
            weight = e.getWalkWeight();
        if (distTo[w] > distTo[v] + weight) {
            distTo[w] = distTo[v] + weight;
            edgeTo[w] = e;
            if (pq.contains(w))
                pq.decreaseKey(w, distTo(w));
            else
                pq.insert(w, distTo(w));
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex {@code s} to
     * vertex {@code v}.
     * 
     * @param v the destination vertex
     * @return the length of a shortest path from the source vertex {@code s} to
     *         vertex {@code v};
     *         {@code Double.POSITIVE_INFINITY} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public double distTo(int v) {
        validateVertex(v);
        return distTo[v];
    }

    /**
     * Returns true if there is a path from the source vertex {@code s} to vertex
     * {@code v}.
     *
     * @param v the destination vertex
     * @return {@code true} if there is a path from the source vertex
     *         {@code s} to vertex {@code v}; {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean hasPathTo(int v) {
        validateVertex(v);
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex {@code s} to vertex {@code v}.
     *
     * @param v the destination vertex
     * @return a shortest path from the source vertex {@code s} to vertex {@code v}
     *         as an iterable of edges, and {@code null} if no such path
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public ArrayList<Stack<DirectedEdge>> pathTo(int v) {
        paths = new ArrayList<>();
        validateVertex(v);
        if (!hasPathTo(v))
            return null;
        Stack<DirectedEdge> path = new Stack<>();
        boolean pathStarted = false;
        boolean walkStarted = false;
        boolean potentialEndWalk = false;
        int counter = 0;
        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            if (!pathStarted && transportation.equals("car") && !e.getCar()) {
                walkStarted = true;
            } else if (!walkStarted && pathStarted && transportation.equals("car") && !e.getCar()) {
                potentialEndWalk = true;
                walkStarted = true;
            } else if (walkStarted && e.getCar()) {
                potentialEndWalk = false;
                counter = 0;
                paths.add(path);
                path = new Stack<>();
                walkStarted = false;
            }
            path.push(e);
            if (!pathStarted)
                pathStarted = true;
            if (potentialEndWalk)
                counter++;
        }
        Stack<DirectedEdge> endPath = new Stack<>();
        if (potentialEndWalk) {
            for (int i = 0; i < counter; i++) {
                endPath.push(path.pop());
            }
        }
        paths.add(path);
        if (potentialEndWalk)
            paths.add(endPath);
        new GPS(paths, transportation);
        return paths;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = distTo.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    public double[] getDistanceTo() {
        return distTo;
    }

    public int getSource() {
        return source;
    }

    public ArrayList<DirectedEdge> getAllVisitedEdges() {
        return allVisitedEdges;
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