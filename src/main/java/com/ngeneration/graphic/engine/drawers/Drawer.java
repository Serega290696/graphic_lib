package com.ngeneration.graphic.engine.drawers;

import com.ngeneration.graphic.engine.drawablecomponents.RenderedComponent;

public abstract class Drawer {
    public void render(RenderedComponent component) {
        if (isDrawable(component)) {
            doRender(component);
        }
    }

    protected abstract void doRender(RenderedComponent component);

    protected abstract boolean isDrawable(RenderedComponent component);

    protected void init() {

    }

    protected void prepareToNextFrame() {

    }
}
