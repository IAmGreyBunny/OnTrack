package com.example.ontrack.task.form.edit;

import com.example.ontrack.IBackButton;
import com.example.ontrack.Main;
import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.task.form.validator.ILessonForm;
import com.example.ontrack.task.lesson.Lesson;
import com.example.ontrack.task.lesson.LessonCycle;
import com.example.ontrack.task.lesson.LessonHelper;
import com.example.ontrack.task.repetition.RepetitionRule;
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

public class EditLessonFormController implements IBackButton, ILessonForm, Initializable {
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

    Lesson oldLesson;

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

    public void setLesson(Lesson lesson)
    {
        oldLesson=lesson;
        LessonCycle lessonCycle = new LessonCycle(oldLesson.getTaskName());
        oldLesson = lessonCycle.getFirstLessonInCycle();
        lessonNameTextField.setText(lesson.getTaskName());
        lessonDescTextArea.setText(lesson.getDescription());
        lessonSubjectTextField.setText(lesson.getSubject());
        lessonVenueTextField.setText(lesson.getVenue());
        lessonStartDatePicker.setValue(lesson.getDate());
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
        String lessonName = lessonNameTextField.getText();
        String lessonDesc = lessonDescTextArea.getText();
        String lessonSubject = lessonSubjectTextField.getText();
        String lessonVenue = lessonVenueTextField.getText();
        RepetitionRule lessonRepetitionRule = repetitionRuleDropDown.getValue();
        LocalDate lessonStartDate = lessonStartDatePicker.getValue();

        //Create error messages
        String errorMessage = validateTaskName(lessonName)
                +validateTaskDesc(lessonDesc)
                +validateSubject(lessonSubject)
                +validateVenue(lessonVenue)
                +validateRepetitionRule(lessonRepetitionRule)
                +validateTaskDate(lessonStartDate);

        if(!errorMessage.isEmpty())
        {
            //TO DO: ERROR MESSAGE BOX TO BE IMPLEMENTED LATER
            System.out.println(errorMessage);
        }
        else
        {
            Lesson newLesson = new Lesson(lessonName,lessonDesc,lessonSubject,lessonVenue,lessonStartDate,1,false);
            LessonCycle.updateLessonsInCycle(oldLesson,newLesson,oldLesson.getRepetitionRule(),repetitionRuleDropDown.getValue());
//            LessonHelper.createLessonInDb(lesson,lessonRepetitionRule);
//            lesson.setRepetitionRule(lessonRepetitionRule);
//            LessonHelper.createLessonCycleInDb(lesson,lessonRepetitionRule);
        }

    }

    @Override
    public String validateTaskName(String taskName) {
        if (taskName.isEmpty()) {
            return "Name is required";
        }
        return "";
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
    public String validateVenue(String subject) {
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