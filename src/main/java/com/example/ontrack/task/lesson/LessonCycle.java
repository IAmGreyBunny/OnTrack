package com.example.ontrack.task.lesson;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.task.repetition.RepetitionRule;
import com.example.ontrack.task.repetition.RepetitionRuleHelper;
import com.example.ontrack.task.repetition.Round;
import com.example.ontrack.task.revision.Revision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class LessonCycle {
    ObservableList<Lesson> lessonsInCycle;
    String lessonCycleName;//same as name of every lesson in the cycle

    public LessonCycle(String lessonCycleName)
    {
        this.lessonCycleName = lessonCycleName;
        lessonsInCycle = getUserLessonCycleWithName(lessonCycleName);
    }

    //Delete all lessons in cycle from database
    public void deleteLessonCycle() {
        for (Lesson lesson : lessonsInCycle)
        {
            //Gets connection to database
            DatabaseManager databaseManager = new DatabaseManager();
            Connection connection = databaseManager.getConnection();
            String sql = "";

            sql = String.format("DELETE FROM lessons WHERE (lessonId=%s)",
                    lesson.getLessonId()); //+1 because sql is not zero-indexed
            try{
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    //Get First Lesson in cycle
    public Lesson getFirstLessonInCycle()
    {
        return lessonsInCycle.get(0);
    }

    //Get Last Lesson in cycle
    public Lesson getLastLessonInCycle()
    {
        return lessonsInCycle.get(lessonsInCycle.size()-1);
    }

    //Get Last completed lesson in cycle
    public Lesson getLastCompletedLessonInCycle()
    {
        Lesson lastCompletedLesson =  getFirstLessonInCycle();
        for(Lesson lesson:lessonsInCycle)
        {
            if(lesson.getStatus())
            {
                lastCompletedLesson=lesson;
            }
        }
        return lastCompletedLesson;
    }

    //Get all user lesson with name
    public ObservableList<Lesson> getUserLessonCycleWithName(String name)
    {
        ObservableList<Lesson> listOfLessons = FXCollections.observableArrayList();

        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        String sql = String.format("SELECT * FROM lessons WHERE (userId = '%s' AND name='%s')",
                CurrentUser.getInstance().getUser().getUserId(),
                name);
        try{
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            if (DatabaseHelper.getResultSetSize(resultSet) >=1)
            {
                do{
                    Lesson lesson = new Lesson(
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("subject"),
                            resultSet.getString("venue"),
                            resultSet.getInt("repetitionRuleId"),
                            resultSet.getObject("lessonDate", LocalDate.class),
                            resultSet.getInt("round"),
                            resultSet.getBoolean("status"));
                    listOfLessons.add(lesson);
                }while(resultSet.next());

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return listOfLessons;
    }

    //Get a percentage of user completion rate for a cycle
    public double getCompletionRateOfLessonCycle()
    {
        double completed=0;
        for(Lesson lesson:lessonsInCycle)
        {
            if(lesson.getStatus())
            {
                completed++;
            }
        }
        return (completed/lessonsInCycle.size())*100.0;
    }

    //For start over repeat type
    public void restartCycle(RepetitionRule repetitionRule)
    {
        Lesson lastCompletedLesson = new Lesson(getLastCompletedLessonInCycle());
        Lesson newLastCompletedLesson = new Lesson(lastCompletedLesson);
        newLastCompletedLesson.setCurrentRound(1);//Restart
        newLastCompletedLesson.setStatus(false);
        LessonHelper.updateLessonInDb(lastCompletedLesson,newLastCompletedLesson);
        LessonHelper.createLessonInDb(newLastCompletedLesson,repetitionRule);
        lastCompletedLesson.setRepetitionRule(repetitionRule);
        deleteLessonCycle();//This deletes current cycle from db to make new ones
        try {
            LessonHelper.createLessonCycleInDb(lastCompletedLesson,repetitionRule);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        getUserLessonCycleWithName(lastCompletedLesson.getTaskName());
    }

    //For expansion of cycle
    public void extendCycle(Lesson lastLessonInCycle, RepetitionRule repetitionRule,int numberOfTimes)
    {
        //Get round
        ObservableList<Round> rounds = repetitionRule.getRounds();
        Round lastRound = rounds.get(rounds.size()-1);
        int roundNumber = lastLessonInCycle.getCurrentRound()+1;
        int roundInterval=0;
        for(int i = roundNumber;i<roundNumber+numberOfTimes;i++)
        {
            roundInterval += lastRound.getRoundInterval();
            Lesson lesson = new Lesson(lastLessonInCycle.getTaskName(),
                    lastLessonInCycle.getDescription(),
                    lastLessonInCycle.getSubject(),
                    lastLessonInCycle.getVenue(),
                    lastLessonInCycle.getRuleId(),
                    lastLessonInCycle.getDate().plusDays(roundInterval),
                    i,
                    false);
            LessonHelper.createLessonInDb(lesson,repetitionRule);
            lesson.setRepetitionRule(repetitionRule);
        }
    }
}
