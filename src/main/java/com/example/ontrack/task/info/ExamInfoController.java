package com.example.ontrack.task.info;

import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.task.Activity;
import com.example.ontrack.task.Exam;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ExamInfoController implements IBackButton {
    @FXML
    private Label examNameLabel;
    @FXML
    private Label examDescLabel;
    @FXML
    private Label examVenueLabel;
    @FXML
    private Label examDateLabel;
    @FXML
    private Label examSubjectLabel;
    @FXML
    private Button backButton;

    public void setExam(Exam exam)
    {
        examNameLabel.setText(exam.getTaskName());
        examDescLabel.setText(exam.getDescription());
        examVenueLabel.setText(exam.getVenue());
        examSubjectLabel.setText(exam.getSubject());
        examDateLabel.setText(exam.getDate().toString());
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
