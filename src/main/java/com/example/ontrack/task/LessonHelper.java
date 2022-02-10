package com.example.ontrack.task;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.repetition.RepetitionRule;
import com.example.ontrack.repetition.Round;
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
    public static void createLessonInDb(Lesson lesson) {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        sql = String.format("INSERT INTO lessons(userId,name,description,venue,status,round,lessonDate) VALUES (%s,'%s','%s','%s',%s,%s,'%s')",
                currentUid,
                lesson.getTaskName(),
                lesson.getDescription(),
                lesson.getVenue(),
                0,
                lesson.currentRound,
                lesson.getDate().toString());
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
                    firstLessonInCycle.getVenue(),
                    firstLessonInCycle.getSubject(),
                    firstLessonInCycle.getRuleId(),
                    firstLessonInCycle.getDate().plusDays(roundInterval),
                    roundNumber,
                    false);
            createLessonInDb(lesson);
            lesson.setRepetitionRule(repetitionRule);
        }
    }
}
