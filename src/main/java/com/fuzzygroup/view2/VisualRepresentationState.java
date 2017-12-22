package com.fuzzygroup.view2;

import com.fuzzygroup.view.Shape;
import com.fuzzygroup.view2.enums.ColorEnum;

import java.util.Map;

public class VisualRepresentationState {

    Shape shapes;
    ColorEnum colors;
    ThreeVector position;
    ThreeVector speed;
    ThreeVector acceleration;
    ThreeVector size;
    double rotation;
    boolean visible = true;
    private double opacity;

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public VisualRepresentationState(ThreeVector position, ThreeVector speed, ThreeVector acceleration, ThreeVector size,
                                     Shape shapes, ColorEnum colors,
                                     boolean visible) {
        this.shapes = shapes;
        this.colors = colors;
        this.position = position;
        this.speed = speed;
        this.acceleration = acceleration;
        this.size = size;
        this.visible = visible;
    }



    VisualRepresentationState cloneState() {
        return new VisualRepresentationState(position, speed, acceleration, size, shapes, colors, visible);
    }

    @Override
    public String toString() {
        return String.format("VisualRepresentation {%-30s %-30s %-30s %b}", size, position, speed, visible);
    }

    public Shape getShape() {
        return shapes;
    }

    public ColorEnum getColors() {
        return colors;
    }

    public ThreeVector getPosition() {
        return position;
    }

    public ThreeVector getSpeed() {
        return speed;
    }

    public ThreeVector getAcceleration() {
        return acceleration;
    }

    public boolean isVisible() {
        return visible;
    }

    public ThreeVector getSize() {
        return size;
    }

    public void setShapes(Shape shapes) {
        this.shapes = shapes;
    }

    public void setColors(ColorEnum colors) {
        this.colors = colors;
    }

    public void setPosition(ThreeVector position) {
        this.position = position;
    }

    public void setSpeed(ThreeVector speed) {
        this.speed = speed;
    }

    public void setAcceleration(ThreeVector acceleration) {
        this.acceleration = acceleration;
    }

    public void setSize(ThreeVector size) {
        this.size = size;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public double getOpacity() {
        return opacity;
    }
}
