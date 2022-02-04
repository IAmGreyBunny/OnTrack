package com.example.ontrack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("authentication/AuthenticationPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("OnTrack");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        /*
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("task/AddRepetitionRuleForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("OnTrack");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();*/
    }

    public static void main(String[] args) {
        launch();
    }
}