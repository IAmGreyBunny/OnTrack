package com.example.ontrack.task;

import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class LessonHelper {
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
                Lesson lesson = new Lesson(resultSet.getInt("lessonId"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
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
}
