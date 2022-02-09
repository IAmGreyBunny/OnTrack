package com.example.ontrack.task.info;

import com.example.ontrack.IBackButton;
import com.example.ontrack.task.Lesson;
import com.example.ontrack.task.Revision;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RevisionInfoController implements IBackButton {
    @FXML
    Label revisionNameLabel;
    @FXML
    Label revisionDescLabel;
    @FXML
    Label revisionDateLabel;
    @FXML
    Label revisionSubjectLabel;
    @FXML
    Label revisionCurrentRoundLabel;
    @FXML
    Label revisionRepeatTypeLabel;

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

    }
}
