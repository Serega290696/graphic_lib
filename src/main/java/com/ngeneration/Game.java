package com.ngeneration;

import com.ngeneration.custom_rendered_components.Car;
import com.ngeneration.custom_rendered_components.Road;
import com.ngeneration.graphic.engine.Corner;
import com.ngeneration.graphic.engine.DrawerScheduler;
import com.ngeneration.graphic.engine.RenderedComponentScheduler;
import com.ngeneration.graphic.engine.Vector;
import com.ngeneration.graphic.engine.view.DrawContext;
import com.ngeneration.graphic.engine.view.RectDrawArea;
import com.ngeneration.graphic.engine.view.RenderedComponent;
import com.ngeneration.graphic.engine.view.Window;

import java.util.HashSet;
import java.util.Set;

public class Game {

    private final Set<Window> windows = new HashSet<>();
    private final Set<RenderedComponent> renderedComponents = new HashSet<>();

    public void start() {
        Window window = new Window(900, 500);
        RectDrawArea area = new RectDrawArea(window, Corner.TOP_LEFT, 0.5, 0.5);
        DrawContext background = new DrawContext("background");
        DrawContext secondaryRole = new DrawContext("player");
        DrawContext player = new DrawContext("player");

        Car car = aCar();
        Road road = aDirectRoad();
        player.add(car, 10);
        secondaryRole.add(road, 5);

        area.addContext(background);
        area.addContext(secondaryRole);
        area.addContext(player);
        window.addArea(area);
        prepareGameObjects();
        windows.add(window);


        RenderedComponentScheduler scheduler = new RenderedComponentScheduler(new BrownianDriver());
        scheduler.add(car);
        DrawerScheduler drawerScheduler = new DrawerScheduler();
        drawerScheduler.add(car);
        System.out.println("Finish");
    }

    private Car aCar() {
        return new Car.Builder()
                .withSize(Vector.one())
                .withPosition(Vector.one())
                .withSpeed(Vector.one())
                .withAcceleration(Vector.zero())
                .withDriver(new BrownianDriver())
                .build();
    }

    private Road aDirectRoad() {
        return new Road.Builder()
                .firstBoundPoint(com.ngeneration.graphic.engine.Vector.diag(1))
                .nextBoundPoint(com.ngeneration.graphic.engine.Vector.diag(2))
                .nextBoundPoint(com.ngeneration.graphic.engine.Vector.diag(3))
                .nextBoundPoint(com.ngeneration.graphic.engine.Vector.diag(4))
                .nextBoundPoint(com.ngeneration.graphic.engine.Vector.diag(5))
                .firstBoundPoint(new Vector(4, 0))
                .nextBoundPoint(new Vector(4, 10))
                .firstBoundPoint(new Vector(6, 0))
                .nextBoundPoint(new Vector(6, 10))
                .build()
                ;
    }

    private void prepareGameObjects() {
        renderedComponents.add(aCar());
        renderedComponents.add(aDirectRoad());
    }


}
