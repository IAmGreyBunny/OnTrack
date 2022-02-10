package com.example.ontrack.task.form.edit;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.task.form.validator.IRepetitionRuleForm;
import com.example.ontrack.task.repetition.RepetitionRule;
import com.example.ontrack.task.repetition.RepetitionRuleHelper;
import com.example.ontrack.task.repetition.Round;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class EditRepetitionRuleFormController implements IRepetitionRuleForm, Initializable {
    @FXML
    Button addApplyButton;
    @FXML
    Button deleteButton;
    @FXML
    Button saveButton;

    @FXML
    TextField roundNumberTextField;
    @FXML
    TextField roundIntervalTextField;
    @FXML
    TextField ruleNameTextField;

    @FXML
    ComboBox<String> repeatTypeDropDown;

    @FXML
    private TableView<Round> roundTableView;
    @FXML
    private TableColumn<Round,Integer> roundNumberColumn;
    @FXML
    private TableColumn<Round,Integer> roundIntervalColumn;

    RepetitionRule oldRepetitionRule;

    //Temporary holder for list of round to be edited by user so that it does not interfere when user don't save
    ObservableList<Round> placeholderListOfRounds = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CurrentUser.getInstance().reloadUser();
    }

    @FXML
    private void onAddChangeButtonClick()
    {
        int roundNumber=0;
        int roundInterval=0;

        if(!(roundNumberTextField.getText().isEmpty() || roundIntervalTextField.getText().isEmpty()))
        {
            roundNumber = Integer.valueOf(roundNumberTextField.getText());
            roundInterval = Integer.valueOf(roundIntervalTextField.getText());
        }

        //Check if input value is valid
        if(roundNumber>0 && roundInterval>0)
        {
            //If round number exist, change its round interval
            for(Round round:placeholderListOfRounds)
            {
                if(round.getRoundNumber()==roundNumber)
                {
                    round.setRoundInterval(roundInterval);
                    roundTableView.refresh();
                    roundTableView.getSortOrder().add(roundNumberColumn);
                    return;
                }
            }

            //If round does not exist,create a new one
            placeholderListOfRounds.add(new Round(roundNumber,roundInterval));
            roundTableView.refresh();
            roundTableView.getSortOrder().add(roundNumberColumn);
        }
    }

    @FXML
    private void onSaveButtonClick()
    {
        //Sort list before doing anything else
        roundNumberColumn.setSortType(TableColumn.SortType.ASCENDING);
        roundTableView.getSortOrder().add(roundNumberColumn);

        //Form validation
        String ruleName = ruleNameTextField.getText();
        String repeatType = repeatTypeDropDown.getValue();
        String errorMessage =  validateName(ruleName)
                +validateRepeatType(repeatType)
                +validateRoundConsistency(placeholderListOfRounds);

        //If form validation is successful
        if(!errorMessage.isEmpty())
        {
            System.out.println(errorMessage);
        }
        else
        {
            RepetitionRule newRepetitionRule = new RepetitionRule(ruleName,repeatType,placeholderListOfRounds);
            RepetitionRuleHelper.updateRepetitionRule(oldRepetitionRule,newRepetitionRule);
            CurrentUser.getInstance().reloadUser();
        }

    }

    //Deletes selected row
    @FXML
    private void onDeleteButtonClick()
    {
        roundTableView.getItems().removeAll(roundTableView.getSelectionModel().getSelectedItems());
    }

    public void setRepetitionRule(RepetitionRule repetitionRule)
    {

        oldRepetitionRule = repetitionRule;

        //Tie attributes of Round class to table columns
        roundNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roundNumber"));
        roundIntervalColumn.setCellValueFactory(new PropertyValueFactory<>("roundInterval"));
        roundIntervalColumn.setSortable(false);

        //Set values to old repetition rules
        placeholderListOfRounds= FXCollections.observableArrayList(oldRepetitionRule.getRounds());
        roundTableView.setItems(placeholderListOfRounds);
        ruleNameTextField.setText(oldRepetitionRule.getRuleName());
        repeatTypeDropDown.setValue(oldRepetitionRule.getRepeatType());

        //Adds listener row, activates when row is selected
        //Sets fields to Round of selected row
        roundTableView.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            if(newSelection!=null)
            {
                roundNumberTextField.setText(String.valueOf(newSelection.getRoundNumber()));
                roundIntervalTextField.setText(String.valueOf(newSelection.getRoundInterval()));
            }
        });

        //Set up combobox repetition types
        repeatTypeDropDown.getItems().addAll(
                "Repeat Last",
                "Start Over",
                "Singular Cycle",
                "Do not repeat"
        );
    }

    @Override
    public String validateName(String name) {
        String errorMessage = "";
        if(name.isEmpty())
        {
            errorMessage = "Rule name is required";
        }
        return errorMessage;
    }

    @Override
    public String validateRepeatType(String repeatType) {
        String errorMessage = "";
        if(repeatType == null || repeatType.isEmpty())
        {
            errorMessage = "Repeat type is required";
        }
        return errorMessage;
    }

    @Override
    public String validateRoundConsistency(ObservableList<Round> listOfRounds) {
        String errorMessage = "";

        Round previousRound = null;
        int missingRound = 0;
        for(Round currentRound:listOfRounds)
        {
            if (previousRound != null)
            {
                missingRound = currentRound.getRoundNumber() - previousRound.getRoundNumber() - 1;
                if (missingRound > 0)
                {
                    for(int i=1;i<=missingRound;i++)
                    {
                        errorMessage += ("Missing round " + (previousRound.getRoundNumber()+i +"\n"));
                    }

                }
            }
            previousRound = currentRound;
        }

        return errorMessage;
    }
}
