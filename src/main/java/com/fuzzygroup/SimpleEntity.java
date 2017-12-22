package com.fuzzygroup;

import com.fuzzygroup.exceptions.DisplayException;
import com.fuzzygroup.view.Shape;
import com.fuzzygroup.view2.*;
import com.fuzzygroup.view2.enums.ColorEnum;

import java.util.HashMap;
import java.util.Map;

public class SimpleEntity extends VisualRepresentationEntity {

    public static final String DISPLAY_QUALIFIER_NAME = Main.CAR_SIMULATOR_DISPLAY_NAME;


    public SimpleEntity() {
        super(new ThreeVector(0, 0, 0), new ThreeVector(0, 0, 0), new ThreeVector(0, 0, 0),
                new ThreeVector(3, 3, 3),
                Shape.RECT, ColorEnum.BLUE, true);
    }

//    public SimpleEntity(String displayQualifier) {
//        super(new ThreeVector(0, 0, 0), new ThreeVector(0, 0, 0), new ThreeVector(0, 0, 0),
//                new ThreeVector(3, 3, 3),
//                new HashMap<>(), new HashMap<>(), true);
//        this.displayQualifier = displayQualifier;
//    }

    public SimpleEntity(ThreeVector position, ThreeVector speed, ThreeVector acceleration, ThreeVector size,
                        Shape shapes, ColorEnum colors, boolean visible) {
        super(position, speed, acceleration, size, shapes, colors, visible);
    }

    @Override
    protected Display getBoundDisplay() {
        try {
            if (displayQualifier != null && !"".equals(displayQualifier)) {
                return Context.SINGLETON.getByName(displayQualifier);
            } else {
                return Context.SINGLETON.getByName(DISPLAY_QUALIFIER_NAME);
            }
        } catch (DisplayException e) {
            e.printStackTrace();
        }
        // todo ugly
        return null;
    }

}
