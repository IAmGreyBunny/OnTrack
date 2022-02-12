package com.example.ontrack.authentication;

import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidator {
    //Various input formats
    private static final Pattern emailRegex = Pattern.compile("^(.+)@(.+)$");
    private static final Pattern usernameRegex = Pattern.compile("^[A-Za-z][A-Za-z0-9_]{8,24}$");
    private static final Pattern passwordRegex = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,30}$");

    //Used for matching regex patterns
    private static Matcher matcher;


    //Methods to validate various input fields,
    //returns a string of the error,
    // return empty string when no error
    static public String validateUsername(String usernameInput){
        //Check for empty field
        if (usernameInput.isEmpty())
        {
            return "*Field is required";
        }

        //Check for length of username
        if(usernameInput.length()<7 || usernameInput.length()>24)
        {
            return "*Username length must be between 8 and 24 characters long";
        }

        //Check for username format
        matcher = usernameRegex.matcher(usernameInput);
        if(!matcher.matches()){
            return "*Username must start with an alphabet and not contain special characters";
        }

        //Check if username
        if(!usernameInput.isEmpty())
        {
            //Database Connection
            DatabaseManager databaseManager = new DatabaseManager();
            Connection connection = databaseManager.getConnection();

            String sql = String.format("SELECT DISTINCT user FROM lessons WHERE (userId = %s)", CurrentUser.getInstance().getUser().getUserId());
            try{
                PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery();
                if (DatabaseHelper.getResultSetSize(resultSet)>=1)
                {
                    do{
                        if(resultSet.getString("username").equals(usernameInput))
                        {
                            return "Username already exist";
                        }
                    }while(resultSet.next());
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }
        return "";
    }

    static public String validateEmail(String emailInput){
        //Check for empty input
        if (emailInput.isEmpty())
        {
            return "*Field is required";
        }

        //Check for email format
        matcher = emailRegex.matcher(emailInput);
        if(!matcher.matches()){
            return "*Email Format is Invalid";
        }

        //Check if email exist
        if(!emailInput.isEmpty())
        {
            //Database Connection
            DatabaseManager databaseManager = new DatabaseManager();
            Connection connection = databaseManager.getConnection();

            String sql = String.format("SELECT DISTINCT user FROM lessons WHERE (userId = %s)", CurrentUser.getInstance().getUser().getUserId());
            try{
                PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery();
                if (DatabaseHelper.getResultSetSize(resultSet)>=1)
                {
                    do{
                        if(resultSet.getString("email").equals(emailInput))
                        {
                            return "Email already exist";
                        }
                    }while(resultSet.next());
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }
        return "";
    }

    static public String validateEmailConfirm(String emailInput, String emailConfirmInput){
        //Check for empty input
        if (emailConfirmInput.isEmpty())
        {
            return "*Field is required";
        }

        //Check to see if email and email confirm are the same
        if(!emailConfirmInput.equals(emailInput))
        {
            return "*Emails are not the same";
        }

        return "";
    }

    static public String validatePassword(String passwordInput){
        //Check for empty input
        if (passwordInput.isEmpty())
        {
            return "*Field is required";
        }

        //Check password length
        if(passwordInput.length()<8 || passwordInput.length()>31)
        {
            return "*Password length must be between 8 and 30 characters long";
        }

        //Check password format
        matcher = passwordRegex.matcher(passwordInput);
        if(!matcher.matches()){
            return "*Password must contain:\n1) At least one digit\n2) An uppercase character\n3) A lowercase character\n4) One Special Character";
        }

        return "";
    }

    static public String validatePasswordConfirm(String passwordInput, String passwordConfirmInput){
        //Check for empty input
        if (passwordConfirmInput.isEmpty())
        {
            return "*Field is required";
        }

        //Check to see if password and confirm password is same
        if(!passwordConfirmInput.equals(passwordInput))
        {
            return "*Passwords are not the same";
        }

        return "";
    }
}
