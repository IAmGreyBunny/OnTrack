package com.example.ontrack.task.form.validator;

import java.time.LocalDate;

public interface ITaskForm {
    String validateTaskName(String taskName);
    String validateTaskDesc(String taskDesc);
    String validateTaskDate(LocalDate date);
}
