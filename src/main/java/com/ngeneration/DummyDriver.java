package com.ngeneration;

import com.ngeneration.custom_rendered_components.Car;

public class DummyDriver implements Driver {

    @Override
    public void accept(Car component) {
        component.setRotation(0);
    }
}
