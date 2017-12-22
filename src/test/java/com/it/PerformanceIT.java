package com.it;


import com.fuzzygroup.SimpleEntity;
import com.fuzzygroup.exceptions.DisplayException;
import com.fuzzygroup.view2.ConsoleVisualisationEngine;
import com.fuzzygroup.view2.Display;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class PerformanceIT {

    public static final String PERF_TEST_DISPLAY_NAME = "car_simulator";
    private int fps = 60;
    private double canvasRotationChangeSpeed = 1;
    private double canvasScalingChangeSpeed = 1;
    private double canvasShiftChangeSpeed = 1;
    private final int objectAmount = 1000;
    private final int newObjectsPerSecond = 200;
    private final int deleteObjectsPerSecond = 100;
    private final boolean enableHistory = true;
    private final int objectsUpdatesPerSecond = 200;
    private final double maxAccelerationChange = 100;

    @Test
    public void test() throws InterruptedException {
        VisualisationLifecycleUtility utility = new VisualisationLifecycleUtility();
        utility.runOnce(PERF_TEST_DISPLAY_NAME,
                fps, enableHistory, objectAmount, objectsUpdatesPerSecond, maxAccelerationChange, newObjectsPerSecond,
                deleteObjectsPerSecond);

        TimeUnit.SECONDS.sleep(15);
        utility.getDisplay().shutdown();
    }
}
