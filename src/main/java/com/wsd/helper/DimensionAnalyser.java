package com.wsd.helper;

import com.wsd.model.Edge;
import com.wsd.model.Graph;
import com.wsd.model.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DimensionAnalyser {
    private final BufferedImage bufferedImage;
    private final Graphics2D g2d;
    private final FontMetrics fm;
    private final Graph graph;
    private final int BUFFERED_IMAGE_WIDTH = 1000;
    private final int BUFFERED_IMAGE_HEIGHT = 1000;
    private final int FONT_SIZE = 24;
    private final String FONT_NAME = "Lucida Sans";
    private final int TEXT_WIDTH_PADDING = 20;
    private final int START_BOX_X = 40;
    private final int START_BOX_Y = 40;
    private final int MINIMAL_PADDING = 40;

    public DimensionAnalyser(Graph graph) {
        this.bufferedImage = new BufferedImage(BUFFERED_IMAGE_WIDTH, BUFFERED_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.g2d = bufferedImage.createGraphics();
        g2d.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));
        this.fm = g2d.getFontMetrics();
        this.graph = graph;
    }

    private float getMaxWeightWidth(Edge edge, int fromIndex) {
        float weightWidth = 0F;
        Vertex to = edge.getVertex();
        String weight = edge.getWeight();
        int toIndex = to.getIndex();
        if (fromIndex >= toIndex) {
            float toLineX = to.getLineStartX();
            weightWidth = (float) fm.stringWidth(weight) + TEXT_WIDTH_PADDING + toLineX;
            return weightWidth;
        }
        return weightWidth;
    }

    private void adjustStartXForBox(Vertex from) {
        int fromIndex = from.getIndex();
        float maxWeightWidth = 0F;

        // For edges that are outbound from this vertex and that come in sequence earlier than this.
        for (Edge edge : graph.getAdjVertices(from)) {
            maxWeightWidth = Math.max(maxWeightWidth, getMaxWeightWidth(edge, fromIndex));
        }

        // For edges that are inbound to this vertex and that come in sequence earlier than this.
        for (Edge edge : graph.getInvAdjVertices(from)) {
            maxWeightWidth = Math.max(maxWeightWidth, getMaxWeightWidth(edge, fromIndex));
        }

        // For self loop edges just left to the current vertex.
        Vertex justLeftToFrom = graph.getIndexToVertex().get(fromIndex - 1);
        for (Edge edge : graph.getInvAdjVertices(justLeftToFrom)) {
            if (justLeftToFrom.equals(edge.getVertex())) {
                maxWeightWidth = Math.max(maxWeightWidth, getMaxWeightWidth(edge, fromIndex));
            }
        }

        if (maxWeightWidth > from.getX()) {
            from.setX(maxWeightWidth);
            from.setLineStart();
        }
    }

    private void estimateBox(Vertex vertex, float startX, float startY) {
        vertex.setX(startX);
        vertex.setY(startY);
        vertex.setWidth((float) fm.stringWidth(vertex.getName()) + TEXT_WIDTH_PADDING);
        vertex.setAscent((float) fm.getAscent());
        vertex.setDescent((float) fm.getDescent());
        vertex.setLineStart();
    }

    private void dispose() {
        g2d.dispose();
    }

    public void estimate() {
        int totalVertices = graph.getIndexToVertex().size();
        Vertex prevVertex = null;
        for (int index = 0; index < totalVertices; index++) {
            Vertex v = graph.getIndexToVertex().get(index);
            if (index == 0) {
                estimateBox(v, START_BOX_X, START_BOX_Y);
            } else {
                float minimalStartX = prevVertex.getX() + prevVertex.getWidth() + MINIMAL_PADDING;
                float minimalStartY = prevVertex.getY();
                estimateBox(v, minimalStartX, minimalStartY);
                adjustStartXForBox(v);
            }
            prevVertex = v;
        }
        dispose();
    }
}
