package com.example.ontrack.task.form;

import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddLessonFormController implements IBackButton, Initializable {
    @FXML
    Button backButton;

    @FXML
    ComboBox<String> repetitionRuleDropDown;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        repetitionRuleDropDown.getItems().add("Create New");

    }

    @FXML
    public void onBackButtonClicked()
    {
        Parent form;
        BorderPane borderPane = (BorderPane) backButton.getScene().getRoot();
        try {
            form = FXMLLoader.load(Main.class.getResource("task/form/AddTaskForm.fxml"));
            borderPane.setLeft(form);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
