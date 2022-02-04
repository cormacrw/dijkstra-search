package com.dijkstra;

import com.dijkstra.models.Cave;

import java.util.*;

public class PathFinder {


    private Map<Integer, Cave> caves;
    private Map<Integer, Cave> settledCaves;
    private Map<Integer, Cave> unsettledCaves;
    private Map<Cave, Cave> predecessors;

    public PathFinder(Map<Integer, Cave> caves) {
        this.caves = caves;
        settledCaves = new HashMap<>();
        unsettledCaves = new HashMap<>();
        predecessors = new HashMap<>();
    }

    public void execute( Cave rootCave ) {
        rootCave.setDistance(0);
        unsettledCaves.put(rootCave.getCaveNumber(), rootCave);
        while(!unsettledCaves.isEmpty()) {
            Cave currentCave = getCaveWithLowestDistance(unsettledCaves);
            settledCaves.put(currentCave.getCaveNumber(), currentCave);
            unsettledCaves.remove(currentCave.getCaveNumber());
            evaluateNeighbours(currentCave);
        }

        int a = 2;
    }

    private void evaluateNeighbours(Cave currentCave) {
        ArrayList<Cave> neighbours = currentCave.getCavePaths();

        for(int i = 0; i < neighbours.size(); i++) {
            Cave neighbour = neighbours.get(i);
            double edgeDistance = Cave.getDistanceBetweenTwoCaves(currentCave, neighbour);
            double newDistance = currentCave.getDistance() + edgeDistance;

            if(newDistance < neighbour.getDistance() ) {
                neighbour.setDistance(newDistance);
                unsettledCaves.put(neighbour.getCaveNumber(), neighbour);
                predecessors.put(neighbour, currentCave);
            }
        }
    }

    private Cave getCaveWithLowestDistance(Map<Integer, Cave> caves) {

        Cave caveWithLowestDistance = null;
        double lowestDistance = Double.POSITIVE_INFINITY;

        for(Cave currentCave : caves.values() ) {
            if(currentCave.getDistance() < lowestDistance) {
                caveWithLowestDistance = currentCave;
                lowestDistance = currentCave.getDistance();
            }
        }

        return caveWithLowestDistance;
    }

    public LinkedList<Cave> getPath(Cave target) {
        LinkedList<Cave> path = new LinkedList<>();
        Cave step = target;

        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }

        Collections.reverse(path);
        return path;
    }
}
