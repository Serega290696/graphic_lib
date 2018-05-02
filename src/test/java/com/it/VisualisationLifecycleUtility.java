package com.it;

//import com.fuzzygroup.SimpleEntity;
//import com.fuzzygroup.view2.ThreeVector;
import com.fuzzygroup.entities.SimpleEntity;
import com.fuzzygroup.exceptions.DisplayException;
import com.fuzzygroup.view.Display;
import com.fuzzygroup.view.ThreeVector;
import com.fuzzygroup.view.lwjgl_engine.LwjglEngine;
//import com.fuzzygroup.view2.ConsoleVisualisationEngine;
//import com.fuzzygroup.view2.Display;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class VisualisationLifecycleUtility {
    private Display display;
    private List<SimpleEntity> entities;

    public Display getDisplay() {
        return display;
    }

    public List<SimpleEntity> getEntities() {
        return entities;
    }

    public void runOnce(String displayName, int fps, boolean enableHistory,
                        int objectAmount, int objectsUpdatesPerSecond, double maxAccelerationChange,
                        int newObjectsPerSecond, int deleteObjectsPerSecond) {
        display = new Display(displayName,
                new LwjglEngine(), 800, 500);
        display.setFps(fps);
//        display.setCanvasRotation(Math.PI/4);
//        display.setScale(2);
        if (enableHistory) {
            display.enableHistoryLogging();
        } else {
            display.disableHistoryLogging();
        }
        try {
            display.startContinuousDrawing();
        } catch (DisplayException e) {
            e.printStackTrace();
            display.shutdown();
        }


        // business logic
        entities = initEntities(displayName, objectAmount);
        startObjectsUpdatingThread(entities,
                objectsUpdatesPerSecond, maxAccelerationChange);
        startObjectsAdditionThread(displayName, entities, newObjectsPerSecond);
        startObjectsDeletionThread(entities, deleteObjectsPerSecond);
    }

    public void startObjectsDeletionThread(List<SimpleEntity> entities, int deleteObjectsPerSecond) {
        Thread objectDeletion = new Thread(() -> {
            boolean stopTest = false;
            while (!stopTest) {
                SimpleEntity entity = entities.get((int) (entities.size() / 2 * Math.random()));
                entity.stopVisualise();
                entities.remove(entity);
                try {
                    TimeUnit.MILLISECONDS.sleep((long) (1 + 1000d / deleteObjectsPerSecond));
                } catch (InterruptedException e) {
                    stopTest = true;
                }
            }
        });
        objectDeletion.start();
    }

    public void startObjectsAdditionThread(String displayQualifier, List<SimpleEntity> entities, int newObjectsPerSecond) {
        Thread objectAddition = new Thread(() -> {
            boolean stopTest = false;
            while (!stopTest) {
                SimpleEntity entity = new SimpleEntity();
                entity.bindToDisplay(displayQualifier);
                entities.add(entity);
                try {
                    TimeUnit.MILLISECONDS.sleep((long) (1 + 1000d / newObjectsPerSecond));
                } catch (InterruptedException e) {
                    stopTest = true;
                }
            }
        });
        objectAddition.start();
    }

    public void startObjectsUpdatingThread(List<SimpleEntity> entities,
                                           int objectsUpdatesPerSecond,
                                           double maxAccelerationChange) {
        Thread objectUpdating = new Thread(() -> {
            Random random = new Random();
            boolean stopTest = false;
            while (!stopTest) {
                entities.forEach(e -> e.setAcceleration(
                        e.getAcceleration().plus(
                                new ThreeVector(random.nextDouble() * maxAccelerationChange,
                                        random.nextDouble() * maxAccelerationChange,
                                        random.nextDouble() * maxAccelerationChange
                                )
                        )
                ));
                try {
                    TimeUnit.MILLISECONDS.sleep(1000 / objectsUpdatesPerSecond);
                } catch (InterruptedException e) {
                    stopTest = true;
                }
            }
            System.out.println("Stop object updating");
        });

        objectUpdating.start();
    }

    public List<SimpleEntity> initEntities(String displayQualifier, int objectAmount) {
        List<SimpleEntity> entities = Collections.synchronizedList(new ArrayList<>());
        for (int i = 0; i < objectAmount; i++) {
            SimpleEntity e = new SimpleEntity();
            e.bindToDisplay(displayQualifier);
            e.setSize(new ThreeVector(3, 3, 3));
            entities.add(e);
        }
        return entities;
    }
}
