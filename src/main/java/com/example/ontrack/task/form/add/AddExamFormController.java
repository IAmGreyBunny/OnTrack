package com.example.ontrack.task.form.add;

import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.repetition.RepetitionRule;
import com.example.ontrack.task.Exam;
import com.example.ontrack.task.ExamHelper;
import com.example.ontrack.task.Revision;
import com.example.ontrack.task.form.validator.ExamTaskFormValidator;
import com.example.ontrack.task.form.validator.RevisionTaskFormValidator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddExamFormController implements IBackButton, Initializable {
    @FXML
    Button backButton;
    @FXML
    Button saveTaskButton;

    @FXML
    TextField examNameTextField;
    @FXML
    TextArea examDescTextArea;
    @FXML
    TextField examVenueTextField;
    @FXML
    TextField examSubjectTextField;
    @FXML
    DatePicker examDatePicker;

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
        String examName = examNameTextField.getText();
        String examDesc = examDescTextArea.getText();
        String examSubject = examSubjectTextField.getText();
        String examVenue = examVenueTextField.getText();
        LocalDate examDate = examDatePicker.getValue();

        //Create error messages
        String examNameError = "";
        String examDescError = "";
        String examSubjectError = "";
        String examVenueError = "";
        String errorMessage = "";

        //Validate user input
        examNameError = ExamTaskFormValidator.validateTaskName(examName);
        examDescError = ExamTaskFormValidator.validateTaskDesc(examDesc);
        examSubjectError = ExamTaskFormValidator.validateSubject(examSubject);
        examVenueError = ExamTaskFormValidator.validateVenue(examVenue);

        if(!examNameError.isEmpty())
        {
            errorMessage += examNameError + "\n";
        }
        if(!examDescError.isEmpty())
        {
            errorMessage += examDescError + "\n";
        }
        if(!examSubjectError.isEmpty())
        {
            errorMessage += examSubjectError + "\n";
        }
        if(!examVenueError.isEmpty())
        {
            errorMessage += examVenueError + "\n";
        }
        if(!errorMessage.isEmpty())
        {
            //TO DO: ERROR MESSAGE BOX TO BE IMPLEMENTED LATER
            System.out.println(errorMessage);
        }
        else
        {
            Exam exam = new Exam(examName,examDesc,examSubject,examVenue,examDate,false);
            ExamHelper.createExamInDb(exam);
        }

    }

}
