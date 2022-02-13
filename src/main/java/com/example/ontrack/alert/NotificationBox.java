package com.example.ontrack.alert;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NotificationBox {
    public static void display(String title, String message)
    {
        Stage window = new Stage();

        //Set up window
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        //Create label and button
        Label label = new Label();
        label.setText(message);
        label.setTextAlignment(TextAlignment.CENTER);
        Button okButton = new Button("confirm");
        okButton.setOnAction(actionEvent -> window.close());

        //Set container for label and button
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label,okButton);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
