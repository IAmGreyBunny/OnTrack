package com.example.ontrack.authentication;

import com.example.ontrack.Main;
import com.example.ontrack.database.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {

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

    //FOR EASE OF LOGIN DURING TESTING, REMOVED WHEN DONE
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginEmailTextField.setText("TestUser123@gmail.com");
        loginPasswordTextField.setText("P@ssword1234");
    }

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
            User user = User.authenticateUser(email,password);

            //If user credential matched in database
            if(user != null)
            {
                System.out.println("Successfully Logged in\n");
                loginFailErrorLabel.setVisible(false);

                //Close login page
                ((Stage) loginButton.getScene().getWindow()).close(); //Close login page

                //Open main window
                try {
                    //Setup new window
                    Stage stage = new Stage(StageStyle.UNDECORATED);
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main/MainPage.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.setMaximized(true);
                    //stage.setFullScreen(true); //Do Not Use Fullscreen
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                loginFailErrorLabel.setText("Incorrect Login Information : Check Credentials");
                loginFailErrorLabel.setVisible(true);
            }
        }

    }
}
