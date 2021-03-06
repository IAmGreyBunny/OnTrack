package com.example.ontrack.task.info;

import com.example.ontrack.*;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.overview.calendar.CalendarControllerHolder;
import com.example.ontrack.task.exam.Exam;
import com.example.ontrack.task.exam.ExamHelper;
import com.example.ontrack.task.form.edit.EditExamFormController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

public class ExamInfoController implements IBackButton, ICompleteTaskInput, IDeleteTask {
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
    @FXML
    private CheckBox completeTaskCheckBox;
    @FXML
    private Button deleteExamButton;

    Exam displayedExam;

    public void setExam(Exam exam)
    {
        examNameLabel.setText(exam.getTaskName());
        examDescLabel.setText(exam.getDescription());
        examVenueLabel.setText(exam.getVenue());
        examSubjectLabel.setText(exam.getSubject());
        examDateLabel.setText(exam.getDate().toString());
        completeTaskCheckBox.setSelected(exam.getStatus());
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
        //Load Edit Form
        System.out.println(Main.class.getResource("task/form/edit/EditExamForm.fxml"));
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

    @Override
    public void onCompleteTask() {
        Exam newExam = new Exam(displayedExam);
        newExam.setStatus(completeTaskCheckBox.isSelected());
        ExamHelper.updateExamInDb(displayedExam,newExam);
        setExam(newExam);
        CalendarControllerHolder.getInstance().refreshCalendar();
    }

    @Override
    public void onDeleteTask() {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        sql = String.format("DELETE FROM exams WHERE (examId=%s)",
                displayedExam.getExamId()); //+1 because sql is not zero-indexed
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        MainPageControllerHolder.getInstance().getMainPageController().refresh();
        MainPageControllerHolder.getInstance().getMainPageController().loadAddTaskForm();
    }
}
