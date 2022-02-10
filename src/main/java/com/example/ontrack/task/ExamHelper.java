package com.example.ontrack.task;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
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
                            resultSet.getObject("examDate",LocalDate.class),
                            resultSet.getBoolean("status"));
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

    public static ObservableList<Exam> getAllExamsFromDate(LocalDate date)
    {
        ObservableList<Exam> listOfExams = FXCollections.observableArrayList();

        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        String sql = String.format("SELECT * FROM exams WHERE (userId = '%s' AND examDate='%s')",currentUid,date.toString());
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
                            resultSet.getObject("examDate",LocalDate.class),
                            resultSet.getBoolean("status"));
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

    //Create Exam in database
    public static void createExamInDb(Exam exam)
    {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        //Add Exam to database
        sql = String.format("INSERT INTO Exams(userId,name,description,venue,subject,status,examDate) VALUES (%s,'%s','%s','%s','%s',%s,'%s')",
                currentUid,
                exam.getTaskName(),
                exam.getDescription(),
                exam.getVenue(),
                exam.getSubject(),
                exam.getStatus(),
                exam.getDate().toString());
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Update Exam in database
    public static void updateExamInDb(Exam oldExam,Exam newExam)
    {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        //Edit exam entry in database
        sql = String.format("UPDATE Exams SET userId=%s,name='%s',description='%s',venue='%s',subject='%s',status=%s,examDate='%s' WHERE (examId = '%s')",
                currentUid,
                newExam.getTaskName(),
                newExam.getDescription(),
                newExam.getVenue(),
                newExam.getSubject(),
                oldExam.getStatus(),
                newExam.getDate().toString(),
                oldExam.getExamId());
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
