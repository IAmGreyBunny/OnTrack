package com.example.ontrack.task.form.add;

import com.example.ontrack.NotificationBox;
import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.task.activity.Activity;
import com.example.ontrack.task.activity.ActivityHelper;
import com.example.ontrack.task.form.validator.IActivityForm;
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

public class AddActivityFormController implements IActivityForm,IBackButton, Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            Activity activity = new Activity(activityName,activityDesc,activityVenue,false,activityDate);
            ActivityHelper.createActivityInDb(activity);
            NotificationBox notificationBox = new NotificationBox();
            notificationBox.display("Success","Task Created");
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
