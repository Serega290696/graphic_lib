package com.ngeneration.graphic.engine.view;

public class ConsoleDrawer implements Drawer {
    public void render(RenderedComponent component) {
        System.out.println("\tDraw component:");
        System.out.println("\t\t" + component.getPosition());
        System.out.println("\t\t" + component.getSize());
    }
}
