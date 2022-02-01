package com.example.ontrack.authentication;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthenticationMenuController implements Initializable {

    @FXML
    private VBox authenticationFormVBox;    //This is the where the forms will be loaded upon
    private Parent form;                    //content of form


    //animation and login form on initialisation
    @Override
    public void initialize(URL url, ResourceBundle rb){
        TranslateTransition t = new TranslateTransition(Duration.seconds(0.5),authenticationFormVBox);
        t.setToX(authenticationFormVBox.getLayoutX()+270);
        t.play();
        t.setOnFinished(actionEvent -> {
            try{
                form = FXMLLoader.load(getClass().getResource("LoginForm.fxml"));
                authenticationFormVBox.getChildren().removeAll();
                authenticationFormVBox.getChildren().setAll(form);
            }catch (IOException ex)
            {

            }
        });
    }

    //Function to slide over registration and show login form, tied to button click
    @FXML
    private void showLoginPanel(){
        TranslateTransition t = new TranslateTransition(Duration.seconds(0.5),authenticationFormVBox);
        t.setToX(authenticationFormVBox.getLayoutX()+270);
        t.play();
        t.setOnFinished(actionEvent -> {
            try{
                form = FXMLLoader.load(getClass().getResource("LoginForm.fxml"));
                authenticationFormVBox.getChildren().removeAll();
                authenticationFormVBox.getChildren().setAll(form);
            }catch (IOException ex){

            }
        });
    }

    //Function to slide over login and show registration form, tied to button click
    @FXML
    private void showRegisterPanel(){
        TranslateTransition t = new TranslateTransition(Duration.seconds(0.5),authenticationFormVBox);
        t.setToX(0);
        t.play();
        t.setOnFinished(actionEvent -> {
            try{
                form = FXMLLoader.load(getClass().getResource("RegistrationForm.fxml"));
                authenticationFormVBox.getChildren().removeAll();
                authenticationFormVBox.getChildren().setAll(form);
            }catch (IOException ex){

            }
        });
    }

}
