package com.example.ontrack.task.form.add;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.repetition.RepetitionRuleFormValidator;
import com.example.ontrack.repetition.RepetitionRule;
import com.example.ontrack.repetition.Round;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AddRepetitionRuleFormController implements Initializable {
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


    //Initialise default values for the table as example for user
    ObservableList<Round> listOfRounds = FXCollections.observableArrayList(
            new Round(1,1),
            new Round(2,3),
            new Round(3,7)
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Tie attributes of Round class to table columns
        roundNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roundNumber"));
        roundIntervalColumn.setCellValueFactory(new PropertyValueFactory<>("roundInterval"));
        roundIntervalColumn.setSortable(false);

        //Set default values
        roundTableView.setItems(listOfRounds);

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
            for(Round round:listOfRounds)
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
            listOfRounds.add(new Round(roundNumber,roundInterval));
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
        Boolean hasError = false;
        String ruleName = ruleNameTextField.getText();
        String repeatType = repeatTypeDropDown.getValue();
        String ruleNameErrorMessage = RepetitionRuleFormValidator.validateName(ruleName);
        String repeatTypeErrorMessage = RepetitionRuleFormValidator.validateRepeatType(repeatType);
        String roundConsistencyErrorMessage = RepetitionRuleFormValidator.validateRoundConsistency(listOfRounds);

        //Check for form validation errors
        if (!roundConsistencyErrorMessage.isEmpty())
        {
            System.out.println(roundConsistencyErrorMessage);
            hasError = true;
        }

        if (!ruleNameErrorMessage.isEmpty())
        {
            System.out.println(ruleNameErrorMessage);
            hasError = true;
        }

        if (!repeatTypeErrorMessage.isEmpty())
        {
            System.out.println(repeatTypeErrorMessage);
            hasError = true;
        }

        //If form validation is successful
        if(!hasError)
        {
            RepetitionRule repetitionRule = new RepetitionRule(ruleName,repeatType,listOfRounds);
            repetitionRule.createRepetitionRuleInDb(repetitionRule);
            CurrentUser.getInstance().reloadUser();
        }

    }

    //Deletes selected row
    @FXML
    private void onDeleteButtonClick()
    {
        roundTableView.getItems().removeAll(roundTableView.getSelectionModel().getSelectedItems());
    }
}
