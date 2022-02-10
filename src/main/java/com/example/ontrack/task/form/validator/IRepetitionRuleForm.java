package com.example.ontrack.task.form.validator;

import com.example.ontrack.task.repetition.Round;
import javafx.collections.ObservableList;

public interface IRepetitionRuleForm {
    String validateName(String name);
    String validateRepeatType(String repeatType);
    String validateRoundConsistency(ObservableList<Round> listOfRounds);
}
