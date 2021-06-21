package com.wsd.service;

import com.wsd.model.Direction;
import com.wsd.model.Edge;
import com.wsd.model.Graph;
import com.wsd.model.Vertex;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class VisualiserG2D {
    private final Graph graph;
    private final Graphics2D g;
    private final BufferedImage bufferedImage;
    private final int PADDING = 40;
    private final int LEFT_PADDING_TEXT = 2;
    private final int MESSAGE_SYMBOL_PADDING = 60;
    private final String FONT_NAME = "Lucida Sans";
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
        return (int) (MESSAGE_SYMBOL_PADDING + fv.getY() + lineHeight);
    }

    private int calculateMessageSymbolYStartPosition() {
        Vertex fv = graph.getIndexToVertex().get(DEFAULT_VERTEX);
        return (int) (fv.getY() + fv.getDescent() + fv.getAscent() + PADDING);
    }

    private BufferedImage createBufferedImage() {
        return new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    }

    private Graphics2D createGraphics2D() {
        Graphics2D g = bufferedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setBackground(new Color(100, 255, 218));
        g.clearRect(0, 0, imageWidth, imageHeight);
        return g;
    }

    private void drawObject(double x, double y, double width, double height) {
        Rectangle2D r = new  Rectangle2D.Double(x, y, width, height);
        g.setPaint(Color.white);
        g.fill(r);
        g.setPaint(Color.black);
        g.setStroke(new BasicStroke(1));
        g.draw(r);
        g.setStroke(new BasicStroke(0));
    }

    private void drawLine(double x1, double y1, double x2, double y2) {
        Line2D topToBottomConnector = new Line2D.Double(x1, y1, x2, y2);
        g.setPaint(Color.black);
        g.setStroke(new BasicStroke(1));
        g.draw(topToBottomConnector);
    }

    private void drawString(String quote, float x, float y, int fontSize) {
        g.setFont(new Font(FONT_NAME, Font.PLAIN, fontSize));
        g.setPaint(Color.black);
        g.drawString(quote, x, y);
    }

    private void drawObjectAndActivationLine(Vertex v) {
        String quote = v.getName();
        float x = v.getX();
        float y = v.getY();
        float width = v.getWidth();
        float ascent = v.getAscent();
        float descent = v.getDescent();
        float height = ascent + descent;
        float lineStartX = x + width / 2;
        float lineStartY = y - ascent + height;
        float lineEndY = lineStartY + lineHeight;
        drawObject(x, y - ascent, width, height);
        drawString(quote, x + LEFT_PADDING_TEXT, y, OBJECT_FONT_SIZE);
        drawLine(lineStartX, lineStartY, lineStartX, lineEndY);
        drawObject(x, lineEndY - ascent, width, height);
        drawString(quote, x + LEFT_PADDING_TEXT, lineEndY, OBJECT_FONT_SIZE);
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
        double ux = x + ARROW_LENGTH * Math.cos(radian1);
        double uy = y + ARROW_LENGTH * Math.sin(radian1);
        double dx = x + ARROW_LENGTH * Math.cos(radian2);
        double dy = y + ARROW_LENGTH * Math.sin(radian2);
        drawLine(x, y, ux, uy);
        drawLine(x, y, dx, dy);
    }

    private void drawMessageSymbol(Vertex from, Vertex to, String weight, int symbolY) {
        drawString(weight, Math.min(from.getLineStartX(), to.getLineStartX()) + LEFT_PADDING_TEXT,
                symbolY - MESSAGE_SYMBOL_TEXT_BOTTOM_PADDING, MESSAGE_SYMBOL_FONT_SIZE);
        float fromLineStartX = from.getLineStartX();
        float toLineStartX = to.getLineStartX();
        if (from.equals(to)) {
            float paddedLineStartX = fromLineStartX + PADDING;
            float paddedSymbolY = symbolY + PADDING_CYCLIC_EDGE;
            drawLine(fromLineStartX, symbolY, paddedLineStartX, symbolY);
            drawLine(paddedLineStartX, symbolY, paddedLineStartX, paddedSymbolY);
            drawLine(paddedLineStartX, paddedSymbolY, fromLineStartX, paddedSymbolY);
            drawMessageSymbolHead(fromLineStartX, paddedSymbolY, Direction.LEFT);
        } else {
            drawLine(fromLineStartX, symbolY, toLineStartX, symbolY);
            if (fromLineStartX < toLineStartX) {
                drawMessageSymbolHead(toLineStartX, symbolY, Direction.RIGHT);
            } else {
                drawMessageSymbolHead(toLineStartX, symbolY, Direction.LEFT);
            }
        }
    }

    private void drawObjectAndActivationLines() {
        graph.getIndexToVertex().forEach((k, v) -> drawObjectAndActivationLine(v));
    }

    private void drawMessageSymbols() {
        int messageY = messageSymbolY;
        for (Edge edge: graph.getEdgesSequence()) {
            Vertex from = edge.getStartVertex();
            Vertex to = edge.getVertex();
            String weight = edge.getWeight();
            drawMessageSymbol(from, to, weight, messageY);
            messageY += MESSAGE_SYMBOL_PADDING;
        }
    }

    private void dispose() {
        g.dispose();
    }

    public VisualiserG2D(Graph graph) {
        this.graph = graph;
        this.lineHeight = graph.getCountEdgeSpace() * MESSAGE_SYMBOL_PADDING;
        this.imageWidth = calculateImageWidth();
        this.imageHeight = calculateImageHeight();
        this.messageSymbolY = calculateMessageSymbolYStartPosition();
        this.bufferedImage = createBufferedImage();
        this.g = createGraphics2D();
    }

    public void draw() {
        drawObjectAndActivationLines();
        drawMessageSymbols();
    }

    public byte[] getImageByteArray() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        dispose();
        return byteArrayOutputStream.toByteArray();
    }
}
