package com.example.ontrack.overview.calendar;

import com.example.ontrack.task.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskItemCellController implements Initializable {
    @FXML
    HBox encompassingHbox;
    @FXML
    Label taskNameLabel;
    @FXML
    CheckBox doneCheckBox;

    Task task;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        encompassingHbox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                encompassingHbox.setScaleX(1.2);
                encompassingHbox.setScaleY(1.2);
            }
        });
        encompassingHbox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                encompassingHbox.setScaleX(1.0);
                encompassingHbox.setScaleY(1.0);
            }
        });
    }

    public void setTaskToCell(Task task)
    {
        this.task=task;
        taskNameLabel.setText(task.getTaskName());
    }
}
