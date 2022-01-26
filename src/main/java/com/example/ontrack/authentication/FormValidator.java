package com.example.ontrack.authentication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidator {
    private static Pattern emailRegex = Pattern.compile("^(.+)@(.+)$");
    private static Pattern usernameRegex = Pattern.compile("^[A-Za-z][A-Za-z0-9_]{8,24}$");
    private static Pattern passwordRegex = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,30}$");

    private static Matcher matcher;


    //Methods to validate various input fields,
    //returns a string of the error,
    // return empty string when no error
    static public String validateUsername(String usernameInput){
        if (usernameInput.isEmpty())
        {
            return "*Field is required";
        }

        if(usernameInput.length()<7 || usernameInput.length()>24)
        {
            return "*Username length must be between 8 and 24 characters long";
        }

        matcher = usernameRegex.matcher(usernameInput);
        if(!matcher.matches()){
            return "*Username must start with an alphabet and not contain special characters";
        }
        return "";
    }

    static public String validateEmail(String emailInput){
        if (emailInput.isEmpty())
        {
            return "*Field is required";
        }

        matcher = emailRegex.matcher(emailInput);
        if(!matcher.matches()){
            return "*Email Format is Invalid";
        }
        return "";
    }

    static public String validateEmailConfirm(String emailInput, String emailConfirmInput){
        if (emailConfirmInput.isEmpty())
        {
            return "*Field is required";
        }

        if(!emailConfirmInput.equals(emailInput))
        {
            return "*Emails are not the same";
        }

        return "";
    }

    static public String validatePassword(String passwordInput){
        if (passwordInput.isEmpty())
        {
            return "*Field is required";
        }

        if(passwordInput.length()<8 || passwordInput.length()>31)
        {
            return "*Password length must be between 8 and 30 characters long";
        }

        matcher = passwordRegex.matcher(passwordInput);
        if(!matcher.matches()){
            return "*Password must contain:\n1) At least one digit\n2) An uppercase character\n3) A lowercase character\n4) One Special Character";
        }

        return "";
    }

    static public String validatePasswordConfirm(String passwordInput, String passwordConfirmInput){
        if (passwordConfirmInput.isEmpty())
        {
            return "*Field is required";
        }

        if(!passwordConfirmInput.equals(passwordInput))
        {
            return "*Passwords are not the same";
        }

        return "";
    }
}
