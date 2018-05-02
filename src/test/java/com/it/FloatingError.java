package com.it;

import com.fuzzygroup.exceptions.DisplayException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FloatingError {

    public static final String PERF_TEST_DISPLAY_NAME = "car_simulator";
    private final boolean consistently = false;
    private final int times = 15;

    private int fps = 60;
    private double canvasRotationChangeSpeed = 1;
    private double canvasScalingChangeSpeed = 1;
    private double canvasShiftChangeSpeed = 1;
    private final int objectAmount = 1000;
    private final int newObjectsPerSecond = 200;
    private final int deleteObjectsPerSecond = 200;
    private final boolean enableHistory = true;
    private final int objectsUpdatesPerSecond = 200;
    private final double maxAccelerationChange = 100;

    @Test
    public void test() throws InterruptedException {
        System.out.println("Start object updating. Interval: "
                + 1000 / objectsUpdatesPerSecond + ".\n" +
                "Max acceleration change: " + maxAccelerationChange + ".\n" +
                "New objects per second: " + newObjectsPerSecond + ".\n" +
                "Delete objects per second: " + deleteObjectsPerSecond + ".\n" +
                "objectsUpdatesPerSecond: " + objectsUpdatesPerSecond + ".\n" +
                "");

        if (consistently) {
            for (int i = 0; i < times; i++) {
                VisualisationLifecycleUtility utility = new VisualisationLifecycleUtility();
                utility.runOnce(PERF_TEST_DISPLAY_NAME + i,
                        fps, enableHistory, objectAmount, objectsUpdatesPerSecond, maxAccelerationChange, newObjectsPerSecond,
                        deleteObjectsPerSecond);
                TimeUnit.SECONDS.sleep(3);
                utility.getDisplay().shutdown();
            }
        } else  {
            List<VisualisationLifecycleUtility> utilities = new ArrayList<>();
            for (int i = 0; i < times; i++) {
                VisualisationLifecycleUtility u = new VisualisationLifecycleUtility();
                utilities.add(u);
                u.runOnce(PERF_TEST_DISPLAY_NAME + i,
                        fps, enableHistory, objectAmount, objectsUpdatesPerSecond, maxAccelerationChange, newObjectsPerSecond,
                        deleteObjectsPerSecond);
            }
            TimeUnit.SECONDS.sleep(3);
            utilities.forEach(u -> u.getDisplay().shutdown());
        }
    }

}
