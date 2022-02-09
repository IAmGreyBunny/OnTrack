package com.example.ontrack.task.info;

import com.example.ontrack.IBackButton;
import com.example.ontrack.task.Exam;
import com.example.ontrack.task.Lesson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LessonInfoController implements IBackButton {
    @FXML
    Label lessonNameLabel;
    @FXML
    Label lessonDescLabel;
    @FXML
    Label lessonVenueLabel;
    @FXML
    Label lessonDateLabel;
    @FXML
    Label lessonSubjectLabel;
    @FXML
    Label lessonCurrentRoundLabel;
    @FXML
    Label lessonRepeatTypeLabel;

    public void setLesson(Lesson lesson)
    {
        lessonNameLabel.setText(lesson.getTaskName());
        lessonDescLabel.setText(lesson.getDescription());
        lessonVenueLabel.setText(lesson.getVenue());
        lessonSubjectLabel.setText(lesson.getSubject());
        lessonDateLabel.setText(lesson.getDate().toString());
        lessonCurrentRoundLabel.setText(String.valueOf(lesson.getCurrentRound()));
        lessonRepeatTypeLabel.setText(lesson.getRepetitionRule().getRepeatType());
    }

    @Override
    public void onBackButtonClicked() {

    }
}
