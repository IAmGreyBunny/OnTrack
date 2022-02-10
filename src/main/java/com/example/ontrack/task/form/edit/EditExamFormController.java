package com.example.ontrack.task.form.edit;

import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.task.Exam;
import com.example.ontrack.task.ExamHelper;
import com.example.ontrack.task.form.validator.IExamForm;
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

public class EditExamFormController implements IExamForm, IBackButton {
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

    Exam oldExam;

    public void setExam(Exam exam)
    {
        oldExam = exam;
        examNameTextField.setText(exam.getTaskName());
        examDescTextArea.setText(exam.getDescription());
        examSubjectTextField.setText(exam.getSubject());
        examVenueTextField.setText(exam.getVenue());
        examDatePicker.setValue(exam.getDate());
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

        //Validate user input
        String errorMessage = validateTaskName(examName) +
                validateTaskDesc(examDesc) +
                validateTaskDate(examDate) +
                validateSubject(examSubject) +
                validateVenue(examVenue);

        if(!errorMessage.isEmpty())
        {
            //TO DO: ERROR MESSAGE BOX TO BE IMPLEMENTED LATER
            System.out.println(errorMessage);
        }
        else
        {
            Exam newExam = new Exam(examName,examDesc,examSubject,examVenue,examDate,false);
            ExamHelper.updateExamInDb(oldExam,newExam);
        }


    }

    @Override
    public String validateVenue(String subject) {
        return "";
    }

    @Override
    public String validateSubject(String subject) {
        return "";
    }

    @Override
    public String validateTaskName(String taskName) {
        if (taskName.isEmpty()) {
            return "Name is required";
        }
        return "";
    }

    @Override
    public String validateTaskDesc(String taskDesc) {
        return "";
    }

    @Override
    public String validateTaskDate(LocalDate date) {
        if (date == null) {
            return "date is required";
        }
        return "";
    }
}
