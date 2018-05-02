package com.ngeneration;

import com.ngeneration.custom_rendered_components.Car;
import com.ngeneration.graphic.engine.Vector;

public class BrownianDriver implements Driver {

    @Override
    public void accept(Car component) {
        double deltaTime = 1;
        component.setSpeed(Vector.diag(10));
        component.setRotation(Math.random() * Math.PI * 2);
        //todo chain responsibility?
        component.setPosition(component.getPosition()
                .plus(new Vector(component.getSpeed().getX() * deltaTime, 0)));
    }
}
