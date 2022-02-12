package com.example.ontrack.task.form.add;

import com.example.ontrack.NotificationBox;
import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.task.exam.Exam;
import com.example.ontrack.task.exam.ExamHelper;
import com.example.ontrack.task.form.validator.IExamForm;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddExamFormController implements IBackButton, IExamForm, Initializable {
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
    public void onBackButtonClicked() {
        Parent form;
        BorderPane borderPane = (BorderPane) backButton.getScene().getRoot();
        try {
            form = FXMLLoader.load(Main.class.getResource("task/form/add/AddTaskForm.fxml"));
            borderPane.setLeft(form);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onSaveTaskButtonClicked() {
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


        if (!errorMessage.isEmpty()) {
            NotificationBox.display("Error",errorMessage);
        } else {
            Exam exam = new Exam(examName, examDesc, examSubject, examVenue, examDate, false);
            ExamHelper.createExamInDb(exam);
            NotificationBox notificationBox = new NotificationBox();
            notificationBox.display("Success","Task Created");
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
            return "Name is required\n";
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
            return "Date is required\n";
        }
        return "";
    }
}
