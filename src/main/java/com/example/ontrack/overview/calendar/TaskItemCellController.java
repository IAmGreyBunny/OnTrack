package com.example.ontrack.overview.calendar;

import com.example.ontrack.task.Task;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class TaskItemCellController {
    @FXML
    Label taskNameLabel;
    @FXML
    CheckBox doneCheckBox;

    Task task;

    public void setTaskToCell(Task task)
    {
        this.task=task;
        taskNameLabel.setText(task.getTaskName());
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
