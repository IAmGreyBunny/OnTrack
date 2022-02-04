package com.example.ontrack.task.form;

import com.example.ontrack.task.repetition.Round;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class RepetitionRuleFormController implements Initializable {
    @FXML
    Button addApplyButton;
    @FXML
    Button deleteButton;
    @FXML
    TextField roundNumberTextField;
    @FXML
    TextField roundIntervalTextField;

    @FXML
    private TableView<Round> roundTableView;
    @FXML
    private TableColumn<Round,Integer> roundNumber;
    @FXML
    private TableColumn<Round,Integer> roundInterval;

    private Round currentSelectedRound;

    //Initialise default values for the table as example for user
    ObservableList<Round> listOfRounds = FXCollections.observableArrayList(
            new Round(1,1),
            new Round(2,3),
            new Round(3,7)
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Tie attributes of Round class to table columns
        roundNumber.setCellValueFactory(new PropertyValueFactory<>("roundNumber"));
        roundInterval.setCellValueFactory(new PropertyValueFactory<>("roundInterval"));

        //Set default values
        roundTableView.setItems(listOfRounds);

        //Adds listener row, activates when row is selected
        //Sets fields to Round of selected row
        roundTableView.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection)->{
            if(newSelection!=null)
            {
                roundNumberTextField.setText(String.valueOf(newSelection.getRoundNumber()));
                roundIntervalTextField.setText(String.valueOf(newSelection.getRoundInterval()));
                currentSelectedRound = obs.getValue();
            }
        });
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
        if(roundNumber>=1 && roundInterval>=1)
        {
            //If round number exist, change its round interval
            for(Round round:listOfRounds)
            {
                if(round.getRoundNumber()==roundNumber)
                {
                    round.setRoundInterval(roundInterval);
                    roundTableView.refresh();
                    return;
                }
            }

            //If round does not exist,create a new one
            listOfRounds.add(new Round(roundNumber,roundInterval));
            roundTableView.refresh();
        }
    }

    //Deletes selected row
    @FXML
    private void onDeleteButtonClick()
    {
        roundTableView.getItems().removeAll(roundTableView.getSelectionModel().getSelectedItems());
    }
}
