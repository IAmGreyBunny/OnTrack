package com.example.ontrack.task;

import com.example.ontrack.task.repetition.RepeatableTask;

public class Lesson extends RepeatableTask {
    private int lessonId;
    private String subject;
    private String venue;

    @Override
    public Task getPreviousTask(Task currentTask) {
        return null;
    }

    @Override
    public Task getNextTask(Task currentTask) {
        return null;
    }

    @Override
    public Task createNextTask(Task currentTask) {
        return null;
    }
}
