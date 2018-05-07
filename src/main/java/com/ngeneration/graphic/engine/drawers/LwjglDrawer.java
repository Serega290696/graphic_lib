package com.ngeneration.graphic.engine.drawers;

import com.fuzzygroup.view.Shape;
import com.fuzzygroup.view.ThreeVector;
import com.fuzzygroup.view.VisualRepresentationState;
import com.fuzzygroup.view.enums.ColorEnum;
import com.ngeneration.graphic.engine.Vector;
import com.ngeneration.graphic.engine.drawablecomponents.RenderedComponent;
import com.ngeneration.graphic.engine.lwjgl_engine.LwjglEngine;

public class LwjglDrawer extends Drawer {
    private static final LwjglEngine engine = new LwjglEngine();
    private static final ThreeVector nilVector = new ThreeVector(0, 0, 0);

    public void doRender(RenderedComponent component) {
        engine.draw(lwjglRepresentation(component), nilVector, nilVector,
                0, 0, 0);
    }

    private VisualRepresentationState lwjglRepresentation(RenderedComponent component) {
        return new VisualRepresentationState(
                toThreeVector(component.getPosition()),
                nilVector,
                nilVector,
                toThreeVector(component.getSize()),
                Shape.RECT,
                ColorEnum.BLUE,
                0,
                0,
                true
                );
    }

    private ThreeVector toThreeVector(Vector vector) {
        return new ThreeVector(vector.getX(), vector.getY(), 0);
    }

    @Override
    public boolean isDrawable(RenderedComponent component) {
        return component != null
                && component.getPosition() != null
                && component.getSize() != null;
    }

    @Override
    protected void init() {
        super.init();
        engine.init();
    }


    protected void prepareToNextFrame() {
        engine.nextFrame();
    }

    protected void afterFrame() {
        engine.afterFrame();
    }
}
