package com.example.ontrack.task.form.edit;

import com.example.ontrack.MainPageControllerHolder;
import com.example.ontrack.alert.NotificationBox;
import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.task.form.validator.ILessonForm;
import com.example.ontrack.task.lesson.Lesson;
import com.example.ontrack.task.lesson.LessonCycle;
import com.example.ontrack.task.repetition.RepetitionRule;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class EditLessonFormController implements IBackButton, ILessonForm{
    @FXML
    Button backButton;
    @FXML
    Button saveTaskButton;

    @FXML
    TextField lessonNameTextField;
    @FXML
    TextArea lessonDescTextArea;
    @FXML
    TextField lessonSubjectTextField;
    @FXML
    TextField lessonVenueTextField;

    Lesson oldLesson;

    public void setLesson(Lesson lesson)
    {
        oldLesson=lesson;
        LessonCycle lessonCycle = new LessonCycle(oldLesson.getTaskName());
        oldLesson = lessonCycle.getFirstLessonInCycle();
        lessonNameTextField.setText(lesson.getTaskName());
        lessonDescTextArea.setText(lesson.getDescription());
        lessonSubjectTextField.setText(lesson.getSubject());
        lessonVenueTextField.setText(lesson.getVenue());
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
    public void onSaveTaskButtonClicked() throws SQLException {
        //Gets user input
        String lessonName = lessonNameTextField.getText();
        String lessonDesc = lessonDescTextArea.getText();
        String lessonSubject = lessonSubjectTextField.getText();
        String lessonVenue = lessonVenueTextField.getText();
        RepetitionRule lessonRepetitionRule = oldLesson.getRepetitionRule();
        LocalDate lessonDate = oldLesson.getDate();

        //Create error messages
        String errorMessage = validateTaskName(lessonName)
                +validateTaskDesc(lessonDesc)
                +validateSubject(lessonSubject)
                +validateVenue(lessonVenue)
                +validateRepetitionRule(lessonRepetitionRule)
                +validateTaskDate(lessonDate);

        if(!errorMessage.isEmpty())
        {
            NotificationBox.display("Error",errorMessage);
        }
        else
        {
            Lesson newLesson = new Lesson(lessonName,lessonDesc,lessonSubject,lessonVenue,lessonDate,1,false);
            LessonCycle.updateLessonsInCycle(oldLesson,newLesson,oldLesson.getRepetitionRule(),lessonRepetitionRule);
            NotificationBox notificationBox = new NotificationBox();
            notificationBox.display("Success","Task Edited");
            MainPageControllerHolder.getInstance().getMainPageController().refresh();
            MainPageControllerHolder.getInstance().getMainPageController().loadAddTaskForm();
        }

    }

    @Override
    public String validateTaskName(String taskName) {
        String errorMessage = "";
        if (taskName.isEmpty()) {
            errorMessage += "Name is required\n";
        }
        return errorMessage;
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

    @Override
    public String validateSubject(String subject) {
        return "";
    }

    @Override
    public String validateVenue(String subject) {
        return "";
    }

    @Override
    public String validateRepetitionRule(RepetitionRule repetitionRule) {
        if(repetitionRule == null)
        {
            return "Repetition rule must be set\n";
        }
        else
        {
            return "";
        }
    }
}
