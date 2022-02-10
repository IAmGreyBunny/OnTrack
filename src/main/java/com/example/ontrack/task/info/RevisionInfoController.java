package com.example.ontrack.task.info;

import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.task.Lesson;
import com.example.ontrack.task.Revision;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class RevisionInfoController implements IBackButton {
    @FXML
    private Label revisionNameLabel;
    @FXML
    private Label revisionDescLabel;
    @FXML
    private Label revisionDateLabel;
    @FXML
    private Label revisionSubjectLabel;
    @FXML
    private Label revisionCurrentRoundLabel;
    @FXML
    private Label revisionRepeatTypeLabel;
    @FXML
    private Button backButton;

    public void setRevision(Revision revision)
    {
        revisionNameLabel.setText(revision.getTaskName());
        revisionDescLabel.setText(revision.getDescription());
        revisionDateLabel.setText(revision.getSubject());
        revisionSubjectLabel.setText(revision.getDate().toString());
        revisionCurrentRoundLabel.setText(String.valueOf(revision.getCurrentRound()));
        revisionRepeatTypeLabel.setText(revision.getRepetitionRule().getRepeatType());
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
