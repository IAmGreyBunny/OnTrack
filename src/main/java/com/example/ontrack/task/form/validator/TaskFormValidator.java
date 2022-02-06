package com.example.ontrack.task.form.validator;

public class TaskFormValidator {

    public static String validateTaskName(String taskName)
    {
        if(taskName.isEmpty()) {
            return "Name is required";
        }
        return "";
    }

    public static String validateTaskDesc(String taskDesc)
    {
        return "";
    }
}
