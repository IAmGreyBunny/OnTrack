package com.example.ontrack.task.form.edit;

import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.task.Activity;
import com.example.ontrack.task.ActivityHelper;
import com.example.ontrack.task.Exam;
import com.example.ontrack.task.form.validator.ActivityTaskFormValidator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditActivityFormController implements IBackButton {
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
        String activityNameError = "";
        String activityDescError = "";
        String activityVenueError = "";
        String errorMessage = "";

        //Validate user input
        activityNameError = ActivityTaskFormValidator.validateTaskName(activityName);
        activityDescError = ActivityTaskFormValidator.validateTaskDesc(activityDesc);
        activityVenueError = ActivityTaskFormValidator.validateVenue(activityVenue);

        if(!activityNameError.isEmpty())
        {
            errorMessage += activityNameError + "\n";
        }
        if(!activityDescError.isEmpty())
        {
            errorMessage += activityDescError + "\n";
        }
        if(!activityVenueError.isEmpty())
        {
            errorMessage += activityVenueError + "\n";
        }
        if(!errorMessage.isEmpty())
        {
            //TO DO: ERROR MESSAGE BOX TO BE IMPLEMENTED LATER
            System.out.println(errorMessage);
        }
        else
        {
            Activity newActivity = new Activity(activityName,activityDesc,activityVenue,false,activityDate);
            ActivityHelper.updateActivityInDb(oldActivity,newActivity);
        }

    }

}
