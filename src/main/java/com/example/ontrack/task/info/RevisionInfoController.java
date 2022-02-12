package com.example.ontrack.task.info;

import com.example.ontrack.*;
import com.example.ontrack.task.form.edit.EditRevisionFormController;
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

public class RevisionInfoController implements IBackButton, ICompleteTaskInput, IDeleteTask, IEditTask {
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
    private Label revisionCompletionRateLabel;
    @FXML
    private Label revisionRepetitionRuleLabel;
    @FXML
    private Button backButton;
    @FXML
    private Button editButton;
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
        revisionRepetitionRuleLabel.setText(revision.getRepetitionRule().getRuleName());
        if(! revision.getRepetitionRule().getRepeatType().equals("Repeat Last"))
        {
            RevisionCycle revisionCycle = new RevisionCycle(revision.getTaskName());
            revisionCompletionRateLabel.setText(String.format("%.2f",revisionCycle.getCompletionRateOfRevisionCycle()));
        }
        completeTaskCheckBox.setSelected(revision.getStatus());
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
        MainPageControllerHolder.getInstance().getMainPageController().refresh();
    }

    @Override
    public void onCompleteTask() {
        Revision newRevision = new Revision(displayedRevision);
        newRevision.setStatus(completeTaskCheckBox.isSelected());
        RevisionHelper.updateRevisionInDb(displayedRevision,newRevision);
        setRevision(newRevision);
        RevisionCycle revisionCycle = new RevisionCycle(displayedRevision.getTaskName());
        int numberOfRoundLeft = revisionCycle.getLastRevisionInCycle().getCurrentRound()-newRevision.getCurrentRound();
        if(revisionCycle.getCompletionRateOfRevisionCycle()==100&&newRevision.getRepetitionRule().getRepeatType().equals("Start Over"))
        {
            System.out.println(revisionCycle.getCompletionRateOfRevisionCycle());
            System.out.println("Restart Cycle");
            revisionCycle.restartCycle(displayedRevision.getRepetitionRule());
        }
        else if(newRevision.getRepetitionRule().getRepeatType().equals("Repeat Last") && numberOfRoundLeft<3)
        {
            revisionCycle.repeatLast(revisionCycle.getLastRevisionInCycle(),newRevision.getRepetitionRule(),5);
        }
    }

    @Override
    public void onEditButtonClicked() {
        //Load Edit Form
        FXMLLoader editRevisionFormLoader = new FXMLLoader(Main.class.getResource("task/form/edit/EditRevisionForm.fxml"));
        EditRevisionFormController editRevisionFormController;
        Parent revisionForm;
        try {
            revisionForm = editRevisionFormLoader.load();
            editRevisionFormController = editRevisionFormLoader.getController();
            editRevisionFormController.setRevision(displayedRevision); //Load calendar cell content based on date given
            ((BorderPane) editButton.getScene().getRoot()).setLeft(revisionForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
