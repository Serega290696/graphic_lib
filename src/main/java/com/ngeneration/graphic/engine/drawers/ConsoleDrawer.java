package com.ngeneration.graphic.engine.drawers;

import com.ngeneration.graphic.engine.drawablecomponents.RenderedComponent;

public class ConsoleDrawer extends Drawer {
    public void doRender(RenderedComponent component) {
        System.out.println("\tDraw component:");
        System.out.println("\t\t" + component.getPosition());
        System.out.println("\t\t" + component.getSize());
    }

    @Override
    public boolean isDrawable(RenderedComponent component) {
        return component != null;
    }
}
