package com.ngeneration;

import com.ngeneration.ai.BrownianDriver;
import com.ngeneration.custom_rendered_components.Car;
import com.ngeneration.custom_rendered_components.Road;
import com.ngeneration.graphic.engine.ComponentsScheduler;
import com.ngeneration.graphic.engine.Corner;
import com.ngeneration.graphic.engine.commands.Command;
import com.ngeneration.graphic.engine.commands.QuitCommand;
import com.ngeneration.graphic.engine.drawablecomponents.PhysicalRenderedComponent;
import com.ngeneration.graphic.engine.physics.PhysicalRenderedComponentStateUpdater;
import com.ngeneration.graphic.engine.view.DrawContext;
import com.ngeneration.graphic.engine.view.RectDrawArea;
import com.ngeneration.graphic.engine.drawablecomponents.RenderedComponent;
import com.ngeneration.graphic.engine.view.Window;

import java.util.HashSet;
import java.util.Set;

public class Simulation {

    private final Set<Window> windows = new HashSet<>();
    private final Set<RenderedComponent> renderedComponents = new HashSet<>();
    private final Command QUIT = new QuitCommand(this);

    public void start() {
        System.out.println("Loading simulation. . .");
        System.out.println("Initialise graphic");
        Window window = new Window(900, 500);
        RectDrawArea area = new RectDrawArea(window, Corner.TOP_LEFT, 0.5, 0.5);
        DrawContext background = new DrawContext("background");
        DrawContext secondaryRole = new DrawContext("player");
        DrawContext player = new DrawContext("player");

        System.out.println("Loading simulation components");
        Car car = ComponentsFactory.aCar().build();
        Road road = ComponentsFactory.aDirectRoad().build();
        player.add(car, 10);
        secondaryRole.add(road, 5);

        System.out.println("Compose simulation");
        area.addContext(background);
        area.addContext(secondaryRole);
        area.addContext(player);
        window.addArea(area);
        prepareGameObjects();
        windows.add(window);


        System.out.println("Loading components schedulers");
        ComponentsScheduler<PhysicalRenderedComponent> physics
                = new ComponentsScheduler<>(new PhysicalRenderedComponentStateUpdater());
        ComponentsScheduler<Car> driver = new ComponentsScheduler<>(new BrownianDriver());
        driver.add(car);
        physics.add(car);
        ComponentsScheduler<RenderedComponent> drawer = new ComponentsScheduler<>(
                RenderedComponent::render);
        drawer.add(car);
        drawer.add(road);
        System.out.println("Simulation was loaded successfully");
    }

    private void prepareGameObjects() {
        renderedComponents.add(ComponentsFactory.aCar().build());
        renderedComponents.add(ComponentsFactory.aDirectRoad().build());
    }


}
