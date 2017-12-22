package com.fuzzygroup.view2;

interface VisualisationEngine {
    void init();

    void createDisplay(int width, int height, String title);

    void shutdown();

    void nextFrame();

    void drawProcess(VisualRepresentationState representation,
                     ThreeVector scale, ThreeVector shift, double canvasRotationRadian, int pixelsPerDistanceUnit);

    void draw(VisualRepresentationState representation,
              ThreeVector scale, ThreeVector shift, double canvasRotationRadian, int pixelsPerDistanceUnit);

    boolean beyondVisibleArea(VisualRepresentationState representation,
                              ThreeVector scale, ThreeVector shift, double canvasRotationRadian);

}
