package com.wsd.model;

import java.util.Objects;

public class Vertex {
    private final String name;
    private Float x;
    private Float y;
    private Float width;
    private Float ascent;
    private Float descent;
    private Float lineStartX;
    private Float lineStartY;
    private Integer index;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return name.equals(vertex.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public Vertex(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getWidth() {
        return width;
    }

    public void setWidth(Float width) {
        this.width = width;
    }

    public Float getAscent() {
        return ascent;
    }

    public void setAscent(Float ascent) {
        this.ascent = ascent;
    }

    public Float getDescent() {
        return descent;
    }

    public void setDescent(Float descent) {
        this.descent = descent;
    }

    public void setLineStart() {
        this.lineStartX = this.x + this.width / 2;
        this.lineStartY = this.y + this.descent;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Float getLineStartX() {
        return lineStartX;
    }
}
