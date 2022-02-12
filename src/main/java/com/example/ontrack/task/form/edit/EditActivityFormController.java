package com.example.ontrack.task.form.edit;

import com.example.ontrack.MainPageControllerHolder;
import com.example.ontrack.NotificationBox;
import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.task.activity.Activity;
import com.example.ontrack.task.activity.ActivityHelper;
import com.example.ontrack.task.form.validator.IActivityForm;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.time.LocalDate;

public class EditActivityFormController implements IActivityForm,IBackButton {
    @FXML
    Button backButton;
    @FXML
    Button saveTaskButton;

    @FXML
    TextField activityNameTextField;
    @FXML
    TextArea activityDescTextArea;
    @FXML
    TextField activityVenueTextField;
    @FXML
    DatePicker activityDatePicker;

    Activity oldActivity;


    public void setActivity(Activity activity)
    {
        oldActivity = activity;
        activityNameTextField.setText(activity.getTaskName());
        activityDescTextArea.setText(activity.getDescription());
        activityVenueTextField.setText(activity.getVenue());
        activityDatePicker.setValue(activity.getDate());
    }

    @FXML
    public void onBackButtonClicked()
    {
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

    @FXML
    public void onSaveTaskButtonClicked()
    {
        //Gets user input
        String activityName = activityNameTextField.getText();
        String activityDesc = activityDescTextArea.getText();
        String activityVenue = activityVenueTextField.getText();
        LocalDate activityDate = activityDatePicker.getValue();

        //Create error messages
        String errorMessage = "";
        errorMessage = validateTaskName(activityName)+
                validateTaskDesc(activityDesc)+
                validateVenue(activityVenue)+
                validateTaskDate(activityDate);

        if(!errorMessage.isEmpty())
        {
            NotificationBox.display("Error",errorMessage);
        }
        else
        {
            Activity newActivity = new Activity(activityName,activityDesc,activityVenue,false,activityDate);
            ActivityHelper.updateActivityInDb(oldActivity,newActivity);
            NotificationBox notificationBox = new NotificationBox();
            notificationBox.display("Success","Task Edited");
            MainPageControllerHolder.getInstance().getMainPageController().refresh();
            MainPageControllerHolder.getInstance().getMainPageController().loadAddTaskForm();
        }

    }

    @Override
    public String validateVenue(String subject) {
        return "";
    }

    @Override
    public String validateTaskName(String taskName) {
        if(taskName.isEmpty()) {
            return "Name is required\n";
        }
        return "";
    }

    @Override
    public String validateTaskDesc(String taskDesc) {
        return "";
    }

    @Override
    public String validateTaskDate(LocalDate date)
    {
        if(date == null)
        {
            return "Date is required\n";
        }
        return "";
    }
}
