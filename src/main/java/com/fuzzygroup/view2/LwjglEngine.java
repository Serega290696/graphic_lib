package com.fuzzygroup.view2;

import com.fuzzygroup.view.Shape;
import com.fuzzygroup.view2.enums.ActionType;
import com.fuzzygroup.view2.enums.ColorEnum;
import com.fuzzygroup.view2.input.KeyboardEvent;
import com.fuzzygroup.view2.input.MouseEvent;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;

public class LwjglEngine extends AbstractVisualisationEngine {

    private ThreeVector lastMousePosition;

    private List<KeyboardEvent> keyboardEvents = new ArrayList<>();
    private List<MouseEvent> mouseEvents = new ArrayList<>();
    private double zoomDecreasingSpeed = 0;
    private int mouseDWheel;
    private volatile boolean keyboardEventsLocked = false;
    private final Object keyboardEventsMonitor = new Object();

    @Override
    public void init() {
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glClearDepth(1);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    }

    @Override
    public void createDisplay(int width, int height, String title) {
        boolean mouseGrabbed = false;
        glViewport(0, 0, width, height);
        glMatrixMode(GL_MODELVIEW);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, width, -width);
        glMatrixMode(GL_MODELVIEW);
        glClearColor(0.0f, 0.0f, 0.002f, 1);
        try {
            Display.setLocation(50, 20);
            Display.setDisplayMode(new DisplayMode(width, height));
            Display.create();
            Display.setVSyncEnabled(true);
            Display.setTitle(title);
            Keyboard.create();
            Mouse.create();
            Mouse.setGrabbed(mouseGrabbed);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shutdown() {
        Display.destroy();
        Keyboard.destroy();
        Mouse.destroy();
    }

    @Override
    public void nextFrame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
        // todo draw after frame?
        drawCursor(3, 20, 20, 1, 1, new ThreeVector(0, 0, 0));

        updateKeyboardEventsType();
        listenKeyboardEvent();
        updateMouseEventsType();
        listenMouseEvent();

        lastMousePosition = new ThreeVector(Mouse.getX(), Mouse.getY(), 0);
        Display.update();
    }

    @Override
    public void draw(VisualRepresentationState representation,
                     ThreeVector scale, ThreeVector shift, double canvasRotationRadian, int pixelsPerDistanceUnit) {
        // todo x3 memory consumption
        ThreeVector shifted = shift(representation.getPosition(), shift);
        ThreeVector shiftedScaled = scale(shifted, scale);
        ThreeVector shiftedScaled2 = scale(shiftedScaled, pixelsPerDistanceUnit);
        ThreeVector shiftedScaledRotated = rotate(shiftedScaled2, canvasRotationRadian);
        VisualRepresentationState transformed = representation.cloneState();
        transformed.setPosition(shiftedScaledRotated);
        // size
        ThreeVector scaledSize = scale(scale(representation.getSize(), scale), pixelsPerDistanceUnit);
        transformed.setSize(scaledSize);
        draw(transformed);
    }

