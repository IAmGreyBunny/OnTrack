package com.example.ontrack.repetition;

public class Round {
    private int roundNumber;
    private int roundInterval;

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getRoundInterval() {
        return roundInterval;
    }

    public void setRoundInterval(int roundInterval) {
        this.roundInterval = roundInterval;
    }

    public Round(int roundNumber, int roundInterval)
    {
        this.roundNumber = roundNumber;
        this.roundInterval = roundInterval;
    }
}
