package com.example.ontrack;

import com.example.ontrack.alert.ConfirmationBox;
import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.authentication.User;
import com.example.ontrack.overview.calendar.CalendarControllerHolder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    User user = CurrentUser.getInstance().getUser();

    private Parent form;
    private Parent taskOverview;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Tab calendarTab;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        try{
            form = FXMLLoader.load(Main.class.getResource("task/form/add/AddTaskForm.fxml"));
            borderPane.setLeft(form);
            FXMLLoader calendarLoader = new FXMLLoader(Main.class.getResource("overview/calendar/Calendar.fxml"));
            taskOverview = calendarLoader.load();
            CalendarControllerHolder.getInstance().setCalendarController(calendarLoader.getController());
            calendarTab.setContent(taskOverview);
        }
        catch (IOException ex)
        {

        }
    }

    @FXML
    private void onExit()
    {
        ConfirmationBox confirmationBox = new ConfirmationBox();
        if(confirmationBox.display("Exit","Are you sure you want to quit?"))
        {
            Platform.exit();
        }

    }

    @FXML
    private void onLogout()
    {
        CurrentUser.getInstance().setUser(null);
        //If user credential matched in database
        if(user != null)
        {
            //Close login page
            ((Stage) borderPane.getScene().getWindow()).close(); //Close Main Page

            //Open main window
            try {
                //Setup new window
                Stage stage = new Stage(StageStyle.UNDECORATED);
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("authentication/AuthenticationPage.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void loadAddTaskForm() {
        try {
            form = FXMLLoader.load(Main.class.getResource("task/form/add/AddTaskForm.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        borderPane.setLeft(form);
    }

    @FXML
    public void loadSelectRepetitionRule() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("task/form/edit/SelectRepetitionRule.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
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

    public void refresh()
    {
        try {
            taskOverview = FXMLLoader.load(Main.class.getResource("overview/calendar/Calendar.fxml"));
            calendarTab.setContent(taskOverview);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
