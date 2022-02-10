package com.example.ontrack.task.form.validator;

import java.time.LocalDate;

public class RevisionTaskFormValidator extends RepeatableTaskFormValidator {

    public static String validateSubject(String subject)
    {
        return "";
    }

    public static String validateDate(LocalDate date)
    {
        if(date == null)
        {
            return "date is required";
        }
        return "";
    }
}
