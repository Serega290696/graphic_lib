package com.ngeneration.graphic.engine.utils;

import java.util.Collection;

public class AssertUtils {
    public void notNull(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
    }

    public void isNull(Object o) {
        if (o != null) {
            throw new IllegalStateException();
        }
    }

    public void isEmpty(String string) {
        if (string == null || string.isEmpty()) {
            throw new IllegalStateException();
        }
    }

    public void isEmpty(Collection collection) {
        if (collection == null || collection.size() == 0) {
            throw new IllegalStateException();
        }
    }
}
