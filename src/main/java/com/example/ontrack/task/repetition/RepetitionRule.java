package com.example.ontrack.task.repetition;

import javafx.collections.ObservableList;

import java.util.List;

public class RepetitionRule {
    private int userId;
    private String ruleName;
    private String repeatType;
    private ObservableList<Round> rounds;

    public RepetitionRule(String ruleName,String repeatType,ObservableList<Round> rounds){
        this.ruleName = ruleName;
        this.repeatType = repeatType;
        this.rounds = rounds;
    }

    //Create repetition rule in database
    public static boolean createRepetitionRule(RepetitionRule repetitionRule)
    {
        return true;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(String repeatType) {
        this.repeatType = repeatType;
    }

    public ObservableList<Round> getRounds() {
        return rounds;
    }

    public void setRounds(ObservableList<Round> rounds) {
        this.rounds = rounds;
    }
}
