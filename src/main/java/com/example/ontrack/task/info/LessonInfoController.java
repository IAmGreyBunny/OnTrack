package com.example.ontrack.task.info;

import com.example.ontrack.IBackButton;
import com.example.ontrack.ICompleteTaskInput;
import com.example.ontrack.IDeleteTask;
import com.example.ontrack.Main;
import com.example.ontrack.task.exam.Exam;
import com.example.ontrack.task.exam.ExamHelper;
import com.example.ontrack.task.lesson.LessonCycle;
import com.example.ontrack.task.lesson.LessonHelper;
import com.example.ontrack.task.repetition.RepetitionRuleHelper;
import com.example.ontrack.task.lesson.Lesson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class LessonInfoController implements IBackButton, IDeleteTask, ICompleteTaskInput {
    @FXML
    private Label lessonNameLabel;
    @FXML
    private Label lessonDescLabel;
    @FXML
    private Label lessonVenueLabel;
    @FXML
    private Label lessonDateLabel;
    @FXML
    private Label lessonSubjectLabel;
    @FXML
    private Label lessonCurrentRoundLabel;
    @FXML
    private Label lessonRepeatTypeLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button deleteLessonButton;
    @FXML
    private CheckBox completeTaskCheckBox;

    Lesson displayedLesson;

    public void setLesson(Lesson lesson)
    {
        lessonNameLabel.setText(lesson.getTaskName());
        lessonDescLabel.setText(lesson.getDescription());
        lessonVenueLabel.setText(lesson.getVenue());
        lessonSubjectLabel.setText(lesson.getSubject());
        lessonDateLabel.setText(lesson.getDate().toString());
        lessonCurrentRoundLabel.setText(String.valueOf(lesson.getCurrentRound()));
        lessonRepeatTypeLabel.setText(RepetitionRuleHelper.getRepetitionRuleFromId(lesson.getRuleId()).getRepeatType());
        LessonCycle lessonCycle = new LessonCycle(lesson.getTaskName());
        lessonCycle.getCompletionRateOfLessonCycle();
        displayedLesson=lesson;
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

    @Override
    public void onDeleteTask()
    {
        LessonCycle lessonCycle = new LessonCycle(displayedLesson.getTaskName());
        lessonCycle.deleteLessonCycle();
    }

    @Override
    public void onCompleteTask() {
        Lesson newLesson = new Lesson(displayedLesson);
        newLesson.setStatus(completeTaskCheckBox.isSelected());
        LessonHelper.updateLessonInDb(displayedLesson,newLesson);
        setLesson(newLesson);
    }
}
