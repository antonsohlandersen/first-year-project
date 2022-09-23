package bfst22.vector.Data.SpatialDataStructures.KDTree;

import bfst22.vector.Drawables.Line;
import bfst22.vector.Data.OSMData.OSMNode;
import bfst22.vector.Data.Point2D;
import bfst22.vector.Collections.PartitionList;
import bfst22.vector.Collections.Stack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//The KdTree stores all intersections between highways on the map.
public class KdTree implements Serializable {
    public static final long serialVersionUID = 9082424;
    private KdNode root;
    private int maxAmountOfOSMNodesInLeaf;
    private float tempShortestDistance;

    List<Line> linesForDebugMode;

    public KdTree(int maxAmountOfOSMNodesInLeaf, PartitionList<OSMNode> data, boolean splitOnX) {
        this.maxAmountOfOSMNodesInLeaf = maxAmountOfOSMNodesInLeaf;
        this.tempShortestDistance = 0;
        root = constructNodes(data, splitOnX);
    }

    //constructNodes is a recursive function that finds all the trees nodes and then returns the root node with its two children
    public KdNode constructNodes(PartitionList<OSMNode> data, boolean splitOnX) {
        if (splitOnX) {
            Collections.sort(data, OSMNode.ByX);
        } else {
            Collections.sort(data, OSMNode.ByY);
        }
        PartitionList<PartitionList<OSMNode>> partitions = data.partitionToPartionLists((int) (data.size() * 0.5));
        OSMNode middlePoint = partitions.get(1).get(0);
        KdNode nodeToBeReturned;
        if (data.size()/2 > maxAmountOfOSMNodesInLeaf) {
            KdNode leftOrTopChild = constructNodes(partitions.get(0), !splitOnX);
            KdNode bottomOrRightChild = constructNodes(partitions.get(1), !splitOnX);
            nodeToBeReturned = new KdParent(splitOnX, middlePoint, leftOrTopChild, bottomOrRightChild);
        } else {
            nodeToBeReturned = new KdLeaf(splitOnX, middlePoint, partitions.get(0), partitions.get(1));
        }
        return nodeToBeReturned;
    }

    //We find the OSMNode that is closest to the point chosen by the user.
    public OSMNode findResult(Point2D point) {
        KdNode currentNode = root;
        Stack<KdNode> visitedNodesStack = new Stack<>();
        Stack<Boolean> cameFromTopOrLeftBooleanStack = new Stack<>();
        //We find the closest point in the leaf in which the user-chosen point would lie if it was an intersection.
        while (!currentNode.hasData()) {
            visitedNodesStack.push(currentNode);
            if (currentNode.getSplitOnX()) {
                if (currentNode.getSplitCord() < (float) point.getX()) {
                    cameFromTopOrLeftBooleanStack.push(false);
                    currentNode = currentNode.getBottomOrRightChild();
                } else {
                    cameFromTopOrLeftBooleanStack.push(true);
                    currentNode = currentNode.getTopOrLeftChild();
                }
            } else {
                if (-currentNode.getSplitCord() < (float) -point.getY()) {
                    cameFromTopOrLeftBooleanStack.push(true);
                    currentNode = currentNode.getTopOrLeftChild();
                } else {
                    cameFromTopOrLeftBooleanStack.push(false);
                    currentNode = currentNode.getBottomOrRightChild();
                }
            }
        }
        OSMNode currentClosestPoint = findSmallestDistanceInLeaf(currentNode, point);
        float currentShortestDistance = tempShortestDistance;

        //We check all nodes that we traversed through to get to the leaf, and check if they could contain an OSMNode that is closer to the point than the one already found.
        while (!visitedNodesStack.isEmpty()) {
            KdNode nextNode = visitedNodesStack.pop();
            boolean cameFromTopOrLeft = cameFromTopOrLeftBooleanStack.pop();
            if (nextNode.getSplitOnX()) {
                float distanceToSplitLine = (float) Math.abs(point.getX() - nextNode.getSplitCord());
                if (currentShortestDistance > distanceToSplitLine) {
                    boolean pointIsRightOfThisNode = false;
                    if (point.getX() > nextNode.getSplitCord()) {
                        pointIsRightOfThisNode = true;
                    }
                    if (cameFromTopOrLeft) {
                        nextNode = nextNode.getBottomOrRightChild();
                    } else {
                        nextNode = nextNode.getTopOrLeftChild();
                    }
                    while (!nextNode.hasData()) {
                        if (nextNode.getSplitOnX()) {
                            if (pointIsRightOfThisNode) {
                                nextNode = nextNode.getBottomOrRightChild();
                            } else {
                                nextNode = nextNode.getTopOrLeftChild();
                            }
                        } else {
                            if (-point.getY() < -nextNode.getSplitCord()) {
                                nextNode = nextNode.getBottomOrRightChild();
                            } else {
                                nextNode = nextNode.getTopOrLeftChild();
                            }
                        }
                    }
                    OSMNode tempNode = findSmallestDistanceInLeaf(nextNode, point);
                    if (tempShortestDistance < currentShortestDistance) {
                        currentShortestDistance = tempShortestDistance;
                        currentClosestPoint = tempNode;
                    }
                }
            } else {
                float distanceToSplitLine = (float) Math.abs(point.getY() - nextNode.getSplitCord());
                if (currentShortestDistance > distanceToSplitLine) {
                    boolean pointIsUnderThisNode = false;
                    if (-point.getY() < -nextNode.getSplitCord()) {
                        pointIsUnderThisNode = true;
                    }
                    if (cameFromTopOrLeft) {
                        nextNode = nextNode.getBottomOrRightChild();
                    } else {
                        nextNode = nextNode.getTopOrLeftChild();
                    }
                    while (!nextNode.hasData()) {
                        if (nextNode.getSplitOnX()) {
                            if (point.getX() > nextNode.getSplitCord()) {
                                nextNode = nextNode.getBottomOrRightChild();
                            } else {
                                nextNode = nextNode.getTopOrLeftChild();
                            }
                        } else {
                            if (pointIsUnderThisNode) {
                                nextNode = nextNode.getTopOrLeftChild();
                            } else {
                                nextNode = nextNode.getBottomOrRightChild();
                            }
                        }
                    }
                    OSMNode tempNode = findSmallestDistanceInLeaf(nextNode, point);
                    if (tempShortestDistance < currentShortestDistance) {
                        currentShortestDistance = tempShortestDistance;
                        currentClosestPoint = tempNode;
                    }
                }
            }
        }
        return currentClosestPoint;
    }

