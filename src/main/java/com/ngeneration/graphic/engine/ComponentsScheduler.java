package com.ngeneration.graphic.engine;

import com.ngeneration.graphic.engine.drawablecomponents.RenderedComponent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ComponentsScheduler<T extends RenderedComponent> { // todo the same interface as other schedulers. Or redesign this
    private Consumer<T> action;
    private Set<T> components = new HashSet<>();
    private Thread thread; // todo manage thread amount. Should use static field ExecutorService?
    private boolean pause;

    public ComponentsScheduler(Consumer<T> action) {
        this.action = action;
        thread = new Thread(this::run, "scheduler-" + this + "-thread");
        thread.start();
    }

    private void run() {
        int timesPerSecond = 3;//todo hardcode
        while (!thread.isInterrupted()) {
            long startIterationMillis = System.nanoTime() / 1_000_000;
            action();
            try {
                sleep(timesPerSecond, startIterationMillis);
            } catch (InterruptedException e) {
                if (thread.isInterrupted()) {
                    System.err.println("Unnecessary line here");
                }
                thread.interrupt();
            }
        }
    }

    private void action() {
        if (action != null) {
            for (T component : components) {
                action.accept(component);
            }
        }
    }

    private void sleep(int timesPerSecond, long startIterationMillis) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(
                1_000 / timesPerSecond
                        - (System.nanoTime() / 1_000_000 - startIterationMillis));
    }

    public boolean isPause() {
        return pause;
    }

    public void pause() {
        this.pause = true;
    }

    public void resume() {
        this.pause = false;
    }

    public void add(T component) {
        components.add(component);
    }

    public void remove(RenderedComponent component) {
        components.remove(component);
    }
}
