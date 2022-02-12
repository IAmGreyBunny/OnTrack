package com.example.ontrack.task.form.edit;

import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.task.form.validator.IRevisionForm;
import com.example.ontrack.task.repetition.RepetitionRule;
import com.example.ontrack.task.revision.Revision;
import com.example.ontrack.task.revision.RevisionCycle;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditRevisionFormController implements IBackButton, IRevisionForm, Initializable {
    @FXML
    Button backButton;
    @FXML
    Button editRepetitionRuleButton;
    @FXML
    Button addRepetitionRuleButton;
    @FXML
    Button saveTaskButton;

    @FXML
    TextField revisionNameTextField;
    @FXML
    TextArea revisionDescTextArea;
    @FXML
    TextField revisionSubjectTextField;
    @FXML
    TextField revisionVenueTextField;
    @FXML
    DatePicker revisionStartDatePicker;

    @FXML
    ComboBox<RepetitionRule> repetitionRuleDropDown;

    Revision oldrevision;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        //Setup repetitionRuleDropDown combobox with user listofrepetitionrules
        //String converter required for displaying RepetitionRule object as string
        repetitionRuleDropDown.setOnShown(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                ObservableList<RepetitionRule> listOfRepetitionRules = CurrentUser.getInstance().getUserRepetitionRules();
                StringConverter<RepetitionRule> converter = new StringConverter<RepetitionRule>() {
                    @Override
                    public String toString(RepetitionRule repetitionRule) {
                        if(repetitionRule != null)
                        {
                            return repetitionRule.getRuleName();
                        }
                        else
                        {
                            return "";
                        }

                    }

                    @Override
                    public RepetitionRule fromString(String s) {
                        for(RepetitionRule repetitionRule:listOfRepetitionRules)
                        {
                            if (repetitionRule.getRuleName().equals(s))
                            {
                                return repetitionRule;
                            }
                        }
                        return null;
                    }
                };
                repetitionRuleDropDown.setConverter(converter);
                repetitionRuleDropDown.setItems(listOfRepetitionRules);
            }
        });

        //Setup repetitionRuleDropDown so to disable edit on empty fields
        if(repetitionRuleDropDown.getValue()==null)
        {
            editRepetitionRuleButton.setDisable(true);
        }
        repetitionRuleDropDown.setOnAction(actionEvent -> {
            if(repetitionRuleDropDown.getValue()==null)
            {
                editRepetitionRuleButton.setDisable(true);
            }
            else
            {
                editRepetitionRuleButton.setDisable(false);
            }
        });
    }

    public void setRevision(Revision revision)
    {
        oldrevision=revision;
        RevisionCycle revisionCycle = new RevisionCycle(oldrevision.getTaskName());
        oldrevision = revisionCycle.getFirstRevisionInCycle();
        revisionNameTextField.setText(revision.getTaskName());
        revisionDescTextArea.setText(revision.getDescription());
        revisionSubjectTextField.setText(revision.getSubject());
        revisionStartDatePicker.setValue(revision.getDate());
    }

    @FXML
    public void loadEditRepetitionRuleForm()
    {
        //Load Edit Form
        FXMLLoader editRepetitionRuleFormLoader = new FXMLLoader(Main.class.getResource("task/form/edit/EditRepetitionRuleForm.fxml"));
        EditRepetitionRuleFormController editRepetitionRuleFormController;
        Parent editRepetitionRuleForm;
        try {
            editRepetitionRuleForm = editRepetitionRuleFormLoader.load();
            editRepetitionRuleFormController = editRepetitionRuleFormLoader.getController();
            editRepetitionRuleFormController.setRepetitionRule(repetitionRuleDropDown.getValue()); //Load calendar cell content based on date given

            //Display new window
            Stage stage = new Stage();
            Scene scene = new Scene(editRepetitionRuleForm);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void loadAddRepetitionRuleForm() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("task/form/add/AddRepetitionRuleForm.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
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
        String revisionVenue = revisionVenueTextField.getText();
        RepetitionRule revisionRepetitionRule = repetitionRuleDropDown.getValue();
        LocalDate revisionStartDate = revisionStartDatePicker.getValue();

        //Create error messages
        String errorMessage = validateTaskName(revisionName)
                +validateTaskDesc(revisionDesc)
                +validateSubject(revisionSubject)
                +validateRepetitionRule(revisionRepetitionRule)
                +validateTaskDate(revisionStartDate);

        if(!errorMessage.isEmpty())
        {
            //TO DO: ERROR MESSAGE BOX TO BE IMPLEMENTED LATER
            System.out.println(errorMessage);
        }
        else
        {
            Revision newrevision = new Revision(revisionName,revisionDesc,revisionSubject,oldrevision.getRuleId(),revisionStartDate,1,false);
            RevisionCycle.updateRevisionsInCycle(oldrevision,newrevision,oldrevision.getRepetitionRule(),repetitionRuleDropDown.getValue());
        }

    }

    @Override
    public String validateTaskName(String taskName) {
        String errorMessage = "";
        if (taskName.isEmpty()) {
            errorMessage += "Name is required";
        }
        //Check if user have any rule with same name
        if(!taskName.isEmpty()) {
            //Database Connection
            DatabaseManager databaseManager = new DatabaseManager();
            Connection connection = databaseManager.getConnection();

            String sql = String.format("SELECT DISTINCT name FROM revisions WHERE (userId = %s)", CurrentUser.getInstance().getUser().getUserId());
            try {
                PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery();
                if (DatabaseHelper.getResultSetSize(resultSet) >= 1) {
                    do {
                        if (resultSet.getString("name").equals(taskName)) {
                            errorMessage += "revision with same name already exist";
                            break;
                        }
                    } while (resultSet.next());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            return "date is required";
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
            return "Repetition rule must be set";
        }
        else
        {
            return "";
        }
    }
}
