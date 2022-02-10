package com.example.ontrack.task.form.validator;

import com.example.ontrack.task.repetition.RepetitionRule;

public interface IRepeatableTaskForm extends ITaskForm{
    String validateRepetitionRule(RepetitionRule repetitionRule);
}
