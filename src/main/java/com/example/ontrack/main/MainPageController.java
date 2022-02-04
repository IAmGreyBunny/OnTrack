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
        System.out.println();
        System.out.println("Current User");
        System.out.println("----------------------------------------");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Password: " + user.getPassword());

        try{
            form = FXMLLoader.load(Main.class.getResource("task/AddTaskForm.fxml"));
            borderPane.setLeft(form);
        }
        catch (IOException ex)
        {

        }
    }
}
