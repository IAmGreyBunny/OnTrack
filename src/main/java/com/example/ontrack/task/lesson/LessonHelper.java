package com.example.ontrack.task.lesson;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.task.repetition.RepetitionRule;
import com.example.ontrack.task.repetition.RepetitionRuleHelper;
import com.example.ontrack.task.repetition.Round;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class LessonHelper {

    //Get all lessons given a date
    public static ObservableList<Lesson> getAllLessonsFromDate(int userId,LocalDate date)
    {
        ObservableList<Lesson> listOfLessons = FXCollections.observableArrayList();

        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        String sql = String.format("SELECT * FROM lessons WHERE (userId = '%s' AND lessonDate='%s')",userId,date.toString());
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
                            resultSet.getObject("lessonDate",LocalDate.class),
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

    public static ObservableList<Lesson> getAllLessonsFromDate(LocalDate date)
    {
        ObservableList<Lesson> listOfLessons = FXCollections.observableArrayList();

        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        String sql = String.format("SELECT * FROM lessons WHERE (userId = '%s' AND lessonDate='%s')",currentUid,date.toString());
        try{
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            if (DatabaseHelper.getResultSetSize(resultSet) >=1)
            {
                Lesson lesson = new Lesson(
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("subject"),
                        resultSet.getString("venue"),
                        resultSet.getInt("repetitionRuleId"),
                        resultSet.getObject("lessonDate",LocalDate.class),
                        resultSet.getInt("round"),
                        resultSet.getBoolean("status"));
                listOfLessons.add(lesson);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return listOfLessons;
    }

    //Create lesson in database
    public static void createLessonInDb(Lesson lesson, RepetitionRule repetitionRule) {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        sql = String.format("INSERT INTO lessons(userId,name,description,subject,venue,repetitionRuleId,lessonDate,round,status) " +
                        "VALUES (%s,'%s','%s','%s','%s',%s,'%s',%s,%s)",
                currentUid,
                lesson.getTaskName(),
                lesson.getDescription(),
                lesson.getSubject(),
                lesson.getVenue(),
                repetitionRule.getRuleId(),
                lesson.getDate().toString(),
                lesson.getCurrentRound(),
                lesson.getStatus());
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        lesson.setRepetitionRule(repetitionRule);
    }

    //Create subsequent lesson in database
    public static void createLessonCycleInDb(Lesson firstLessonInCycle, RepetitionRule repetitionRule) throws SQLException {
        //Get rounds
        ObservableList<Round> rounds = repetitionRule.getRounds();
        int roundInterval=0;
        for(Round round : rounds)
        {
            int roundNumber = round.getRoundNumber()+1;
            roundInterval += round.getRoundInterval();
            Lesson lesson = new Lesson(firstLessonInCycle.getTaskName(),
                    firstLessonInCycle.getDescription(),
                    firstLessonInCycle.getSubject(),
                    firstLessonInCycle.getVenue(),
                    firstLessonInCycle.getRuleId(),
                    firstLessonInCycle.getDate().plusDays(roundInterval),
                    roundNumber,
                    false);
            createLessonInDb(lesson,repetitionRule);
            lesson.setRepetitionRule(repetitionRule);
        }
    }

    //Update lesson in database
    public static void updateLessonInDb(Lesson oldLesson,Lesson newLesson)
    {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        sql = String.format("UPDATE lessons SET name='%s',description='%s',subject='%s',venue='%s',status=%s,round=%s,lessonDate='%s' WHERE lessonId=%s",
                newLesson.getTaskName(),
                newLesson.getDescription(),
                newLesson.getSubject(),
                newLesson.getVenue(),
                newLesson.getStatus(),
                newLesson.getCurrentRound(),
                newLesson.getDate().toString(),
                oldLesson.getLessonId());
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

}
