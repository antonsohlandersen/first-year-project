package bfst22.vector.RouteFinding;

import bfst22.vector.Collections.Stack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

public class GPS {

    public GPS(ArrayList<Stack<DirectedEdge>> listOfPaths, String transportation) {
        deleteFileContent();
        double travelTime = 0;
        for (Stack<DirectedEdge> path : listOfPaths) {
            Iterator<DirectedEdge> iterator = path.iterator();
            String previousDirection = null;
            String currentDirection = null;
            String turn = null;
            String lastTurn = null;
            double roadLength = 0;
            DirectedEdge currentEdge;
            DirectedEdge previousEdge = null;
            double angleBetweenEdges = 0.0f;
            while (iterator.hasNext()) {
                currentEdge = iterator.next();
                if (transportation.equals("car"))
                    travelTime += currentEdge.getCarWeight();
                if (transportation.equals("bike"))
                    travelTime += currentEdge.getBikeWeight();
                if (transportation.equals("walk"))
                    travelTime += currentEdge.getWalkWeight();
                currentDirection = computeDirection(currentEdge.getvLon(), currentEdge.getvLat(), currentEdge.getwLon(),
                        currentEdge.getwLat(), previousDirection);
                roadLength += currentEdge.getLength();
                if (previousEdge != null) {
                    angleBetweenEdges = computeAngle(previousEdge.getvLon(), previousEdge.getvLat(),
                            currentEdge.getvLon(), currentEdge.getvLat(), currentEdge.getwLon(), currentEdge.getwLat());
                }
                turn = checkDirection(previousDirection, currentDirection, angleBetweenEdges, previousEdge,
                        currentEdge);
                // Prints route to file only when turning
                if (!turn.equals("straight")) {
                    if (lastTurn != null) {
                        writeToFile("Go " + lastTurn + " and keep going for "
                                + new DecimalFormat("#.##").format(roadLength) + "km");
                        roadLength = 0;
                    }
                    lastTurn = turn;
                }
                if (!iterator.hasNext()) {
                    if (roadLength == 0) {
                        roadLength = currentEdge.getLength();
                    }

                    writeToFile("Go " + lastTurn + " and keep going for " + new DecimalFormat("#.##").format(roadLength)
                            + "km");

                }
                previousDirection = currentDirection;
                previousEdge = currentEdge;
            }
        }
        writeToFile("Estimated travel time: " + travelTime);
        writeToFile("Arrived at your destination.");
    }

    // Returns a direction based upon the direction of the edge
    private String computeDirection(float vLon, float vLat, float wLon, float wLat, String previousDirection) {
        if ((Math.abs(wLat - vLat)) > (Math.abs(wLon - vLon)) && Math.abs(wLat) > Math.abs(vLat))
            return "north";
        else if ((Math.abs(wLat - vLat)) > (Math.abs(wLon - vLon)) && Math.abs(wLat) < Math.abs(vLat))
            return "south";
        else if ((Math.abs(wLon - vLon)) > (Math.abs(wLat - vLat)) && Math.abs(wLon) > Math.abs(vLon))
            return "east";
        else if ((Math.abs(wLon - vLon)) > (Math.abs(wLat - vLat)) && Math.abs(wLon) < Math.abs(vLon))
            return "west";
        return previousDirection;
    }

    // Returns relative direction dependent on difference between previous and
    // current direction
    private String checkDirection(String previousDirection, String currentDirection, double angle,
            DirectedEdge previousEdge, DirectedEdge currentEdge) {
        if (previousDirection == null)
            return currentDirection;
        switch (previousDirection) {
            case "north": {
                if (currentDirection.equals("north") || angle > 140)
                    return "straight";
                else if (currentDirection.equals("east"))
                    return "right";
                else if (currentDirection.equals("west"))
                    return "left";
                else if (currentDirection.equals("south")) {
                    if (angle < 30) {
                        return "U-turn";
                    } else if (Math.abs(currentEdge.getwLon()) > Math.abs(currentEdge.getvLon())) {
                        return "right";
                    } else if (Math.abs(currentEdge.getwLon()) <= Math.abs(currentEdge.getvLon())) {
                        return "left";
                    }
                }
                break;
            }
            case "east": {
                if (currentDirection.equals("east") || angle > 140)
                    return "straight";
                else if (currentDirection.equals("north"))
                    return "left";
                else if (currentDirection.equals("south"))
                    return "right";
                else if (currentDirection.equals("west")) {
                    if (angle < 30) {
                        return "U-turn";
                    } else if (Math.abs(currentEdge.getwLat()) < Math.abs(currentEdge.getvLat())) {
                        return "right";
                    } else if (Math.abs(currentEdge.getwLat()) >= Math.abs(currentEdge.getvLat())) {
                        return "left";
                    }
                }
                break;
            }
            case "west": {
                if (currentDirection.equals("west") || angle > 140)
                    return "straight";
                else if (currentDirection.equals("north"))
                    return "right";
                else if (currentDirection.equals("south"))
                    return "left";
                else if (currentDirection.equals("east")) {
                    if (angle < 30) {
                        return "U-turn";
                    } else if (Math.abs(currentEdge.getwLat()) < Math.abs(currentEdge.getvLat())) {
                        return "right";
                    } else if (Math.abs(currentEdge.getwLat()) >= Math.abs(currentEdge.getvLat())) {
                        return "left";
                    }
                }
                break;
            }
            case "south": {
                if (currentDirection.equals("south") || angle > 140)
                    return "straight";
                else if (currentDirection.equals("east"))
                    return "left";
                else if (currentDirection.equals("west"))
                    return "right";
                else if (currentDirection.equals("north")) {
                    if (angle < 30) {
                        return "U-turn";
                    } else if (Math.abs(currentEdge.getwLon()) < Math.abs(currentEdge.getvLon())) {
                        return "right";
                    } else if (Math.abs(currentEdge.getwLon()) >= Math.abs(currentEdge.getvLon())) {
                        return "left";
                    }
                }
                break;
            }
        }
        return previousDirection;
    }

    // Computes angle between previous edge and current edge
    private double computeAngle(float P1_x, float P1_y, float P2_x, float P2_y, float P3_x, float P3_y) {
        double a = Math.sqrt(Math.pow((P1_x - P2_x), 2) + Math.pow((P1_y - P2_y), 2));
        double b = Math.sqrt(Math.pow((P2_x - P3_x), 2) + Math.pow((P2_y - P3_y), 2));
        double c = Math.sqrt(Math.pow((P1_x - P3_x), 2) + Math.pow((P1_y - P3_y), 2));
        double result = Math.acos((Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b)) * (180 / Math.PI);
        return result;
    }

    private void writeToFile(String id) {
        try {
            var myWriter = new BufferedWriter(new FileWriter("data/directions.txt", true));
            myWriter.write(id + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void deleteFileContent() {
        try {
            File file = new File("data/directions.txt");
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