    //Finds the smallest distance from a specific point to all the points in a leafs data-list
    public OSMNode findSmallestDistanceInLeaf(KdNode currentNode, Point2D point) {
        float pointX = (float) point.getX();
        float pointY = (float) point.getY();
        OSMNode closestPoint = null;
        float shortestDistance = Float.MAX_VALUE;
        boolean checkedTopOrLeft = false;

        List<OSMNode> currentData = null;

        float smallestDistanceToSplitLine = 0;
        if(currentNode.getSplitOnX()){
            smallestDistanceToSplitLine = (float) Math.abs(pointX - currentNode.getSplitCord());
            if (currentNode.getSplitCord() < (float) point.getX()) {
                currentData = currentNode.getBottomOrRightData();
                checkedTopOrLeft = false;
            } else {
                currentData = currentNode.getTopOrLeftData();
                checkedTopOrLeft = true;
            }
        } else {
            smallestDistanceToSplitLine = (float) Math.abs(pointY - currentNode.getSplitCord());
            if (-currentNode.getSplitCord() < (float) -point.getY()) {
                currentData = currentNode.getTopOrLeftData();
                checkedTopOrLeft = true;
            } else {
                currentData = currentNode.getBottomOrRightData();
                checkedTopOrLeft = false;
            }
        }

        for (OSMNode node : currentData) {
            float currentDistance = (float) Math.sqrt(Math.pow(Math.abs(pointX - node.getLon()), 2) + Math.pow(Math.abs(pointY - node.getLat()), 2));
            if (currentDistance < shortestDistance) {
                shortestDistance = currentDistance;
                closestPoint = node;
            }
        }

        if(smallestDistanceToSplitLine < shortestDistance){
            if(checkedTopOrLeft){
                currentData = currentNode.getBottomOrRightData();
            } else {
                currentData = currentNode.getTopOrLeftData();
            }
            for (OSMNode node : currentData) {
                float currentDistance = (float) Math.sqrt(Math.pow(Math.abs(pointX - node.getLon()), 2) + Math.pow(Math.abs(pointY - node.getLat()), 2));
                if (currentDistance < shortestDistance) {
                    shortestDistance = currentDistance;
                    closestPoint = node;
                }
            }
        }

        tempShortestDistance = shortestDistance;
        return closestPoint;
    }

    public long getClosestPointId(Point2D point) {
        return findResult(point).getId();
    }

    //This method is exclusively used to find the lines used to draw the debug-mode for the tree.
    public void findLines(KdNode currentNode, float minX, float minY, float maxX, float maxY) {
        if (currentNode.getSplitOnX()) {
            float currentSplitCord = currentNode.getSplitCord();
            linesForDebugMode.add(new Line(new Point2D(currentSplitCord, minY), new Point2D(currentSplitCord, maxY)));
            if (!currentNode.hasData()) {
                findLines(currentNode.getBottomOrRightChild(), currentSplitCord, minY, maxX, maxY);
                findLines(currentNode.getTopOrLeftChild(), minX, minY, currentSplitCord, maxY);
            }
        } else {
            float currentSplitCord = currentNode.getSplitCord();
            linesForDebugMode.add(new Line(new Point2D(minX, currentSplitCord), new Point2D(maxX, currentSplitCord)));
            if (!currentNode.hasData()) {
                findLines(currentNode.getBottomOrRightChild(), minX, currentSplitCord, maxX, maxY);
                findLines(currentNode.getTopOrLeftChild(), minX, minY, maxX, currentSplitCord);
            }
        }
    }

    //In this method we create our box for the lines in our debug mode to be drawn in.
    public List<Line> getLines() {
        if (linesForDebugMode == null) {
            linesForDebugMode = new ArrayList<>();
            linesForDebugMode.add(new Line(new Point2D(4.2403, -58.0085), new Point2D(8.7465, -58.0085)));
            linesForDebugMode.add(new Line(new Point2D(8.7465, -58.0085), new Point2D(8.7465, -54.3769)));
            linesForDebugMode.add(new Line(new Point2D(8.7465, -54.3769), new Point2D(4.2403, -54.3769)));
            linesForDebugMode.add(new Line(new Point2D(4.2403, -54.3769), new Point2D(4.2403, -58.0085)));
            float minX = (float)linesForDebugMode.get(0).getFrom().getX();
            float minY = (float)linesForDebugMode.get(1).getFrom().getY();
            float maxX = (float)linesForDebugMode.get(2).getFrom().getX();
            float maxY = (float)linesForDebugMode.get(3).getFrom().getY();
            findLines(root, minX, minY, maxX, maxY);
        }
        return linesForDebugMode;
    }
}
