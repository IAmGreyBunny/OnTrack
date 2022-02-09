package com.example.ontrack.task.form.edit;

import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.repetition.RepetitionRule;
import com.example.ontrack.task.Lesson;
import com.example.ontrack.task.form.validator.LessonTaskFormValidator;
import javafx.collections.ObservableList;
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

public class EditLessonFormController implements IBackButton, Initializable {
    @FXML
    Button backButton;
    @FXML
    Button editRepetitionRuleButton;
    @FXML
    Button addRepetitionRuleButton;
    @FXML
    Button saveTaskButton;

    @FXML
    TextField lessonNameTextField;
    @FXML
    TextArea lessonDescTextArea;
    @FXML
    TextField lessonSubjectTextField;
    @FXML
    TextField lessonVenueTextField;
    @FXML
    DatePicker lessonStartDatePicker;

    @FXML
    ComboBox<RepetitionRule> repetitionRuleDropDown;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {

        //Setup repetitionRuleDropDown combobox with user listofrepetitionrules
        //String converter required for displaying RepetitionRule object as string
        ObservableList<RepetitionRule> listOfRepetitionRules = RepetitionRule.getUserRepetitionRules();
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
        String lessonName = lessonNameTextField.getText();
        String lessonDesc = lessonDescTextArea.getText();
        String lessonSubject = lessonSubjectTextField.getText();
        String lessonVenue = lessonVenueTextField.getText();
        RepetitionRule lessonRepetitionRule = repetitionRuleDropDown.getValue();
        LocalDate lessonStartDate = lessonStartDatePicker.getValue();

        //Create error messages
        String lessonNameError = "";
        String lessonDescError = "";
        String lessonSubjectError = "";
        String lessonVenueError = "";
        String lessonRepetitionRuleError = "";
        String errorMessage = "";

        //Validate user input
        lessonNameError = LessonTaskFormValidator.validateTaskName(lessonName);
        lessonDescError = LessonTaskFormValidator.validateTaskDesc(lessonDesc);
        lessonSubjectError = LessonTaskFormValidator.validateSubject(lessonSubject);
        lessonVenueError = LessonTaskFormValidator.validateVenue(lessonVenue);
        lessonRepetitionRuleError = LessonTaskFormValidator.validateRepetitionRule(lessonRepetitionRule);

        if(!lessonNameError.isEmpty())
        {
            errorMessage += lessonNameError + "\n";
        }
        if(!lessonDescError.isEmpty())
        {
            errorMessage += lessonDescError + "\n";
        }
        if(!lessonSubjectError.isEmpty())
        {
            errorMessage += lessonSubjectError + "\n";
        }
        if(!lessonVenueError.isEmpty())
        {
            errorMessage += lessonVenueError + "\n";
        }
        if(!lessonRepetitionRuleError.isEmpty())
        {
            errorMessage += lessonRepetitionRuleError + "\n";
        }

        if(!errorMessage.isEmpty())
        {
            //TO DO: ERROR MESSAGE BOX TO BE IMPLEMENTED LATER
            System.out.println(errorMessage);
        }
        else
        {
            Lesson lesson = new Lesson(lessonName,lessonDesc,lessonSubject,lessonVenue,lessonRepetitionRule,lessonStartDate);
            lesson.createLessonInDb();
            lesson.setRepetitionRule(lessonRepetitionRule);
            lesson.createLessonCycleInDb();
        }

    }

}
