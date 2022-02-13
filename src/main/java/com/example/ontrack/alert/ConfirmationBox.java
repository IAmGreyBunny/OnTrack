package com.example.ontrack.alert;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmationBox {

    static boolean answer;

    public static boolean display(String title, String message) {
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
        Button cancelButton = new Button("cancel");
        okButton.setOnAction(actionEvent -> {
            answer = true;
            window.close();
        });
        cancelButton.setOnAction(actionEvent -> {
            answer = false;
            window.close();
        });

        //Set container for label and button
        VBox vBox = new VBox(10);
        HBox buttonRow = new HBox(10);
        buttonRow.getChildren().addAll(okButton,cancelButton);
        buttonRow.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(20));
        vBox.getChildren().addAll(label,buttonRow);
        vBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
