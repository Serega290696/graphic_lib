package com.fuzzygroup.view2;

import com.fuzzygroup.exceptions.DisplayException;
import com.fuzzygroup.view.Shape;
import com.fuzzygroup.view2.enums.ColorEnum;

import java.util.Map;

public abstract class VisualRepresentationEntity extends VisualRepresentationState {

    private final StateHistory history;
    protected String displayQualifier;
    private Display display;
    private boolean enableHistoryLogging = true;

    public VisualRepresentationEntity(ThreeVector position, ThreeVector speed, ThreeVector acceleration, ThreeVector size,
                                      Shape shapes, ColorEnum colors, boolean visible) {
        this(position, speed, acceleration, size, shapes, colors, visible, new StateHistory());
    }

    public VisualRepresentationEntity(ThreeVector position, ThreeVector speed, ThreeVector acceleration, ThreeVector size,
                                      Shape shapes, ColorEnum colors,
                                      boolean visible, StateHistory history) {
        super(position, speed, acceleration, size, shapes, colors, visible);
        this.history = history;
//        this.display = getBoundDisplay();
        if (display != null) {
            this.enableHistoryLogging = display.isHistoryEnable();
//            registration(); // todo?
        }
    }

    public void bindToDisplay(String displayQualifier) {
        try {
            display = Context.SINGLETON.getByName(displayQualifier);
            display.register(this);
            saveStateToHistory();
//            if (display != null) {
//                display.register(this);
//            } else {
//                throw new DisplayException("No display defined");
//            }
        } catch (DisplayException e) {
            e.printStackTrace();
        }
    }

    public void stopVisualise() {
        display.remove(this);
    }

    protected abstract Display getBoundDisplay();

    public void calculateNextInstant() {
        //todo
        recomputeState();
        saveStateToHistory();
    }

    private void recomputeState() {
        speed = speed.plus(acceleration.multiple(display.getDeltaTime()));
        position = position.plus(speed.multiple(display.getDeltaTime()));
    }

    private void saveStateToHistory() {
        if (!enableHistoryLogging) {
            history.clearAndSave(Context.uptimeMillis(), cloneState());
        }
        history.addStateAt(Context.uptimeMillis(), cloneState());
    }

    public void moveTo(double x, double y, double z) {
        // todo is it ok?
        position = new ThreeVector(x, y, z);
    }

    public void shiftOn(double deltaX, double deltaY, double deltaZ) {
        // todo is it ok?
        moveTo(position.getX() + deltaX, position.getY() + deltaY, position.getZ() + deltaZ);
    }

    public VisualRepresentationState getFloorStateAt(int timestamp) {
        return history.getFloorStateAt(timestamp);
    }
    public VisualRepresentationState getLastState() {
        return history.getLastState();
    }

    public Shape getCompositeShape() {
        return shapes;
    }


    //todo make display private?
    public Display getDisplay() {
        return display;
    }

    public boolean isEnableHistoryLogging() {
        return enableHistoryLogging;
    }

    public void enableHistoryLogging() {
        this.enableHistoryLogging = true;
    }

    public void disableHistoryLogging() {
        this.enableHistoryLogging = false;
    }

    public StateHistory getHistory() {
        return history;
    }


}
