package com.ngeneration.graphic.engine;

import com.ngeneration.graphic.engine.physics.RenderedComponentStateUpdater;
import com.ngeneration.graphic.engine.view.RenderedComponent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class DrawerScheduler extends Thread { // todo the same interface as other schedulers. Or redesign this
    private Consumer<RenderedComponent> updater;
    private Set<RenderedComponent> components = new HashSet<>();

    public DrawerScheduler() {
        this.updater = RenderedComponent::render;
        start();
    }

    public void add(RenderedComponent component) {
        components.add(component);
    }

    @Override
    public void run() {
        super.run();
        long startIteration;
        while (!isInterrupted()) {
            startIteration = System.nanoTime();
            if (updater != null) {
                components.forEach(updater);
            }
            try {
                Thread.sleep(100); //todo hardcode
            } catch (InterruptedException e) {
                interrupt();
            }
        }
    }
}
