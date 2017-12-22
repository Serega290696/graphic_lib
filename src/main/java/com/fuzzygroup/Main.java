package com.fuzzygroup;

import com.fuzzygroup.exceptions.DisplayException;
import com.fuzzygroup.view2.*;

import java.util.concurrent.TimeUnit;

public class Main {

    public static final String CAR_SIMULATOR_DISPLAY_NAME = "car_simulator";

    public static void main(String[] args) {
        // view
        Display display = new Display(CAR_SIMULATOR_DISPLAY_NAME,
                new LwjglEngine(), 800, 500);
        display.setFps(1);
//        display.setCanvasRotation(Math.PI/4);
//        display.setScale(2);
        display.disableHistoryLogging();
        display.drawFrame();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            display.startContinuousDrawing();
        } catch (DisplayException e) {
            e.printStackTrace();
        }

        // controller
        ApplicationController controller = new ApplicationController();
        controller.addOnExitHook(display::shutdown);

        // business logic
        SimpleEntity entity1 = new SimpleEntity();
        entity1.setSpeed(new ThreeVector(1, 1, 0));
        entity1.bindToDisplay(CAR_SIMULATOR_DISPLAY_NAME);
        SimpleEntity entity2 = new SimpleEntity();
        entity2.setSpeed(new ThreeVector(10, 2, 0));
        entity2.bindToDisplay(CAR_SIMULATOR_DISPLAY_NAME);
        SimpleEntity entity3 = new SimpleEntity();
        entity3.setSpeed(new ThreeVector(1, -1, 0));
        entity3.bindToDisplay(CAR_SIMULATOR_DISPLAY_NAME);

        new Thread(() -> {
            while (true) {
                entity1.calculateNextInstant();
                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                entity2.calculateNextInstant();
                try {
                    TimeUnit.MILLISECONDS.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(() -> {
            while (true) {
                entity3.calculateNextInstant();
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

//        stopVisualiseMomentWithDelayForSeconds(display, 3, 3);


    }

    private static void stopVisualiseMomentWithDelayForSeconds(Display display,
                                                               int delaySeconds, int pauseDurationSeconds) {
        try {
            TimeUnit.SECONDS.sleep(delaySeconds);

            System.out.println("Stop at: " + Context.uptimeMillis());
            display.setVisualisationMoment(Context.uptimeMillis());
            TimeUnit.SECONDS.sleep(pauseDurationSeconds);

            System.out.println("Resume at: " + Context.uptimeMillis());
            display.visualiseLastMoment();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
