package com.example.ontrack.authentication;

import com.example.ontrack.Main;
import com.example.ontrack.database.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class LoginFormController {

    //Various form fields
    @FXML
    private TextField loginEmailTextField;
    @FXML
    private TextField loginPasswordTextField;

    //Button
    @FXML
    private Button loginButton;

    //Error Labels
    @FXML
    private Label loginEmailErrorLabel;
    @FXML
    private Label loginPasswordErrorLabel;
    @FXML
    private Label loginFailErrorLabel;

    @FXML
    private void onLogin()
    {
        //Flag for form validation
        Boolean hasError = false;

        //Gets User input
        String email = loginEmailTextField.getText();
        String password = loginPasswordTextField.getText();

        //Check if any field is empty
        if(email.isEmpty())
        {
            hasError=true;
            loginEmailErrorLabel.setText("*Field is required");
            loginEmailErrorLabel.setVisible(true);
        }
        else
        {
            loginEmailErrorLabel.setVisible(false);
        }

        if(password.isEmpty())
        {
            hasError=true;
            loginPasswordErrorLabel.setText("*Field is required");
            loginPasswordErrorLabel.setVisible(true);
        }
        else
        {
            loginPasswordErrorLabel.setVisible(false);
        }


        //Form validation successful
        if(hasError==false)
        {
            //Database connection, attempts to log in user
            DatabaseManager databaseManager = new DatabaseManager();
            Connection database = databaseManager.getConnection();
            Boolean userAuthenticationSuccess = User.authenticateUser(database,email,password);

            //If user credential matched in database
            if(userAuthenticationSuccess)
            {
                System.out.println("Successfully Logged in");
                loginFailErrorLabel.setVisible(false);

                //Close login page
                ((Stage) loginButton.getScene().getWindow()).close(); //Close login page

                //Open main window
                Stage mainWindow = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main/MainPage.fxml"));
                Scene mainScene = null;
                try {
                    //Setup new window
                    mainScene = new Scene(fxmlLoader.load());
                    mainWindow.setTitle("OnTrack");
                    mainWindow.setScene(mainScene);
                    mainWindow.setResizable(false);
                    mainWindow.setFullScreen(true);
                    mainWindow.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                System.out.println("Log in unsuccessful");
                loginFailErrorLabel.setText("Incorrect Login Information : Check Credentials");
                loginFailErrorLabel.setVisible(true);
            }
        }

    }
}
