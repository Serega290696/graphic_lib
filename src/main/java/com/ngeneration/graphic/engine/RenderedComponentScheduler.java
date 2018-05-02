package com.ngeneration.graphic.engine;

import com.ngeneration.graphic.engine.physics.RenderedComponentStateUpdater;
import com.ngeneration.graphic.engine.view.RenderedComponent;

import java.util.HashSet;
import java.util.Set;

public class RenderedComponentScheduler extends Thread {
    private RenderedComponentStateUpdater updater;
    private Set<RenderedComponent> components = new HashSet<>();
    private double deltaTime = 0.1; //todo not my responsibility!

    public RenderedComponentScheduler(RenderedComponentStateUpdater updater) {
        this.updater = updater;
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
            long timeToSleep = (long) (deltaTime * 1000) - (System.nanoTime() - startIteration) / 1_000_000;
            if (timeToSleep >= 0) {
                try {
                    Thread.sleep(timeToSleep); //todo ok?}
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
        }
    }
}