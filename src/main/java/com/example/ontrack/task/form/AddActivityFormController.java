package com.example.ontrack.task.form;

import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.task.Activity;
import com.example.ontrack.task.form.validator.ActivityTaskFormValidator;
import com.example.ontrack.task.Activity;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddActivityFormController implements IBackButton, Initializable {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

    @FXML
    public void onSaveTaskButtonClicked()
    {
        //Gets user input
        String activityName = activityNameTextField.getText();
        String activityDesc = activityDescTextArea.getText();
        String activityVenue = activityVenueTextField.getText();

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
            Activity activity = new Activity(activityName,activityDesc,activityVenue);
            activity.createActivityInDb();
        }

    }

}
