package com.example.ontrack.authentication;

import com.example.ontrack.repetition.RepetitionRule;
import com.example.ontrack.repetition.RepetitionRuleHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CurrentUser {
    private User user;
    private ObservableList<RepetitionRule> userRepetitionRules = FXCollections.observableArrayList();
    private final static CurrentUser INSTANCE = new CurrentUser();

    private CurrentUser(){}//Prevents creation of any more instances

    public static CurrentUser getInstance()
    {
        return INSTANCE;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ObservableList<RepetitionRule> getUserRepetitionRules() {
        return userRepetitionRules;
    }

    //Method used to set repetition rules from database on initial startup
    public void loadUserRepetitionRules() {
        this.userRepetitionRules = RepetitionRuleHelper.getUserRepetitionRules();
    }

    //Method for refreshing user
    public void reloadUser()
    {
        this.userRepetitionRules = RepetitionRuleHelper.getUserRepetitionRules();
    }
}
