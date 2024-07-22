package app.harikarthik.nutrifusion.utils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class LoadingAnimation extends Pane {
    private static final int NUM_DOTS = 12;
    private static final double DOT_RADIUS = 5;
    private static final double ANIMATION_RADIUS = 30;
    private static final double ROTATION_DURATION = 1000; // in milliseconds

    private Timeline timeline;

    public LoadingAnimation() {
        for (int i = 0; i < NUM_DOTS; i++) {
            Circle dot = new Circle(DOT_RADIUS, Color.rgb(216, 60, 60));
            double angle = 2 * Math.PI / NUM_DOTS * i;

            // Calculate distance to corner based on angle (using properties of inscribed square)
            double cornerOffset = ANIMATION_RADIUS / Math.sqrt(2) * Math.tan(angle + Math.PI / 4);

            double translateX, translateY;
            // Adjust position based on quadrant (use cosine and sine for reference)
            if (i % 2 == 0) { // Even positions (0, 2, ...) - right side
                translateX = ANIMATION_RADIUS + cornerOffset;
                translateY = (Math.cos(angle) > 0) ? cornerOffset : -cornerOffset;
            } else { // Odd positions (1, 3, ...) - left side
                translateX = -ANIMATION_RADIUS - cornerOffset;
                translateY = (Math.cos(angle) > 0) ? cornerOffset : -cornerOffset;
            }

            dot.setTranslateX(translateX);
            dot.setTranslateY(translateY);
            getChildren().add(dot);
        }

        timeline = new Timeline(new KeyFrame(Duration.millis(ROTATION_DURATION / NUM_DOTS), event -> {
            setRotate(getRotate() + 360.0 / NUM_DOTS);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void start() {
        timeline.play();
    }

    public void stop() {
        timeline.stop();
    }
}