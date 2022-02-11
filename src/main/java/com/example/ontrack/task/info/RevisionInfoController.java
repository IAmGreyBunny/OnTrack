package com.example.ontrack.task.info;

import com.example.ontrack.IBackButton;
import com.example.ontrack.ICompleteTaskInput;
import com.example.ontrack.IDeleteTask;
import com.example.ontrack.Main;
import com.example.ontrack.task.revision.RevisionHelper;
import com.example.ontrack.task.revision.Revision;
import com.example.ontrack.task.revision.RevisionCycle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class RevisionInfoController implements IBackButton, ICompleteTaskInput, IDeleteTask {
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
    @FXML
    private Button deleteRevisionButton;
    @FXML
    private CheckBox completeTaskCheckBox;

    Revision displayedRevision;

    public void setRevision(Revision revision)
    {
        revisionNameLabel.setText(revision.getTaskName());
        revisionDescLabel.setText(revision.getDescription());
        revisionSubjectLabel.setText(revision.getSubject());
        revisionDateLabel.setText(revision.getDate().toString());
        revisionCurrentRoundLabel.setText(String.valueOf(revision.getCurrentRound()));
        revisionRepeatTypeLabel.setText(revision.getRepetitionRule().getRepeatType());
        RevisionCycle revisionCycle = new RevisionCycle(revision.getTaskName());
        revisionCycle.getCompletionRateOfRevisionCycle();
        displayedRevision=revision;
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
    public void onDeleteTask() {
        RevisionCycle revisionCycle = new RevisionCycle(displayedRevision.getTaskName());
        revisionCycle.deleteRevisionCycle();
    }

    @Override
    public void onCompleteTask() {
        Revision newRevision = new Revision(displayedRevision);
        newRevision.setStatus(completeTaskCheckBox.isSelected());
        RevisionHelper.updateRevisionInDb(displayedRevision,newRevision);
        setRevision(newRevision);
        RevisionCycle revisionCycle = new RevisionCycle(displayedRevision.getTaskName());
        if(revisionCycle.getCompletionRateOfRevisionCycle()==100)
        {
            System.out.println(revisionCycle.getCompletionRateOfRevisionCycle());
            System.out.println("Restart Cycle");
            revisionCycle.restartCycle(displayedRevision.getRepetitionRule());
        }
    }
}
