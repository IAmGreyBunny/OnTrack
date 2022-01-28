package com.example.ontrack.authentication;

import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.database.databaseobjects.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;

public class LoginFormController {

    //Various form fields
    @FXML
    private TextField loginEmailTextField;
    @FXML
    private TextField loginPasswordTextField;

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
        Boolean hasError = false;

        //Gets User input
        String email = loginEmailTextField.getText();
        String password = loginPasswordTextField.getText();

        //If error exist, set label visible and text to error message
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
            //Database connection
            DatabaseManager databaseManager = new DatabaseManager();
            Connection database = databaseManager.getConnection();
            Boolean userAuthenticationSuccess = User.authenticateUser(database,email,password);
            if(userAuthenticationSuccess)
            {
                //TO DO : Logs in complete
                System.out.println("Successfully Logged in");
                loginFailErrorLabel.setVisible(false);
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
