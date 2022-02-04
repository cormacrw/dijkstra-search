package com.dijkstra.models;

import java.util.ArrayList;

public class Cave {
    private int x,y, caveNumber;
    private ArrayList<Cave> cavePaths;
    private boolean visited;
    private double distance;

    public Cave(int x, int y, int caveNumber) {
        this.x = x;
        this.y = y;
        this.caveNumber = caveNumber;
        this.cavePaths = new ArrayList<>();
        visited = false;
        distance = Double.POSITIVE_INFINITY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getCaveNumber() {
        return caveNumber;
    }

    public boolean hasBeenVisited() {
        return visited;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<Cave> getCavePaths() {
        return cavePaths;
    }

    public void addCave(Cave cave) {
        cavePaths.add(cave);
    }

    public String toString() {
        String str = "Cave " + caveNumber + " at position (" + x + "," + y + ") connects to caves ";
        for (Cave cavePath : cavePaths) {
            str += cavePath.getCaveNumber() + ",";
        }

        str += "and has a distance from the root of " + distance;

        return str;
    }

    public static double getDistanceBetweenTwoCaves(Cave cave1, Cave cave2) {
        return Math.sqrt( Math.pow(cave1.getX() - cave2.getX(), 2) + Math.pow(cave1.getY() - cave2.getY(), 2) ) ;
    }
}
