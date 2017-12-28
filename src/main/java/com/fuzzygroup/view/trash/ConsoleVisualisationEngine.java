package com.fuzzygroup.view2;

public class ConsoleVisualisationEngine extends AbstractVisualisationEngine {

    @Override
    public void init() {

    }

    @Override
    public void createDisplay(Display display) {

    }

    @Override
    public InputHandler createInputHandler() {
        return null;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void nextFrame() {

    }

    @Override
    public void afterFrame() {

    }

    @Override
    public void draw(VisualRepresentationState representation,
                     ThreeVector scale, ThreeVector shift, double canvasRotationRadian, int pixelPerDistanceUnit, double sidesRatio) {
        // todo x3 memory consumption
        // but only position change
        ThreeVector shifted = shift(representation.getPosition(), shift);
        ThreeVector shiftedScaled = scale(scale(shifted, scale), pixelPerDistanceUnit);
        ThreeVector shiftedScaledRotated = rotate(shiftedScaled, canvasRotationRadian);
        VisualRepresentationState clone = representation.cloneState();
        clone.setPosition(shiftedScaledRotated);
//        System.out.println("\t" + clone);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean beyondVisibleArea(VisualRepresentationState representation) {
        return false;
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
    public double getMouseX() {
        return 0;
    }

    @Override
    public double getMouseY() {
        return 0;
    }
}