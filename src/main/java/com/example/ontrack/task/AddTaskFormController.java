package com.example.ontrack.task;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddTaskFormController implements Initializable {
    @FXML
    private ComboBox<String> taskTypeDropDown;

    @FXML
    private Button addNewTaskButton;


    @Override
    public void initialize(URL url, ResourceBundle rb){
        taskTypeDropDown.getItems().addAll(
                "Activity",
                "Exam",
                "Lesson",
                "Revision"
        );
    }

}
