package com.ngeneration.graphic.engine;

import com.fuzzygroup.view.Display;
import com.fuzzygroup.view.Shape;
import com.fuzzygroup.view.ThreeVector;
import com.fuzzygroup.view.VisualRepresentationState;
import com.fuzzygroup.view.enums.ColorEnum;
import com.ngeneration.graphic.engine.drawablecomponents.RenderedComponent;
import com.ngeneration.graphic.engine.lwjgl_engine.LwjglEngine;

import java.util.HashSet;
import java.util.Set;

public enum ArtistTmp { // todo the same interface as other schedulers. Or redesign this
    INSTANCE;
    private static final ThreeVector nilVector = new ThreeVector(0, 0, 0);
    private static final ThreeVector oneVector = new ThreeVector(1, 1, 1);

    private Set<RenderedComponent> components = new HashSet<>();
    private LwjglEngine engine = new LwjglEngine();
    private boolean started;

    public void add(RenderedComponent component) {
        components.add(component);
    }

    public void start() {

        if (!started) {
            new Thread(() -> {
                initGraphic();
                while (!Thread.currentThread().isInterrupted()) {
                    beforeFrame();
                    for (RenderedComponent component : components) {
                        renderComponent(component);
                    }
                    afterFrame();
                }

            }).start();
            started = true;
        }
    }

    public Display createDisplay() {
        Display display = new Display("main", new LwjglEngine(), 900, 500);
        display.setFps(60);
        display.disableHistoryLogging();
//        engine.createDisplay(display);
        return display;
    }

    private void initGraphic() {
        engine.init();
    }

    private void beforeFrame() {
        engine.nextFrame();

    }

    private void renderComponent(RenderedComponent component) {
        engine.draw(lwjglRepresentation(component),
                oneVector, nilVector, 0, 0, 0);
    }

    private void afterFrame() {
        engine.afterFrame();

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

    public boolean isDrawable(RenderedComponent component) {
        return component != null
                && component.getPosition() != null
                && component.getSize() != null;
    }

}
