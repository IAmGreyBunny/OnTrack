package com.example.ontrack.task;

import com.example.ontrack.repetition.RepetitionRule;

public abstract class RepeatableTask extends Task {
    protected int currentRound;
    protected RepetitionRule repetitionRule;

    public int getCurrentRound() {
        return currentRound;
    }

    public void setCurrentRound(int round) {
        this.currentRound = round;
    }

    public RepetitionRule getRepetitionRule() {
        return repetitionRule;
    }

    public void setRepetitionRule(RepetitionRule repetitionRule) {
        this.repetitionRule = repetitionRule;
    }

    public abstract Task getPreviousTask(Task currentTask);
    public abstract Task getNextTask(Task currentTask);
    public abstract Task createNextTask(Task currentTask);

}
