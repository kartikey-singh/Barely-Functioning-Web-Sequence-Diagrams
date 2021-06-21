package com.wsd.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Graph {
    private static final Logger LOG = LoggerFactory.getLogger(Graph.class);
    private final Map<Vertex, List<Edge>> adjVertices;
    private final Map<Vertex, List<Edge>> invAdjVertices;
    private final Map<Integer, Vertex> indexToVertex;
    private final List<SequenceState> sequenceStates;
    private final Map<String, Vertex> nameToVertex;
    private final List<Edge> edgesSequence;
    private int countEdgeSpace;

    private void addVertex(String label) {
        Vertex v = new Vertex(label);
        nameToVertex.putIfAbsent(label, v);
        v = nameToVertex.get(label);
        adjVertices.putIfAbsent(v, new ArrayList<>());
        invAdjVertices.putIfAbsent(v, new ArrayList<>());
    }

    private void addEdge(String label1, String label2, String weight) {
        Vertex v1 = nameToVertex.get(label1);
        Vertex v2 = nameToVertex.get(label2);
        adjVertices.get(v1).add(new Edge(v2, weight));
        invAdjVertices.get(v2).add(new Edge(v1, weight));
        edgesSequence.add(new Edge(v2, weight, v1));
    }

    private void createGraph() {
        countEdgeSpace = 1;
        for (SequenceState sequenceState : sequenceStates) {
            String from = sequenceState.getFrom();
            String to = sequenceState.getTo();
            String text = sequenceState.getText();
            addVertex(from);
            addVertex(to);
            addEdge(from, to, text);
            if (from.equals(to)) countEdgeSpace++;
            countEdgeSpace++;
        }
    }

    private void createIndexToVertexMapping() {
        int counter = 0;
        for (Vertex vertex : adjVertices.keySet()) {
            vertex.setIndex(counter);
            indexToVertex.put(counter++, vertex);
        }
    }

    private void printAdjGraph() {
        adjVertices.forEach((k, v) -> LOG.info("Key = " + k + ", Value = " + v));
    }

    private void printInvAdjGraph() {
        invAdjVertices.forEach((k, v) -> LOG.info("Key = " + k + ", Value = " + v));
    }

    public Graph(List<SequenceState> sequenceStates) {
        this.adjVertices = new LinkedHashMap<>();
        this.invAdjVertices = new LinkedHashMap<>();
        this.indexToVertex = new HashMap<>();
        this.sequenceStates = sequenceStates;
        this.nameToVertex = new HashMap<>();
        this.edgesSequence = new ArrayList<>();
        createGraph();
        createIndexToVertexMapping();
        LOG.info("Logging adjacency graph starts ...");
        printAdjGraph();
        LOG.info("Logging adjacency graph finishes");
        LOG.info("Logging inverse adjacency graph starts ...");
        printInvAdjGraph();
        LOG.info("Logging inverse adjacency graph finishes");

    }

    public List<Edge> getAdjVertices(Vertex v) {
        return adjVertices.get(v);
    }

    public List<Edge> getInvAdjVertices(Vertex v) {
        return invAdjVertices.get(v);
    }

    public Map<Integer, Vertex> getIndexToVertex() {
        return indexToVertex;
    }

    public int getCountEdgeSpace() {
        return countEdgeSpace;
    }

    public Map<Vertex, List<Edge>> getAdjVertices() {
        return adjVertices;
    }

    public List<Edge> getEdgesSequence() {
        return edgesSequence;
    }
}
