package com.example.ontrack.authentication;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.text.Normalizer;

public class RegistrationFormController {

    //Various form fields
    @FXML
    private TextField registrationUsername;
    @FXML
    private TextField registrationEmail;
    @FXML
    private TextField registrationEmailConfirm;
    @FXML
    private TextField registrationPassword;
    @FXML
    private TextField registrationPasswordConfirm;

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
        System.out.println("___________________________________");//For Debugging
        String usernameError = FormValidator.validateUsername(registrationUsername.getText());
        String emailError = FormValidator.validateEmail(registrationEmail.getText());
        String emailConfirmError = FormValidator.validateEmailConfirm(registrationEmail.getText(),registrationEmailConfirm.getText());
        String passwordError = FormValidator.validatePassword(registrationPassword.getText());
        String passwordConfirmError = FormValidator.validatePasswordConfirm(registrationPassword.getText(),registrationPasswordConfirm.getText());

        //If error exist, set label visible and text to error message
        if(!usernameError.isEmpty())
        {
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
            registrationPasswordConfirmErrorLabel.setText(passwordConfirmError);
            registrationPasswordConfirmErrorLabel.setVisible(true);
        }
        else
        {
            registrationPasswordConfirmErrorLabel.setText(passwordConfirmError);
            registrationPasswordConfirmErrorLabel.setVisible(false);
        }

        //For debugging purposes
        System.out.println(usernameError);
        System.out.println(emailError);
        System.out.println(emailConfirmError);
        System.out.println(passwordError);
        System.out.println(passwordConfirmError);

    }
}
