package com.ngeneration.graphic.engine.drawablecomponents;

import com.fuzzygroup.view.Shape;
import com.fuzzygroup.view.enums.ColorEnum;
import com.ngeneration.graphic.engine.Vector;
import com.ngeneration.graphic.engine.drawers.Console1DDrawer;
import com.ngeneration.graphic.engine.drawers.Drawer;
import com.ngeneration.graphic.engine.view.DrawContext;

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
        if (visible) {
            drawer.render(this);
        }
    }

    /*
        This method only for internal use.
        Investigate possibility of using this method only within package.
        Probably should use Jigsaw?
     */
    public void addToContext(DrawContext context) {
        contexts.add(context);
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getSize() {
        return size;
    }

    public void setRotation(double radian) {
        this.rotation = radian;
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public void setSize(Vector size) {
        this.size = size;
    }

    public Shape getShapes() {
        return shapes;
    }

    public void setShapes(Shape shapes) {
        this.shapes = shapes;
    }

    public ColorEnum getColors() {
        return colors;
    }

    public void setColors(ColorEnum colors) {
        this.colors = colors;
    }

    public double getRotation() {
        return rotation;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public double getOpacity() {
        return opacity;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }
}
