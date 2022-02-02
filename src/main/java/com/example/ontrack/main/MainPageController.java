package com.example.ontrack.main;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.authentication.User;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController implements Initializable {
    User user = CurrentUser.getInstance().getUser();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        System.out.println("Current User");
        System.out.println("----------------------------------------");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Password: " + user.getPassword());
    }
}
