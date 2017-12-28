package com.unit.vectors;

import com.fuzzygroup.view2.ThreeVector;
import org.junit.Test;

public class Direction {
    @Test
    public void testDirection() {
        vector(new ThreeVector(1, 10, 0)); // top

        vector(new ThreeVector(10, 1, 0)); // right
        vector(new ThreeVector(1, -10, 0)); // bottom
        vector(new ThreeVector(-10, -1, 0)); // left


    }

    private void vector(ThreeVector vector) {
        System.out.println(vector.toPolar().getDirection());
    }

}