    private void updateMouseEventsType() {
        for (MouseEvent event : mouseEvents) {
            boolean keysPressed = true;
            for (int key : event.getKeys()) {
//                    System.out.println("key = " + key);
                if (!Mouse.isButtonDown(key)) {
                    keysPressed = false;
                }
            }
            if (Arrays.stream(event.getKeys()).noneMatch(k -> k == Keyboard.KEY_LSHIFT)
                    && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                keysPressed = false;
            }
            if (Arrays.stream(event.getKeys()).noneMatch(k -> k == Keyboard.KEY_LCONTROL)
                    && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                keysPressed = false;
            }
            if ((event.getCurrentActionType() & ActionType.NO_ACTION) > 0 && keysPressed) {
                event.setCurrentActionType( ActionType.CLICKED);
            } else if ((event.getCurrentActionType() &  ActionType.CLICKED) > 0 && keysPressed) {
                event.setCurrentActionType( ActionType.PRESSED);
            }
            //every 200 millis
//            if ((event.getCurrentActionType() & ActionType.PRESSED) > 0
//                    && (event.getCurrentActionType() & ActionType.PRESSED_EVERY_200_MILLISECONDS) == 0
//                    && keysPressed) {
//                event.setCurrentActionType(
//                        event.getCurrentActionType() | ActionType.PRESSED_200_MILLISECONDS);
//            } else if ((event.getCurrentActionType() & ActionType.PRESSED_EVERY_200_MILLISECONDS) > 0 && keysPressed) {
//                event.setCurrentActionType(ActionType.PRESSED);
//            }
            //other
            if ((event.getCurrentActionType() &  ActionType.RELEASED) > 0 && keysPressed) {
                event.setCurrentActionType( ActionType.CLICKED);
            }
            if ((event.getCurrentActionType() &  ActionType.RELEASED) > 0 && !keysPressed) {
                event.setCurrentActionType( ActionType.NO_ACTION);
            }
            if ((event.getCurrentActionType() &  ActionType.PRESSED) > 0 && !keysPressed) {
                event.setCurrentActionType( ActionType.RELEASED);
            }
            if ((event.getCurrentActionType() &  ActionType.PRESSED) > 0 && keysPressed
                    && System.currentTimeMillis() - event.getLastActionTime() >= 10) {
                event.setCurrentActionType( ActionType.PRESSED_50_MILLISECONDS);
            }
            if ((event.getCurrentActionType() &  ActionType.PRESSED) > 0 && keysPressed
                    && System.currentTimeMillis() - event.getLastActionTime() >= 200) {
                event.setCurrentActionType( ActionType.PRESSED_200_MILLISECONDS);
                event.setCurrentActionType( ActionType.PRESSED_EVERY_200_MILLISECONDS);
            }
            if ((event.getCurrentActionType() &  ActionType.PRESSED) > 0 && keysPressed
                    && System.currentTimeMillis() - event.getLastActionTime() >= 1000) {
                event.setCurrentActionType( ActionType.PRESSED_ONE_SECOND);
            }
            if ((event.getCurrentActionType() &  ActionType.PRESSED) > 0 && keysPressed
                    && System.currentTimeMillis() - event.getLastActionTime() >= 2000) {
                event.setCurrentActionType( ActionType.PRESSED_TWO_SECONDS);
            }
        }
    }

    private void createMouseEvent(Runnable runnable, int type, int key) {
        MouseEvent event = new MouseEvent(runnable, type, key);
        mouseEvents.add(event);
    }

    private void updateKeyboardEventsType() {
        keyboardEventsLocked = true;
        for (KeyboardEvent event : keyboardEvents) {
            boolean keysPressed = true;
            for (int key : event.getKeys()) {
//                    System.out.println("key = " + key);
                if (!Keyboard.isKeyDown(key)) {
                    keysPressed = false;
                }
            }
            if (Arrays.stream(event.getKeys()).noneMatch(k -> k == Keyboard.KEY_LSHIFT)
                    && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                keysPressed = false;
            }
            if (Arrays.stream(event.getKeys()).noneMatch(k -> k == Keyboard.KEY_LCONTROL)
                    && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                keysPressed = false;
            }
            if ((event.getCurrentActionType() &  ActionType.NO_ACTION) > 0 && keysPressed) {
                event.setCurrentActionType( ActionType.CLICKED);
            } else if ((event.getCurrentActionType() &  ActionType.CLICKED) > 0 && keysPressed) {
                event.setCurrentActionType( ActionType.PRESSED);
            }
            //other
            if ((event.getCurrentActionType() &  ActionType.RELEASED) > 0 && keysPressed) {
                event.setCurrentActionType( ActionType.CLICKED);
            }
            if ((event.getCurrentActionType() &  ActionType.RELEASED) > 0 && !keysPressed) {
                event.setCurrentActionType( ActionType.NO_ACTION);
            }
            if ((event.getCurrentActionType() &  ActionType.PRESSED) > 0 && !keysPressed) {
                event.setCurrentActionType( ActionType.RELEASED);
                event.resetLastIntervalTrigerMilliseconds();
            }
            if ((event.getCurrentActionType() &  ActionType.PRESSED) > 0 && keysPressed
                    && System.currentTimeMillis() - event.getLastActionTime() >= 10) {
                event.setCurrentActionType( ActionType.PRESSED_50_MILLISECONDS);
            }
            if ((event.getCurrentActionType() &  ActionType.PRESSED) > 0 && keysPressed
                    && System.currentTimeMillis() - event.getLastActionTime() >= 200) {
                event.setCurrentActionType( ActionType.PRESSED_200_MILLISECONDS);
            }
            if ((event.getCurrentActionType() &  ActionType.PRESSED) > 0 && keysPressed
                    && System.currentTimeMillis() - event.getLastActionTime() >= 1000) {
                event.setCurrentActionType( ActionType.PRESSED_ONE_SECOND);
            }
            if ((event.getCurrentActionType() &  ActionType.PRESSED) > 0 && keysPressed
                    && System.currentTimeMillis() - event.getLastActionTime() >= 2000) {
                event.setCurrentActionType( ActionType.PRESSED_TWO_SECONDS);
            }
            //every 200 millis
//            if(System.currentTimeMillis() - event.getLastIntervalTrigerMilliseconds()> 100) {
//                System.out.println("");
//            }
//            if(keysPressed) {
//                System.out.println(System.currentTimeMillis() - event.getLastIntervalTrigerMilliseconds());
//            }
            if (
//                    (event.getCurrentActionType() & ActionType.PRESSED) > 0
//                    && (event.getCurrentActionType() & ActionType.PRESSED_EVERY_200_MILLISECONDS) == 0
                    System.currentTimeMillis() - event.getLastIntervalTrigerMilliseconds() >= 100
                            && keysPressed
                    ) {
                event.setCurrentActionType(
                        event.getCurrentActionType() |  ActionType.PRESSED_EVERY_200_MILLISECONDS);
                event.resetLastIntervalTrigerMilliseconds();
            } else if ((event.getCurrentActionType() &  ActionType.PRESSED_EVERY_200_MILLISECONDS) > 0) {
                event.setCurrentActionType( ActionType.PRESSED);
            }
//            if (event.getCurrentActionType() != 1) {
//                System.out.println(event.getCurrentActionType());
//            }
//            if ((event.getCurrentActionType() & ActionType.PRESSED_EVERY_200_MILLISECONDS) > 0) {
//                System.out.println("PRESSED_EVERY_200_MILLISECONDS");
//            }
        }
//        if (keyboardEventsTryToGetAccess) {
//        System.out.println("keyboardEventsMonitor");
        synchronized (keyboardEventsMonitor) {
            keyboardEventsMonitor.notifyAll();
            keyboardEventsLocked = false;
        }
//        }
    }

