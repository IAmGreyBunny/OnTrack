package com.example.ontrack.task.form.validator;

import com.example.ontrack.repetition.RepetitionRule;
import com.example.ontrack.repetition.RepetitionRuleFormValidator;

public class RepeatableTaskFormValidator extends TaskFormValidator {
    public static String validateRepetitionRule(RepetitionRule repetitionRule)
    {
        if(repetitionRule != null)
        {
            Boolean hasError = false;
            String ruleNameErrorMessage = RepetitionRuleFormValidator.validateName(repetitionRule.getRuleName());
            String repeatTypeErrorMessage = RepetitionRuleFormValidator.validateRepeatType(repetitionRule.getRepeatType());
            String roundConsistencyErrorMessage = RepetitionRuleFormValidator.validateRoundConsistency(repetitionRule.getRounds());

            //Check for form validation errors
            if (!roundConsistencyErrorMessage.isEmpty())
            {
                System.out.println(roundConsistencyErrorMessage);
                hasError = true;
            }

            if (!ruleNameErrorMessage.isEmpty())
            {
                System.out.println(ruleNameErrorMessage);
                hasError = true;
            }

            if (!repeatTypeErrorMessage.isEmpty())
            {
                System.out.println(repeatTypeErrorMessage);
                hasError = true;
            }

            //If form validation is successful
            if(!hasError)
            {
                return "";
            }
            else
            {
                String errorMessage = ruleNameErrorMessage +"\n" + repeatTypeErrorMessage +"\n" + roundConsistencyErrorMessage;
                return errorMessage;
            }
        }
        else
        {
            return "";
        }

    }
}
