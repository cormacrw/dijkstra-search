package com.dijkstra.request;

import java.util.List;

public class SearchParams {
    private List<Node> nodes;
    private int target;

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}
