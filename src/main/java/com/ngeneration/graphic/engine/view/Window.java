package com.ngeneration.graphic.engine.view;

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

    public void addArea(DrawArea area) {
        areas.add(area);
    }

    public Vector getSize() {
        return size;
    }
}
