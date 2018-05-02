package com.ngeneration.graphic.engine.view;

import com.fuzzygroup.view.Shape;
import com.fuzzygroup.view.enums.ColorEnum;
import com.ngeneration.graphic.engine.Vector;
import com.ngeneration.graphic.engine.view.DrawContext;
import com.ngeneration.graphic.engine.view.Drawer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

public class RenderedComponent {
    protected Vector position;
    protected Vector size;
    protected Shape shapes;
    protected ColorEnum colors;
    protected double rotation;
    protected boolean visible = true;
    protected double opacity;

    protected Set<DrawContext> contexts = new HashSet<>();

    protected Drawer drawer = new Console1DDrawer();//todo: inject?

    public void render() {
        drawer.render(this);
    }

    void addToContext(DrawContext context) {
        contexts.add(context);
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getSize() {
        return size;
    }

}
