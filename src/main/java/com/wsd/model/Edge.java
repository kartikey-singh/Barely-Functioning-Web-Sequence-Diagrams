package com.wsd.model;

public class Edge {
    private final Vertex vertex;
    private final String weight;
    private final Vertex startVertex;

    public Edge(Vertex vertex, String weight) {
        this.vertex = vertex;
        this.weight = weight;
        this.startVertex = null;
    }

    public Edge(Vertex vertex, String weight, Vertex startVertex) {
        this.vertex = vertex;
        this.weight = weight;
        this.startVertex = startVertex;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public String getWeight() {
        return weight;
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "vertex=" + vertex +
                ", weight='" + weight + '\'' +
                '}';
    }
}
