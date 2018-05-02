package com.ngeneration.graphic.engine.physics;

import com.ngeneration.graphic.engine.view.RenderedComponent;

import java.util.function.Consumer;

public interface RenderedComponentStateUpdater<T extends RenderedComponent> extends Consumer<T> {

}
