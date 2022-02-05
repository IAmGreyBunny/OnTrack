package com.example.ontrack.task.form.validator;

import com.example.ontrack.task.repetition.Round;
import javafx.collections.ObservableList;

public class RepetitionRuleValidator{

    //Check name
    public static String validateName(String name)
    {
        String errorMessage = "";
        if(name.isEmpty())
        {
            errorMessage = "Rule name is required";
        }
        return errorMessage;
    }

    //Check repeat type
    public static String validateRepeatType(String repeatType)
    {
        String errorMessage = "";
        if(repeatType.isEmpty())
        {
            errorMessage = "Repeat type is required";
        }
        return errorMessage;
    }

    //Check for missing rounds
    public static String validateRoundConsistency(ObservableList<Round> listOfRounds)
    {
        String errorMessage = "";

        Round previousRound = null;
        int missingRound = 0;
        for(Round currentRound:listOfRounds)
        {
            if (previousRound != null)
            {
                missingRound = currentRound.getRoundNumber() - previousRound.getRoundNumber() - 1;
                if (missingRound > 0)
                {
                    for(int i=1;i<=missingRound;i++)
                    {
                        errorMessage += ("Missing round " + (previousRound.getRoundNumber()+i +"\n"));
                    }

                }
            }
            previousRound = currentRound;
        }

        return errorMessage;
    }
}
