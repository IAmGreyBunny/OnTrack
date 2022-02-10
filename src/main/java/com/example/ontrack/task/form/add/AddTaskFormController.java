package com.example.ontrack.task.form.add;

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

public class AddTaskFormController implements Initializable {

    @FXML
    private ComboBox<String> taskTypeDropDown;

    @FXML
    private Button addNewTaskButton;

    Parent form;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        taskTypeDropDown.getItems().addAll(
                "Activity",
                "Exam",
                "Lesson",
                "Revision"
        );
    }


    //On add task button clicked
    //Load task creation form as fxml
    //Set to left pane of main page's border pane
    @FXML
    private void loadTaskForm() {
        try {
            form = FXMLLoader.load(Main.class.getResource(String.format("task/form/add/Add%sForm.fxml",taskTypeDropDown.getValue())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        BorderPane borderPane = (BorderPane) addNewTaskButton.getScene().getRoot();
        borderPane.setLeft(form);

    }

}
