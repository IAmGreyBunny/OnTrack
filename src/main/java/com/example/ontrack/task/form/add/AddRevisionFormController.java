package com.example.ontrack.task.form.add;

import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.task.Revision;
import com.example.ontrack.task.RevisionHelper;
import com.example.ontrack.task.form.validator.RevisionTaskFormValidator;
import com.example.ontrack.repetition.RepetitionRule;
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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddRevisionFormController implements IBackButton, Initializable {
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
    DatePicker revisionStartDatePicker;

    @FXML
    ComboBox<RepetitionRule> repetitionRuleDropDown;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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

    @FXML
    public void loadEditRepetitionRuleForm()
    {

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
        RepetitionRule revisionRepetitionRule = repetitionRuleDropDown.getValue();
        LocalDate revisionStartDate = revisionStartDatePicker.getValue();

        //Create error messages
        String revisionNameError = "";
        String revisionDescError = "";
        String revisionSubjectError = "";
        String revisionRepetitionRuleError = "";
        String revisionDateRuleError = "";
        String errorMessage = "";

        //Validate user input
        revisionNameError = RevisionTaskFormValidator.validateTaskName(revisionName);
        revisionDescError = RevisionTaskFormValidator.validateTaskDesc(revisionDesc);
        revisionSubjectError = RevisionTaskFormValidator.validateSubject(revisionSubject);
        revisionRepetitionRuleError = RevisionTaskFormValidator.validateRepetitionRule(revisionRepetitionRule);
        revisionDateRuleError = RevisionTaskFormValidator.validateDate(revisionStartDate);

        if(!revisionNameError.isEmpty())
        {
            errorMessage += revisionNameError + "\n";
        }
        if(!revisionDescError.isEmpty())
        {
            errorMessage += revisionDescError + "\n";
        }
        if(!revisionSubjectError.isEmpty())
        {
            errorMessage += revisionSubjectError + "\n";
        }
        if(!revisionRepetitionRuleError.isEmpty())
        {
            errorMessage += revisionRepetitionRuleError + "\n";
        }
        if(!revisionDateRuleError.isEmpty())
        {
            errorMessage += revisionDateRuleError + "\n";
        }

        if(!errorMessage.isEmpty())
        {
            //TO DO: ERROR MESSAGE BOX TO BE IMPLEMENTED LATER
            System.out.println(errorMessage);
        }
        else
        {
            Revision revision = new Revision(revisionName,revisionDesc,revisionSubject,revisionStartDate,0,false);
            RevisionHelper.createRevisionInDb(revision);
            revision.setRepetitionRule(revisionRepetitionRule);
            RevisionHelper.createRevisionCycleInDb(revision,revisionRepetitionRule);
        }

    }

}
