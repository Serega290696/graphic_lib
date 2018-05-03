package com.ngeneration.graphic.engine.drawers;

import com.ngeneration.graphic.engine.drawablecomponents.RenderedComponent;

public class Console1DDrawer extends Drawer {
    public void doRender(RenderedComponent component) {
        double position = component.getPosition().getX();
        for (int i = 0; i < Math.abs(position / 10); i++) {
            System.out.print(".");
        }
        System.out.println(" " + component.getPosition());
    }

    @Override
    public boolean isDrawable(RenderedComponent component) {
        return component != null
                && component.getPosition() != null
                && component.getSize() != null;
    }
}
