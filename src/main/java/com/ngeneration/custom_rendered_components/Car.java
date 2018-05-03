package com.ngeneration.custom_rendered_components;

import com.fuzzygroup.view.Shape;
import com.fuzzygroup.view.enums.ColorEnum;
import com.ngeneration.ai.Driver;
import com.ngeneration.graphic.engine.Vector;
import com.ngeneration.graphic.engine.drawablecomponents.PhysicalRenderedComponent;

public class Car extends PhysicalRenderedComponent {
    private Driver driver;

    Car(Builder builder) {
        this.position = builder.position;
        this.size = builder.size;
        this.shapes = builder.shapes;
        this.colors = builder.colors;
        this.rotation = builder.rotation;
        this.visible = builder.visible;
        this.opacity = builder.opacity;
        this.speed = builder.speed;
        this.acceleration = builder.acceleration;
        this.driver = builder.driver;
    }

    public static class Builder {
        Vector position;
        Vector size;
        Shape shapes;
        ColorEnum colors;
        double rotation;
        boolean visible = true;
        double opacity;
        Vector speed;
        Vector acceleration;
        Driver driver;

        public Builder withPosition(Vector position) {
            this.position = position;
            return this;
        }

        public Builder withSize(Vector size) {
            this.size = size;
            return this;
        }

        public Builder withShapes(Shape shapes) {
            this.shapes = shapes;
            return this;
        }

        public Builder withColors(ColorEnum colors) {
            this.colors = colors;
            return this;
        }

        public Builder withRotation(double rotation) {
            this.rotation = rotation;
            return this;
        }

        public Builder withVisible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder withOpacity(double opacity) {
            this.opacity = opacity;
            return this;
        }

        public Builder withSpeed(Vector speed) {
            this.speed = speed;
            return this;
        }

        public Builder withAcceleration(Vector acceleration) {
            this.acceleration = acceleration;
            return this;
        }

        public Builder withDriver(Driver driver) {
            this.driver = driver;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }
}