    private void createKeyboardEvent(Runnable r, int type, int... triggerKey) {
        while (keyboardEventsLocked) {
            synchronized (keyboardEventsMonitor) {
                while (keyboardEventsLocked) {
                    try {
                        keyboardEventsMonitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        KeyboardEvent event = new KeyboardEvent(r, type, triggerKey);
        keyboardEvents.add(event);
    }

    private void listenKeyboardEvent() {
        for (KeyboardEvent event : keyboardEvents) {
            if (Arrays.stream(event.getKeys()).allMatch(Keyboard::isKeyDown)) {
            }
            if (//Arrays.stream(event.getKeys()).allMatch(Keyboard::isKeyDown)
                // &&
                    (event.getTriggerActionType() & event.getCurrentActionType()) > 0) {
//                System.out.println(event);
                event.run();
            }
        }
    }

    private void listenMouseEvent() {
        for (MouseEvent event : mouseEvents) {
            if (Arrays.stream(event.getKeys()).allMatch(Mouse::isButtonDown)
                    && event.getTriggerActionType() == event.getCurrentActionType()) {
                event.run();
            }
            mouseDWheel = Mouse.getDWheel();
            if (
                    Arrays.stream(event.getKeys()).allMatch(k -> k == 3)
                            &&
                            (mouseDWheel != 0 || zoomDecreasingSpeed != 0)
//                    && event.getTriggerActionType() == event.getCurrentActionType()
                    ) {
                event.run();
            }
        }
    }

    private void draw(VisualRepresentationState representation) {
        glDisable(GL_TEXTURE_2D);
        double x = representation.getPosition().getX();
        double y = representation.getPosition().getY();
        double sx = representation.getSize().getX();
        double sy = representation.getSize().getY();
        double rotation = representation.getRotation();
        ColorEnum color = representation.getColors();
        double opacity = representation.getOpacity();
        switch (representation.getShape()) {
            case RECT:
                Drawer.rect(x, y, sx, sy, rotation, color, opacity);
                break;
            case TRIANGLE:
                Drawer.triangle(x, y, sx, sy, rotation, color, opacity);
                break;
            case CIRCLE:
                Drawer.circle(x, y, sx, sy, rotation, color, opacity);
                break;
            case STAR:
                Drawer.star(x, y, sx, sy, rotation, color, opacity);
                break;
            case FON:
                break;
            default:
                break;
        }
    }

    private void draw(Shape shape, double x, double y, double sx, double sy, double rotation,
                      ColorEnum color, double opacity) {
        glDisable(GL_TEXTURE_2D);
//        x = representation.getPosition().getX();
//        y = representation.getPosition().getY();
//        sx = representation.getSize().getX();
//        sy = representation.getSize().getY();
//        rotation = representation.getRotation();
//        color = representation.getColors();
//        opacity = representation.getOpacity();
        switch (shape) {
            case RECT:
                Drawer.rect(x, y, sx, sy, rotation, color, opacity);
                break;
            case TRIANGLE:
                Drawer.triangle(x, y, sx, sy, rotation, color, opacity);
                break;
            case CIRCLE:
                Drawer.circle(x, y, sx, sy, rotation, color, opacity);
                break;
            case STAR:
                Drawer.star(x, y, sx, sy, rotation, color, opacity);
                break;
            case FON:
                break;
            default:
                break;
        }
    }

    private ThreeVector shift(ThreeVector position, ThreeVector shift) {
        return position.plus(shift);
    }

    private ThreeVector scale(ThreeVector position, ThreeVector scale) {
        return position.coordinatewiseMultiplication(scale);
    }

    private ThreeVector scale(ThreeVector position, double factor) {
        return position.multiple(factor);
    }

    private ThreeVector rotate(ThreeVector position, double radian) {
        ThreeVector.PolarCoordinateSystemVector polarVector = position.toPolar();
        polarVector.setRadian(polarVector.getRadian() + radian);
        return polarVector.toFlatCartesianVector();
    }

    @Override
    public boolean beyondVisibleArea(VisualRepresentationState representation,
                                     ThreeVector scale, ThreeVector shift, double canvasRotationRadian) {
        return false;
    }

    public void drawCursor(int cursorType, float cursorWidth, float cursorHeight, float em, float DEFAULT_EM, ThreeVector shift) {

        switch (cursorType) {
            case 1:
                draw(Shape.TRIANGLE,
                        Mouse.getX() / em + shift.getX() + cursorWidth / em / 3,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.getY() + cursorHeight / em / 3,
                        cursorHeight / em, cursorWidth / em, 115, ColorEnum.BLUE, 1);
                break;
            case 2:
                draw(Shape.TRIANGLE,
                        Mouse.getX() / em + shift.getX(),
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.getY(),
                        cursorHeight / em, cursorWidth / em, 0, ColorEnum.BLUE, 1);
                draw(Shape.TRIANGLE,
                        Mouse.getX() / em + shift.getX(),
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.getY(),
                        cursorHeight / em, cursorWidth / em, 90, ColorEnum.BLUE, 1);
                draw(Shape.TRIANGLE,
                        Mouse.getX() / em + shift.getX(),
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.getY(),
                        cursorHeight / em, cursorWidth / em, 180, ColorEnum.BLUE, 1);
                draw(Shape.TRIANGLE,
                        Mouse.getX() / em + shift.getX(),
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.getY(),
                        cursorHeight / em, cursorWidth / em, -90, ColorEnum.BLUE, 1);
                break;
            case 3:
                double animationCycleMilliseconds = 1000;
                double cursorAnimationPhase = animationCycleMilliseconds - System.currentTimeMillis() % animationCycleMilliseconds;
                double maxCursorShift = 3;
                double cursorShiftX = 1.2 + (maxCursorShift - 1.2) * Math.abs(1 - cursorAnimationPhase / animationCycleMilliseconds * 2);
                double cursorShiftY = 1.6 + (maxCursorShift - 1.4) * Math.abs(1 - cursorAnimationPhase / animationCycleMilliseconds * 2);
                double trWidth = cursorWidth / em / 3;
                double trHeight = cursorHeight / em / 3;
                draw(Shape.TRIANGLE,
                        Mouse.getX() / em + shift.getX() - cursorWidth / em / cursorShiftX,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.getY() + 0,
                        trHeight, trWidth, 0, ColorEnum.BLUE, 1);
                draw(Shape.TRIANGLE,
                        Mouse.getX() / em + shift.getX() - 0,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.getY() + cursorHeight / em / cursorShiftY,
                        trHeight, trWidth, 90, ColorEnum.BLUE, 1);
                draw(Shape.TRIANGLE,
                        Mouse.getX() / em + shift.getX() + cursorWidth / em / cursorShiftX,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.getY() + 0,
                        trHeight, trWidth, 180, ColorEnum.BLUE, 1);
                draw(Shape.TRIANGLE,
                        Mouse.getX() / em + shift.getX() - 0,
                        100 / em * DEFAULT_EM - Mouse.getY() / em + shift.getY() - cursorHeight / em / cursorShiftY,
                        trHeight, trWidth, -90, ColorEnum.BLUE, 1);
                break;
        }
    }

    private static class Drawer {


        private static void rect(double x, double y, double sx, double sy, double rotate,
                                 ColorEnum color, double opacity) {
            glPushMatrix();
            {
                chooseColor(color, opacity);
                glTranslated(x, y, 0);
                glRotated((float) (-rotate), 0, 0, 1);
                glEnable(GL_POLYGON_SMOOTH);
                glBegin(GL_QUADS);
                {
                    glVertex2d(-sx / 2, -sy / 2);
                    glVertex2d(-sx / 2, sy / 2);
                    glVertex2d(sx / 2, sy / 2);
                    glVertex2d(sx / 2, -sy / 2);
                }
                glEnd();
            }
            glPopMatrix();
        }

        private static void circle(double x, double y, double sx, double sy, double rotation,
                                   ColorEnum color, double opacity) {
            glPushMatrix();
            {
                chooseColor(color, opacity);
                glTranslated(x, y, 0);
                if (sx < 200) {
                    if (sx == 0) {
                        sx = 1;
                    }
                    if (sy == 0) {
                        sy = 1;
                    }
                    glEnable(GL_POINT_SMOOTH);
                    glPointSize((float) sx);
                    glBegin(GL_POINTS);
                    glVertex2d(0, 0); // ������ �����
                    glEnd();
                } else {
                    glBegin(GL_POLYGON);
                    {
                        int amountPoints = (int) (0.1 * sx);
                        for (int i = 0; i <= amountPoints; i++) {
                            glVertex2d(sx / 2 * Math.cos((float) i / amountPoints * 2 * Math.PI), sy / 2 * Math.sin((float) i / amountPoints * 2 * Math.PI));
                        }
                    }
                    glEnd();
                }
            }
            glPopMatrix();
        }

        private static void star(double x, double y, double sx, double sy, double rotation,
                                 ColorEnum color, double opacity) {
            glPushMatrix();
            {
                chooseColor(color, opacity);
                glTranslated(x, y, 0);
                glRotated(-rotation, 0, 0, 1);
                if (sx < 8) {
                    if (sx == 0) {
                        sx = 1;
                    }
                    glEnable(GL_POINT_SMOOTH);
                    glPointSize((float) sx);
                    glBegin(GL_POINTS);
                    glVertex2d(0, 0);
                    glEnd();
                } else {
                    glBegin(GL_POLYGON);
                    {
                        int amountPoints = 18;
//                int amountPoints = (int) (0.1 * sx);
                        for (int i = 0; i <= amountPoints; i++) {
                            double factor = 1;
                            if (i % 2 == 0) {
                                factor = (0.5 + 0.0 * Math.random());
                            }
                            glVertex2d(factor * sx / 2 * Math.cos((float) i / amountPoints * 2 * Math.PI), factor * sy / 2 * Math.sin((float) i / amountPoints * 2 * Math.PI));
                        }
                    }
                    glEnd();
                }
            }
            glPopMatrix();
        }

        private static void triangle(double x, double y, double sx, double sy, double rotate,
                                     ColorEnum color, double opacity) {
            glPushMatrix();
            {

                double side = sx;
                chooseColor(color, opacity);
                glTranslated(x, y, 0);
                glRotated(-rotate, 0, 0, 1);
                glEnable(GL_POLYGON_SMOOTH);
                glBegin(GL_TRIANGLES);
                {
                    glVertex2d(-sx / 2, -sy / 2);
                    glVertex2d(-sx / 2, sy / 2);
                    glVertex2d((float) ((Math.pow(3, 0.5f) - 1) / 2 * sx), 0);
                }
                glEnd();
            }
            glPopMatrix();
        }


        private static void chooseColor(ColorEnum color, double opacity) {
            switch (color) {
                case WHITE:
                    glColor4d(0.8f, 0.8f, 0.8f, opacity);
                    break;
                case RED:
                    glColor4d(1.0f, 0.0f, 0.0f, opacity);
                    glColor4d(0.733f, 0.223f, 0.168f, opacity);
                    break;
                case GREEN:
                    glColor4d(0.478f, 0.737f, 0.345f, opacity);
                    break;
                case BLUE:
                    glColor4d(0.247f, 0.494f, 1.0f, opacity);
                    break;
                case BLACK:
                    glColor4d(0f, 0.0f, 0.0f, opacity);
                    break;
            }
        }
    }
}
