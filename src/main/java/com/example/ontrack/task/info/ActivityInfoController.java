package com.example.ontrack.task.info;

import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.task.Activity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ActivityInfoController implements IBackButton {
    @FXML
    private Label activityName;
    @FXML
    private Label activityDesc;
    @FXML
    private Label activityVenue;
    @FXML
    private Label activityDate;
    @FXML
    private Button backButton;

    public void setActivity(Activity activity)
    {
        activityName.setText(activity.getTaskName());
        activityDesc.setText(activity.getDescription());
        activityVenue.setText(activity.getVenue());
        activityDate.setText(activity.getDate().toString());
    }

    @Override
    public void onBackButtonClicked() {
        Parent form;
        BorderPane borderPane = (BorderPane) backButton.getScene().getRoot();
        try {
            form = FXMLLoader.load(Main.class.getResource("task/form/add/AddTaskForm.fxml"));
            borderPane.setLeft(form);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
