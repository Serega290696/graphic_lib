package com.ngeneration.graphic.engine.view;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class DrawContext
//        implements List<List<RenderedComponent>>//todo?
{
    private final String name;
    private boolean isActivated = true;
    private final List<List<RenderedComponent>> layersWithComponents = new ArrayList<>(100);

    public DrawContext(String name) {
        this.name = name;
        for (int i = 0; i < 100; i++) {
            layersWithComponents.add(new ArrayList<>()); //todo dont like this
        }
    }

    public boolean add(RenderedComponent component, int layer) {
        component.addToContext(this);
        return layersWithComponents.get(layer).add(component);
    }

    public String getName() {
        return name;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }
}
