package com.example.ontrack.task.repetition;

import com.example.ontrack.task.Task;

public abstract class RepeatableTask extends Task {
    private int round;
    private RepetitionRule repetitionRule;

    public abstract Task getPreviousTask(Task currentTask);
    public abstract Task getNextTask(Task currentTask);
    public abstract Task createNextTask(Task currentTask);

}
