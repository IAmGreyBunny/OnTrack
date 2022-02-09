package com.example.ontrack.task.info;

import com.example.ontrack.IBackButton;
import com.example.ontrack.task.Activity;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ActivityInfoController implements IBackButton {
    @FXML
    Label activityName;
    @FXML
    Label activityDesc;
    @FXML
    Label activityVenue;
    @FXML
    Label activityDate;

    public void setActivity(Activity activity)
    {
        activityName.setText(activity.getTaskName());
        activityDesc.setText(activity.getDescription());
        activityVenue.setText(activity.getVenue());
        activityDate.setText(activity.getDate().toString());
    }

    @Override
    public void onBackButtonClicked() {

    }
}
