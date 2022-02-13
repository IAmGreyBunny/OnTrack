package com.example.ontrack;

import javafx.animation.ScaleTransition;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.util.Duration;

public class Animation {
    public static void popUpChange(Parent parent, double impactFactor)
    {
        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setNode(parent);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setByX(impactFactor);
        scaleTransition.setByY(impactFactor);
        scaleTransition.setCycleCount(2);
        scaleTransition.setDuration(Duration.millis(400));
        scaleTransition.play();
    }
}
