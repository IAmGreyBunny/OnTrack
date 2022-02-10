package com.example.ontrack.task.info;

import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.overview.calendar.TaskItemCellController;
import com.example.ontrack.task.Activity;
import com.example.ontrack.task.Exam;
import com.example.ontrack.task.ExamHelper;
import com.example.ontrack.task.form.edit.EditExamFormController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    @FXML
    private Button editButton;

    Exam displayedExam;

    public void setExam(Exam exam)
    {
        examNameLabel.setText(exam.getTaskName());
        examDescLabel.setText(exam.getDescription());
        examVenueLabel.setText(exam.getVenue());
        examSubjectLabel.setText(exam.getSubject());
        examDateLabel.setText(exam.getDate().toString());
        displayedExam = exam;
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

    public void onEditButtonClicked()
    {
        //Create calendar cell for date
        FXMLLoader editExamFormLoader = new FXMLLoader(Main.class.getResource("task/form/edit/EditExamForm.fxml"));
        EditExamFormController editExamFormController;
        Parent examForm;
        try {
            examForm = editExamFormLoader.load();
            editExamFormController = editExamFormLoader.getController();
            editExamFormController.setExam(displayedExam); //Load calendar cell content based on date given
            ((BorderPane) editButton.getScene().getRoot()).setLeft(examForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
