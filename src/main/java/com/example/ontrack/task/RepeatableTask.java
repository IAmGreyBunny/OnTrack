package com.example.ontrack.task;

public abstract class RepeatableTask extends Task{
    private int round;
    private RepetitionRule repetitionRule;

    public abstract Task getPreviousTask(Task currentTask);
    public abstract Task getNextTask(Task currentTask);
    public abstract Task createNextTask(Task currentTask);

}
