package com.example.ontrack.main;

import com.example.ontrack.Main;
import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.authentication.User;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    User user = CurrentUser.getInstance().getUser();

    private Parent form;
    @FXML
    private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        try{
            form = FXMLLoader.load(Main.class.getResource("task/form/AddTaskForm.fxml"));
            borderPane.setLeft(form);
        }
        catch (IOException ex)
        {

        }
    }
}
