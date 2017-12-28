package com.fuzzygroup.view;

import com.fuzzygroup.exceptions.DisplayLifecycleException;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;

import com.fuzzygroup.view.input.InputHandler;
import com.fuzzygroup.view.input.KeyboardEvent;
import com.fuzzygroup.view.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Display {

    private final Logger logger = LoggerFactory.getLogger(Display.class);

    private static final int MAX_FPS = 1000;
    private static final int MIN_FPS = 1;
    private static final int DEFAULT_FPS = 25;
    private static final ThreeVector DEFAULT_SCALE = new ThreeVector(1, 1, 1);
    private static final ThreeVector DEFAULT_SHIFT = new ThreeVector(0, 0, 0);
    private static final double DEFAULT_CANVAS_ROTATION = 0;

    private final String qualifier;

    private Set<VisualRepresentationEntity> representationEntities
            = new CopyOnWriteArraySet<>();

    private VisualisationEngine engine;
    private boolean started;
    private boolean shutdown;
    private int fps; // must be less then 1000
    private long drawingIntervalMilliseconds;
    private ScheduledExecutorService scheduledExecutorService;
    private int visualisationMoment;
    private boolean visualizeLastMoment = true;
    private ThreeVector scale = DEFAULT_SCALE;
    private ThreeVector shift = DEFAULT_SHIFT;
    private double canvasRotationRadian = DEFAULT_CANVAS_ROTATION;
    private double deltaTime; // seconds
    private boolean historyEnable = true;
    private boolean interrupted;
    private int width;
    private int height;
    private int pixelsPerDistanceUnit;
    private InputHandler inputHandler;

    public Display(String qualifier, VisualisationEngine engine, int width, int height) {
        this.engine = engine;
        inputHandler = engine.createInputHandler();
        this.qualifier = qualifier;
        this.width = width;
        this.height = height;
        this.pixelsPerDistanceUnit = width / 100;
        Context.SINGLETON.register(this);
    }

    public InputHandler getInputHandler() {
        return inputHandler;
    }

    public void register(VisualRepresentationEntity representation) {
        representationEntities.add(representation);
    }

    public String getQualifier() {
        return qualifier;
    }

    public int shutdown() {
        if (!started) {
            // todo wrap exception?
            try {
                throw new DisplayLifecycleException("Can't shutdown: Context is not started");
            } catch (DisplayLifecycleException e) {
                e.printStackTrace();
            }
        }
        engine.shutdown();
        int returnCode;
        shutdown = true;
        scheduledExecutorService.shutdown();
        try {
            scheduledExecutorService.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            List<Runnable> rejected = scheduledExecutorService.shutdownNow();
            int rejectedThreadsAmount = rejected.size();
            representationEntities.clear();
            returnCode = rejectedThreadsAmount;
        }
        return returnCode;
    }

    // todo require to drawProcess in main thread (OpenGL requirement, should to configure)

    public synchronized void startContinuousDrawing()
            throws DisplayLifecycleException {
        if (started) {
            throw new DisplayLifecycleException("Repeated context startup detected: " +
                    "Visualisation context is already started");
        }
        if (shutdown) {
            throw new DisplayLifecycleException("Repeated context startup detected: " +
                    "Couldn't reuse Visualisation context, context status is 'shutdown'");
        }
        if (fps == 0) {
            fps = DEFAULT_FPS;
        }
        Runnable continuousDrawing = () -> {
            engine.init();
            engine.createDisplay(this);
            while (true) {
                long iterationStartTime = System.currentTimeMillis();
                engine.nextFrame();
                drawFrame();
                engine.afterFrame();
                long iterationDuration = System.currentTimeMillis() - iterationStartTime;
                drawingDurationMillisecondsPerSecond = iterationDuration;
                if (iterationDuration > drawingIntervalMilliseconds) {
//                System.out.printf(" [WARN] Visualisation overloading. " +
//                                "Interval between frames is %d, " +
//                                "while expected interval must be lower then %d",
//                        iterationDuration, drawingIntervalMilliseconds);
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(drawingIntervalMilliseconds - iterationDuration > 0 ?
                            drawingIntervalMilliseconds - iterationDuration : 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread drawing = new Thread(continuousDrawing);
        drawing.setName("drawer-daemon");
        drawing.setDaemon(true);
        drawing.start();
//        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
//        ScheduledFuture<?> scheduledFuture
//                = scheduledExecutorService.scheduleAtFixedRate(continuousDrawing, 0,
//                (long) (deltaTime * 1000), TimeUnit.MILLISECONDS);
//        enableExceptionHandler(scheduledFuture);
        enableReporter();
        started = true;
    }

//    private void enableExceptionHandler(ScheduledFuture<?> scheduledFuture) {
//        Thread exceptionHandler = new Thread(() -> {
//            try {
//                Object o = scheduledFuture.get();
//            } catch (InterruptedException | ExecutionException e) {
//                interrupted = true;
//                e.printStackTrace();
//                logger.error("Visualisation context failure. Try shutdown. . .");
//                shutdown();
//                logger.info("Shutdown success");
//            }
//        });
//        exceptionHandler.setName("continuous-drawer-exception-handler");
//        exceptionHandler.setDaemon(true);
//        exceptionHandler.start();
//    }

    private long drawingDurationMillisecondsPerSecond;

    private void enableReporter() {
        Runnable reporterTask = () -> {
            boolean enableReporting = true;
            while (enableReporting) {
                try {
//                    drawingDurationMillisecondsPerSecond = 0;
                    TimeUnit.MILLISECONDS.sleep(991);

                    if (drawingDurationMillisecondsPerSecond > 1000) {
                        logger.warn("Time waste on drawing: {} milliseconds per second. Objects amount: {}",
                                drawingDurationMillisecondsPerSecond, representationEntities.size());
                    } else {
                        logger.trace("Time waste on drawing: {} milliseconds per second. Objects amount: {}",
                                drawingDurationMillisecondsPerSecond, representationEntities.size());
                    }
                } catch (InterruptedException e) {
                    enableReporting = false;
                }
            }
        };
        Thread reporter = new Thread(reporterTask);
        reporter.setName("reporter-display-" + qualifier);
        reporter.setDaemon(true);
        reporter.start();
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setFps(int fps) {
        if (fps > 1000) {
            this.fps = MAX_FPS;
            System.err.printf(" [WARN] FPS must be less then %d. " +
                            "You try configure fps on %d. " +
                            "Established FPS is %d. ",
                    MAX_FPS, fps, this.fps);
        } else if (fps < 1) {
            this.fps = MIN_FPS;
            System.err.printf(" [WARN] FPS must be more then %d. " +
                            "You try configure fps on %d. " +
                            "Established FPS is %d. ",
                    MIN_FPS, fps, this.fps);
        } else {
            this.fps = fps;
        }
        drawingIntervalMilliseconds = 1000 / fps;
        deltaTime = (double) drawingIntervalMilliseconds / 1000;
    }


    //    private void calculateNextInstant() {
    // to private todo
    public void drawFrame() {

        if (visualizeLastMoment) {
            visualisationMoment = Context.uptimeMillis();
        }
//        System.out.println("Snapshot drawing");
        // todo rewrite to parallel stream after debugging
//        for (VisualRepresentationEntity r : representationEntities) {
//            if (historyEnable && !visualizeLastMoment) {
//                engine.drawProcess(r.getFloorStateAt(visualisationMoment), scale, shift, canvasRotationRadian);
//            } else {
//                engine.drawProcess(r.getLastState(), scale, shift, canvasRotationRadian);
//            }
//        }
        representationEntities
                .stream()
//                .map(Reference::get)
                .filter(Objects::nonNull)
                .forEach(o -> {
                    double sidesRatio = width / height;
                    if (historyEnable && !visualizeLastMoment) {
                        engine.drawProcess(o.getFloorStateAt(visualisationMoment), scale, shift, canvasRotationRadian,
                                pixelsPerDistanceUnit, sidesRatio);
                    } else {
                        engine.drawProcess(o.getLastState(), scale, shift, canvasRotationRadian,
                                pixelsPerDistanceUnit, sidesRatio);
                    }
                });
    }

    //    }
    public void setVisualisationMoment(int timestamp) {
        if (!historyEnable) {
            logger.error("History logging is disabled, " +
                    "therefore you cannot set visualise moment, " +
                    "you can see only last state.");
        } else {
            logger.trace("Visualise moment set to {}. Last moment is {}",
                    timestamp, Context.uptimeMillis());
            visualizeLastMoment = false;
            visualisationMoment = timestamp;
        }
    }

    //        }
    public void visualiseLastMoment() {
        visualizeLastMoment = true;
    }

    //            r.calculateNextInstant();

    //        for (VisualRepresentationEntity r : representationEntities) {
    public double getCanvasRotation() {
        return canvasRotationRadian;
    }

    ////        iter.
    public void setScale(double scale) {
        setScale(scale, scale, scale);
    }

    ////        ListIterator<Object> iter = l.listIterator();
    public void setScale(double xScale, double yScale, double zScale) {
        setScale(new ThreeVector(xScale, yScale, zScale));
    }

    ////
    public void setScale(ThreeVector scale) {
        this.scale = scale;
    }

    ////        List<Object> l = new ArrayList<>();
    public void setShift(double xShift, double yShift, double zShift) {
        setScale(new ThreeVector(xShift, yShift, zShift));
    }

    ////        representationEntities.iter
    public void setShift(ThreeVector shift) {
        this.shift = shift;
    }

    public void setCanvasRotation(double radian) {
        this.canvasRotationRadian = radian;
    }

    public ThreeVector getScale() {
        return scale;
    }

    public ThreeVector getShift() {
        return shift;
    }


    public double getDeltaTime() {
        return deltaTime;
    }

    public boolean isHistoryEnable() {
        return historyEnable;
    }

    public void disableHistoryLogging() {
        historyEnable = false;
        representationEntities
                .stream()
//                .map(Reference::get)
                .filter(Objects::nonNull)
                .forEach(VisualRepresentationEntity::disableHistoryLogging);

//        representationEntities.forEach(VisualRepresentationEntity::disableHistoryLogging);
    }

    public void enableHistoryLogging() {
        historyEnable = true;
        representationEntities
                .stream()
//                .map(Reference::get)
                .filter(Objects::nonNull)
                .forEach(VisualRepresentationEntity::enableHistoryLogging);

    }

    public void remove(VisualRepresentationEntity entity) {
        representationEntities.remove(entity);
    }

    public int getFps() {
        return fps;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPixelsPerDistanceUnit() {
        return pixelsPerDistanceUnit;
    }

    public void addAction(KeyboardEvent event) {
        inputHandler.createKeyboardEvent(event);
    }

    public void addAction(MouseEvent event) {
        inputHandler.createMouseEvent(event);
    }
    public double getMouseX() {
        return engine.getMouseX();
    }
    public double getMouseY() {
        return engine.getMouseY();
    }
}
