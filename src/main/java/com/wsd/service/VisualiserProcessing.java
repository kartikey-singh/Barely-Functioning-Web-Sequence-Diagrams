package com.wsd.service;

import com.wsd.model.Direction;
import com.wsd.model.Edge;
import com.wsd.model.Graph;
import com.wsd.model.Vertex;
import processing.core.PApplet;

import java.util.List;
import java.util.Map;

public class VisualiserProcessing extends PApplet {
    private final Graph graph;
    private final String[] processingArgs;
    private final int PADDING = 40;
    private final int LEFT_PADDING_TEXT = 2;
    private final int MESSAGE_SYMBOL_PADDING = 60;
    private final int MESSAGE_SYMBOL_TEXT_BOTTOM_PADDING = 10;
    private final int MESSAGE_SYMBOL_FONT_SIZE = 16;
    private final int PADDING_CYCLIC_EDGE = 20;
    private final int OBJECT_FONT_SIZE = 24;
    private final int DEFAULT_VERTEX = 0;
    private final int ARROW_LENGTH = 10;
    private final int lineHeight;
    private final int imageHeight;
    private final int imageWidth;
    private final int messageSymbolY;

    private int calculateImageWidth() {
        Map<Integer, Vertex> indexToVertex = graph.getIndexToVertex();
        int lastVertex = indexToVertex.size() - 1;
        Vertex lv = indexToVertex.get(lastVertex);
        return (int) (lv.getWidth() + lv.getX() + PADDING);
    }

    private int calculateImageHeight() {
        Vertex fv = graph.getIndexToVertex().get(DEFAULT_VERTEX);
        return (int) (MESSAGE_SYMBOL_PADDING * 2 + fv.getY() * 2 + lineHeight);
    }

    private int calculateMessageSymbolYStartPosition() {
        Vertex fv = graph.getIndexToVertex().get(DEFAULT_VERTEX);
        return (int) (fv.getY() + fv.getDescent() + fv.getAscent() + PADDING);
    }

    private void drawObjectAndActivationLine(Vertex v) {
        String quote = v.getName();
        textSize(OBJECT_FONT_SIZE);
        float x = v.getX();
        float y = v.getY();
        float width = v.getWidth();
        float ascent = v.getAscent();
        float descent = v.getDescent();
        float height = ascent + descent;
        float lineStartX = x + width / 2;
        float lineStartY = y - ascent + height;
        float lineEndY = lineStartY + lineHeight;
        fill(255);
        rect(x, y - ascent, width, height);
        fill(0);
        text(quote, x + LEFT_PADDING_TEXT, y);
        fill(255);
        line(lineStartX, lineStartY, lineStartX, lineEndY);
        fill(255);
        rect(x, lineEndY - ascent, width, height);
        fill(0);
        text(quote, x + LEFT_PADDING_TEXT, lineEndY);
    }

    private void drawMessageSymbolHead(float x, float y, Direction d) {
        double radian1 = 0.0, radian2 = 0.0;
        if (d.equals(Direction.LEFT)) {
            radian1 = Math.toRadians(45);
            radian2 = Math.toRadians(315);
        } else if (d.equals(Direction.RIGHT)) {
            radian1 = Math.toRadians(135);
            radian2 = Math.toRadians(225);
        }
        float ux = (float) (x + ARROW_LENGTH * Math.cos(radian1));
        float uy = (float) (y + ARROW_LENGTH * Math.sin(radian1));
        float dx = (float) (x + ARROW_LENGTH * Math.cos(radian2));
        float dy = (float) (y + ARROW_LENGTH * Math.sin(radian2));
        line(x, y, ux, uy);
        line(x, y, dx, dy);
    }

    private void drawMessageSymbol(Vertex from, Vertex to, String weight, int symbolY) {
        fill(0);
        textSize(MESSAGE_SYMBOL_FONT_SIZE);
        text(weight, Math.min(from.getLineStartX(), to.getLineStartX()) + LEFT_PADDING_TEXT,
                symbolY - MESSAGE_SYMBOL_TEXT_BOTTOM_PADDING);
        if (from.equals(to)) {
            float paddedLineStartX = from.getLineStartX() + PADDING;
            float paddedSymbolY = symbolY + PADDING_CYCLIC_EDGE;

            line(from.getLineStartX(), symbolY, paddedLineStartX, symbolY);
            line(paddedLineStartX, symbolY, paddedLineStartX, paddedSymbolY);
            line(paddedLineStartX, paddedSymbolY, from.getLineStartX(), paddedSymbolY);
            drawMessageSymbolHead(from.getLineStartX(), paddedSymbolY, Direction.LEFT);
        } else {
            line(from.getLineStartX(), symbolY, to.getLineStartX(), symbolY);
            if (from.getLineStartX() < to.getLineStartX()) {
                drawMessageSymbolHead(to.getLineStartX(), symbolY, Direction.RIGHT);
            } else {
                drawMessageSymbolHead(to.getLineStartX(), symbolY, Direction.LEFT);
            }
        }
    }

    public VisualiserProcessing(Graph graph) {
        this.graph = graph;
        this.processingArgs = new String[]{"Visualiser"};
        this.lineHeight = graph.getCountEdgeSpace() * MESSAGE_SYMBOL_PADDING;
        this.imageWidth = calculateImageWidth();
        this.imageHeight = calculateImageHeight();
        this.messageSymbolY = calculateMessageSymbolYStartPosition();
    }

    public void settings() {
        size(imageWidth, imageHeight);
        smooth();
    }

    public void draw() {
        // GLOBAL COLOR
        background(194, 234, 190);
        int messageY = messageSymbolY;
        graph.getIndexToVertex().forEach((k, v) -> drawObjectAndActivationLine(v));
        for (Map.Entry<Vertex, List<Edge>> vertexEdge : graph.getAdjVertices().entrySet()) {
            Vertex from = vertexEdge.getKey();
            for (Edge edge : vertexEdge.getValue()) {
                Vertex to = edge.getVertex();
                String weight = edge.getWeight();
                drawMessageSymbol(from, to, weight, messageY);
                messageY += MESSAGE_SYMBOL_PADDING;
            }
        }
        save("example.png");
        exit();
    }

    public String[] getProcessingArgs() {
        return processingArgs;
    }
}
