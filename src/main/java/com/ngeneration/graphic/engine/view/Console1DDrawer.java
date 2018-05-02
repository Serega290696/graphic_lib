package com.ngeneration.graphic.engine.view;

public class Console1DDrawer implements Drawer {
    public void render(RenderedComponent component) {
        double position = component.getPosition().getX();
        for (int i = 0; i < Math.abs(position / 10); i++) {
            System.out.print(".");
        }
        System.out.println(" " + component.getPosition());
    }
}
