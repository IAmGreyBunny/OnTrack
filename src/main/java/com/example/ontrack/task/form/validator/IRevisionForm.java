package com.example.ontrack.task.form.validator;

import java.time.LocalDate;

public interface IRevisionForm extends ITaskForm,IRepeatableTaskForm{
    String validateSubject(String subject);
}
