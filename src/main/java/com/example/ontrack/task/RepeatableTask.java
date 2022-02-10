package com.example.ontrack.task;

import com.example.ontrack.task.repetition.RepetitionRule;

public abstract class RepeatableTask extends Task {
    protected int currentRound;
    protected RepetitionRule repetitionRule;
    protected int ruleId;

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

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }
}
