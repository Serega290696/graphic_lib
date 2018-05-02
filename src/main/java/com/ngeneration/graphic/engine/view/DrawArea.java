package com.ngeneration.graphic.engine.view;

import com.ngeneration.graphic.engine.Vector;

import java.util.ArrayList;
import java.util.List;

public abstract class DrawArea {

    protected final List<DrawContext> contexts = new ArrayList<>();
    protected final Window holderWindow;
    protected Vector zoomFactor;

    public DrawArea(Window holderWindow) {
        this.holderWindow = holderWindow;
    }

    protected abstract boolean withinAreaBounds(Vector position);

    public void addContext(DrawContext context) {
        contexts.add(context);
    }

}
