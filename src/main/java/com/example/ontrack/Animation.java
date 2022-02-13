package com.example.ontrack;

import javafx.scene.Node;
import javafx.scene.Parent;

public class Animation {
    public static void popUpChange(Parent parent, double impactFactor)
    {
        parent.setScaleX(impactFactor);
        parent.setScaleY(impactFactor);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        parent.setScaleX(impactFactor);
        parent.setScaleY(impactFactor);
    }
}
