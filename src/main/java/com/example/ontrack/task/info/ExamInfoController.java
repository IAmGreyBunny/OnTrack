package com.example.ontrack.task.info;

import com.example.ontrack.IBackButton;
import com.example.ontrack.task.Activity;
import com.example.ontrack.task.Exam;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ExamInfoController implements IBackButton {
    @FXML
    Label examNameLabel;
    @FXML
    Label examDescLabel;
    @FXML
    Label examVenueLabel;
    @FXML
    Label examDateLabel;
    @FXML
    Label examSubjectLabel;

    public void setExam(Exam exam)
    {
        examNameLabel.setText(exam.getTaskName());
        examDescLabel.setText(exam.getDescription());
        examVenueLabel.setText(exam.getVenue());
        examDateLabel.setText(exam.getSubject());
        examSubjectLabel.setText(exam.getDate().toString());
    }

    @Override
    public void onBackButtonClicked() {

    }
}
