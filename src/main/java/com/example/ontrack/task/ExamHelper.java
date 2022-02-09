package com.example.ontrack.task;

import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class ExamHelper {
    public static ObservableList<Exam> getAllExamsFromDate(int userId,LocalDate date)
    {
        ObservableList<Exam> listOfExams = FXCollections.observableArrayList();

        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        String sql = String.format("SELECT * FROM exams WHERE (userId = '%s' AND examDate='%s')",userId,date.toString());
        try{
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            if (DatabaseHelper.getResultSetSize(resultSet) >=1)
            {
                do {
                    Exam exam = new Exam(resultSet.getInt("examId"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("subject"),
                            resultSet.getString("venue"),
                            resultSet.getBoolean("status"),
                            resultSet.getObject("examDate",LocalDate.class));
                    listOfExams.add(exam);
                }
                while(resultSet.next());

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return listOfExams;
    }
}
