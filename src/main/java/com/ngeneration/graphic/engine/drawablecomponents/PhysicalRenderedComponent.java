package com.ngeneration.graphic.engine.drawablecomponents;

import com.ngeneration.graphic.engine.Vector;

public class PhysicalRenderedComponent extends RenderedComponent {
    protected Vector speed;
    protected Vector acceleration;

    public Vector getSpeed() {
        return speed;
    }

    public void setSpeed(Vector speed) {
        this.speed = speed;
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector acceleration) {
        this.acceleration = acceleration;
    }
}
