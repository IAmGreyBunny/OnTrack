package com.example.ontrack.task.info;

import com.example.ontrack.*;
import com.example.ontrack.alert.ConfirmationBox;
import com.example.ontrack.overview.calendar.CalendarControllerHolder;
import com.example.ontrack.task.form.edit.EditLessonFormController;
import com.example.ontrack.task.lesson.LessonCycle;
import com.example.ontrack.task.lesson.LessonHelper;
import com.example.ontrack.task.lesson.Lesson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class LessonInfoController implements IBackButton, IDeleteTask, ICompleteTaskInput, IEditTask {
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
    private Label lessonCompletionRateLabel;
    @FXML
    private Label lessonRepetitionRuleLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button editButton;
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
        lessonRepeatTypeLabel.setText(lesson.getRepetitionRule().getRepeatType());
        lessonRepetitionRuleLabel.setText(lesson.getRepetitionRule().getRuleName());
        Animation.popUpChange(lessonCompletionRateLabel,1.2);
        if(! lesson.getRepetitionRule().getRepeatType().equals("Repeat Last"))
        {
            LessonCycle lessonCycle = new LessonCycle(lesson.getTaskName());
            lessonCompletionRateLabel.setText(String.format("%.2f",lessonCycle.getCompletionRateOfLessonCycle()));
            Animation.popUpChange(lessonCompletionRateLabel,1.2);
        }
        else
        {
            lessonCompletionRateLabel.setText("Infinite");
        }
        completeTaskCheckBox.setSelected(lesson.getStatus());
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
        ConfirmationBox confirmationBox = new ConfirmationBox();
        if(confirmationBox.display("Delete","Deleting this task will delete all task in chain\n Are you sure you want to do this?"))
        {
            LessonCycle lessonCycle = new LessonCycle(displayedLesson.getTaskName());
            lessonCycle.deleteLessonCycle();
            MainPageControllerHolder.getInstance().getMainPageController().refresh();
            MainPageControllerHolder.getInstance().getMainPageController().loadAddTaskForm();
        }
    }

    @Override
    public void onEditButtonClicked()
    {
        //Load Edit Form
        FXMLLoader editLessonFormLoader = new FXMLLoader(Main.class.getResource("task/form/edit/EditLessonForm.fxml"));
        EditLessonFormController editLessonFormController;
        Parent LessonForm;
        try {
            LessonForm = editLessonFormLoader.load();
            editLessonFormController = editLessonFormLoader.getController();
            editLessonFormController.setLesson(displayedLesson); //Load calendar cell content based on date given
            ((BorderPane) editButton.getScene().getRoot()).setLeft(LessonForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCompleteTask() {
        Lesson newLesson = new Lesson(displayedLesson);
        newLesson.setStatus(completeTaskCheckBox.isSelected());
        LessonHelper.updateLessonInDb(displayedLesson,newLesson);
        LessonCycle lessonCycle = new LessonCycle(displayedLesson.getTaskName());
        int numberOfRoundLeft = lessonCycle.getLastLessonInCycle().getCurrentRound()-newLesson.getCurrentRound();
        if(lessonCycle.getCompletionRateOfLessonCycle()==100 && newLesson.getRepetitionRule().getRepeatType().equals("Start Over"))
        {
            System.out.println(lessonCycle.getCompletionRateOfLessonCycle());
            System.out.println("Restart Cycle");
            lessonCycle.restartCycle(displayedLesson.getRepetitionRule());
        }
        else if(newLesson.getRepetitionRule().getRepeatType().equals("Repeat Last") && numberOfRoundLeft<3)
        {
            lessonCycle.extendCycle(lessonCycle.getLastLessonInCycle(),newLesson.getRepetitionRule(),5);
        }
        CalendarControllerHolder.getInstance().refreshCalendar();
        setLesson(newLesson);
    }
}
