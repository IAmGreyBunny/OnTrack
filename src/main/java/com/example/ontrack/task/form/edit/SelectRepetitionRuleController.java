package com.example.ontrack.task.form.edit;

import com.example.ontrack.Main;
import com.example.ontrack.alert.NotificationBox;
import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.task.repetition.RepetitionRule;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SelectRepetitionRuleController implements Initializable {
    @FXML
    ComboBox<RepetitionRule> repetitionRuleDropDown;
    @FXML
    Button editButton;

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
    }

    @FXML
    public void loadEditRepetitionRuleForm()
    {
        //Load Edit Form
        FXMLLoader editRepetitionRuleFormLoader = new FXMLLoader(Main.class.getResource("task/form/edit/EditRepetitionRuleForm.fxml"));
        EditRepetitionRuleFormController editRepetitionRuleFormController;
        Parent editRepetitionRuleForm;
        try {
            if(repetitionRuleDropDown.getValue()!=null)
            {
                editRepetitionRuleForm = editRepetitionRuleFormLoader.load();
                editRepetitionRuleFormController = editRepetitionRuleFormLoader.getController();
                editRepetitionRuleFormController.setRepetitionRule(repetitionRuleDropDown.getValue()); //Load calendar cell content based on date given

                //Display new window
                Stage stage = new Stage();
                Scene scene = new Scene(editRepetitionRuleForm);
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                Stage currentStage = (Stage) editButton.getScene().getWindow();
                currentStage.close();
            }else
            {
                NotificationBox notificationBox = new NotificationBox();
                notificationBox.display("Error","No repetition rule is selected");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
