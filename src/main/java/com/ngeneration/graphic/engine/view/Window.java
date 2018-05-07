package com.ngeneration.graphic.engine.view;

import com.ngeneration.graphic.engine.Corner;
import com.ngeneration.graphic.engine.Vector;

import java.util.ArrayList;
import java.util.List;

public class Window {
    private final List<DrawArea> areas = new ArrayList<>();
    private Vector size;

    public Window(Vector size) {
        this.size = size;
        areas.add(new RectDrawArea(this, size.divide(2), size));
    }

    public Window(double x, double y) {
        this(new Vector(x, y));
    }

    public DrawArea allocateArea(Corner corner, double fractionX, double fractionY) {
        RectDrawArea area = new RectDrawArea(this, corner, fractionX, fractionY);
        areas.add(area);
        return area;
    }

    public Vector getSize() {
        return size;
    }

    public void close() {
        areas.forEach(DrawArea::close);
        areas.clear();
    }
}
