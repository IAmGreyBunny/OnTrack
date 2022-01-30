package com.example.ontrack.authentication;

import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.database.databaseobjects.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;

public class RegistrationFormController {

    //Various form fields
    @FXML
    private TextField registrationUsernameTextField;
    @FXML
    private TextField registrationEmailTextField;
    @FXML
    private TextField registrationEmailConfirmTextField;
    @FXML
    private TextField registrationPasswordTextField;
    @FXML
    private TextField registrationPasswordConfirmTextField;

    //Error Labels
    @FXML
    private Label registrationUsernameErrorLabel;
    @FXML
    private Label registrationEmailErrorLabel;
    @FXML
    private Label registrationEmailConfirmErrorLabel;
    @FXML
    private Label registrationPasswordErrorLabel;
    @FXML
    private Label registrationPasswordConfirmErrorLabel;

    @FXML
    private void onRegistration()
    {
        //Gets User input
        String username = registrationUsernameTextField.getText();
        String email = registrationEmailTextField.getText();
        String emailConfirm = registrationEmailConfirmTextField.getText();
        String password = registrationPasswordTextField.getText();
        String passwordConfirm = registrationPasswordConfirmTextField.getText();

        //Validates user input, returns error message
        System.out.println("___________________________________");//For Debugging
        Boolean hasError = false;
        String usernameError = FormValidator.validateUsername(username);
        String emailError = FormValidator.validateEmail(email);
        String emailConfirmError = FormValidator.validateEmailConfirm(email,emailConfirm);
        String passwordError = FormValidator.validatePassword(password);
        String passwordConfirmError = FormValidator.validatePasswordConfirm(password,passwordConfirm);

        //If error message exist, set label visible and text to error message
        if(!usernameError.isEmpty())
        {
            hasError=true;
            registrationUsernameErrorLabel.setText(usernameError);
            registrationUsernameErrorLabel.setVisible(true);
        }
        else
        {
            registrationUsernameErrorLabel.setText(usernameError);
            registrationUsernameErrorLabel.setVisible(false);
        }

        if(!emailError.isEmpty())
        {
            hasError=true;
            registrationEmailErrorLabel.setText(emailError);
            registrationEmailErrorLabel.setVisible(true);
        }
        else
        {
            registrationEmailErrorLabel.setText(emailError);
            registrationEmailErrorLabel.setVisible(false);
        }

        if(!emailConfirmError.isEmpty())
        {
            hasError=true;
            registrationEmailConfirmErrorLabel.setText(emailConfirmError);
            registrationEmailConfirmErrorLabel.setVisible(true);
        }
        else
        {
            registrationEmailConfirmErrorLabel.setText(emailConfirmError);
            registrationEmailConfirmErrorLabel.setVisible(false);
        }

        if(!passwordError.isEmpty())
        {
            hasError=true;
            registrationPasswordErrorLabel.setText(passwordError);
            registrationPasswordErrorLabel.setVisible(true);
        }
        else
        {
            registrationPasswordErrorLabel.setText(passwordError);
            registrationPasswordErrorLabel.setVisible(false);
        }

        if(!passwordConfirmError.isEmpty())
        {
            hasError=true;
            registrationPasswordConfirmErrorLabel.setText(passwordConfirmError);
            registrationPasswordConfirmErrorLabel.setVisible(true);
        }
        else
        {
            registrationPasswordConfirmErrorLabel.setText(passwordConfirmError);
            registrationPasswordConfirmErrorLabel.setVisible(false);
        }

        //Form validation successful, Creates an account in database
        if(hasError==false)
        {
            //Database connection
            DatabaseManager databaseManager = new DatabaseManager();
            Connection database = databaseManager.getConnection();
            Boolean registrationSuccess = User.registerUser(database,username,email,password);
            if(registrationSuccess)
            {
              //TO DO: registration complete alert box
            }
        }
    }
}
