package com.example.ontrack.task.form.edit;

import com.example.ontrack.MainPageControllerHolder;
import com.example.ontrack.alert.NotificationBox;
import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.task.form.validator.IRevisionForm;
import com.example.ontrack.task.repetition.RepetitionRule;
import com.example.ontrack.task.revision.Revision;
import com.example.ontrack.task.revision.RevisionCycle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class EditRevisionFormController implements IBackButton, IRevisionForm{
    @FXML
    Button backButton;
    @FXML
    Button saveTaskButton;

    @FXML
    TextField revisionNameTextField;
    @FXML
    TextArea revisionDescTextArea;
    @FXML
    TextField revisionSubjectTextField;

    Revision oldRevision;


    public void setRevision(Revision revision)
    {
        oldRevision=revision;
        RevisionCycle revisionCycle = new RevisionCycle(oldRevision.getTaskName());
        oldRevision = revisionCycle.getFirstRevisionInCycle();
        revisionNameTextField.setText(revision.getTaskName());
        revisionDescTextArea.setText(revision.getDescription());
        revisionSubjectTextField.setText(revision.getSubject());
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
        String revisionName = revisionNameTextField.getText();
        String revisionDesc = revisionDescTextArea.getText();
        String revisionSubject = revisionSubjectTextField.getText();
        RepetitionRule revisionRepetitionRule = oldRevision.getRepetitionRule();
        LocalDate revisionStartDate = oldRevision.getDate();

        //Create error messages
        String errorMessage = validateTaskName(revisionName)
                +validateTaskDesc(revisionDesc)
                +validateSubject(revisionSubject)
                +validateRepetitionRule(revisionRepetitionRule)
                +validateTaskDate(revisionStartDate);

        if(!errorMessage.isEmpty())
        {
            NotificationBox.display("Error",errorMessage);
        }
        else
        {
            Revision newRevision = new Revision(revisionName,revisionDesc,revisionSubject,oldRevision.getRuleId(),revisionStartDate,1,false);
            RevisionCycle.updateRevisionsInCycle(oldRevision,newRevision,oldRevision.getRepetitionRule(),oldRevision.getRepetitionRule());
            NotificationBox notificationBox = new NotificationBox();
            notificationBox.display("Success","Task Edited");
        }
        MainPageControllerHolder.getInstance().getMainPageController().refresh();
        MainPageControllerHolder.getInstance().getMainPageController().loadAddTaskForm();

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
