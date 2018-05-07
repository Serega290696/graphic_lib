package com.ngeneration;

import com.fuzzygroup.exceptions.DisplayLifecycleException;
import com.fuzzygroup.view.*;
import com.fuzzygroup.view.enums.ColorEnum;
import com.ngeneration.ai.BrownianDriver;
import com.ngeneration.custom_rendered_components.Car;
import com.ngeneration.custom_rendered_components.Road;
import com.ngeneration.graphic.engine.ArtistTmp;
import com.ngeneration.graphic.engine.ComponentsScheduler;
import com.ngeneration.graphic.engine.Corner;
import com.ngeneration.graphic.engine.Vector;
import com.ngeneration.graphic.engine.commands.Command;
import com.ngeneration.graphic.engine.commands.QuitCommand;
import com.ngeneration.graphic.engine.drawablecomponents.PhysicalRenderedComponent;
import com.ngeneration.graphic.engine.physics.PhysicalComponentStateUpdater;
import com.ngeneration.graphic.engine.view.DrawArea;
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
        windows.add(window);
        DrawArea area = window.allocateArea(Corner.TOP_LEFT, 0.5, 0.5);
        DrawContext background = new DrawContext("background");
        DrawContext secondaryRole = new DrawContext("player");
        DrawContext player = new DrawContext("player");

        System.out.println("Loading simulation components");
        Car car = ComponentsFactory.aCar().build();
        Road road = ComponentsFactory.aDirectRoad().build();
        player.put(10, car);
        secondaryRole.put(5, road);

        System.out.println("Compose simulation");
        area.addContext(background);
        area.addContext(secondaryRole);
        area.addContext(player);
        prepareGameObjects();


        System.out.println("Loading components schedulers");
        ComponentsScheduler<PhysicalRenderedComponent> physics
                = new ComponentsScheduler<>(new PhysicalComponentStateUpdater());
        ComponentsScheduler<Car> driver = new ComponentsScheduler<>(new BrownianDriver());
//        ComponentsScheduler<RenderedComponent> drawer = new ComponentsScheduler<>(
//                RenderedComponent::render);
        driver.add(car);
        physics.add(car);

        ArtistTmp.INSTANCE.add(car);
        ArtistTmp.INSTANCE.add(road);
//        ArtistTmp.INSTANCE.start();


        Display display = ArtistTmp.INSTANCE.createDisplay();
        display.setFps(30);
        display.disableHistoryLogging();
        display.register(toEntity(car));
        display.register(toEntity(road));
        try {
            display.startContinuousDrawing();
        } catch (DisplayLifecycleException e) {
            e.printStackTrace();
        }
        System.out.println("Simulation was loaded successfully");
    }

    private static VisualRepresentationEntity toEntity(RenderedComponent component) {
        return new VisualRepresentationEntity(
                toThreeVector(component.getPosition()),
                toThreeVector(component.getPosition()),
                toThreeVector(component.getPosition()),
                toThreeVector(component.getSize()),
                component.getShapes(),
                component.getColors(),
                true
        ) {
            @Override
            public void recomputeState() {

            }
        };
    }

    private static final ThreeVector nilVector = new ThreeVector(0, 0, 0);
    private static final ThreeVector oneVector = new ThreeVector(1, 1, 1);

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

    private static ThreeVector toThreeVector(Vector vector) {
        if (vector != null) {
            return new ThreeVector(vector.getX(), vector.getY(), 0);
        }
        return new ThreeVector(0, 0, 0);
    }

    private void initGraphicEngine() {

    }


    public void finish() {
        for (Window window : windows) {
            window.close();
        }
        windows.clear();
    }


    private void prepareGameObjects() {
        renderedComponents.add(ComponentsFactory.aCar().build());
        renderedComponents.add(ComponentsFactory.aDirectRoad().build());
    }


}
