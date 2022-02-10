package com.example.ontrack.task.form.validator;

import java.time.LocalDate;

public interface ILessonForm extends ITaskForm,IRepeatableTaskForm{
    String validateSubject(String subject);
    String validateVenue(String subject);
}
