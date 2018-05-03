package com.ngeneration.ai;

import com.ngeneration.custom_rendered_components.Car;
import com.ngeneration.graphic.engine.Vector;

public class BrownianDriver implements Driver {

    @Override
    public void accept(Car component) {
        computeSpeedModule(component); //todo don`t like 'compute'. Look smth like enrich, assign, ...
        computeSpeedDirection(component);
    }

    private void computeSpeedModule(Car component) {
        component.setRotation(Math.random() * Math.PI * 2);
    }

    private void computeSpeedDirection(Car component) {
        component.setSpeed(new Vector.PolarCoordinateSystemVector(component.getRotation(), 10)
                .toFlatCartesianVector());
    }
}
