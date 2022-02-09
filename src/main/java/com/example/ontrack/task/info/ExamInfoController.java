package com.example.ontrack.task.info;

import com.example.ontrack.IBackButton;
import com.example.ontrack.task.Activity;
import com.example.ontrack.task.Exam;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ExamInfoController implements IBackButton {
    @FXML
    Label examName;
    @FXML
    Label examDesc;
    @FXML
    Label examVenue;
    @FXML
    Label examDate;
    @FXML
    Label examSubject;

    public void setExam(Exam exam)
    {
        examName.setText(exam.getTaskName());
        examDesc.setText(exam.getDescription());
        examVenue.setText(exam.getVenue());
        examSubject.setText(exam.getSubject());
        examDate.setText(exam.getDate().toString());
    }

    @Override
    public void onBackButtonClicked() {

    }
}
