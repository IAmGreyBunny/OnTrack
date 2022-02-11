package com.example.ontrack.task.exam;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.task.Task;

import java.sql.*;
import java.time.LocalDate;

public class Exam extends Task {
    private int examId;
    private String subject;
    private String venue;
    private LocalDate date;

    //Constructor for cloning object
    public Exam(Exam another) {
        this.examId = another.examId;
        this.taskName=another.taskName;
        this.description = another.description;
        this.venue=another.venue;
        this.date = another.date;
        this.subject = another.subject;
        this.status = another.status;
    }

    //Constructor with partial info for exam, useful for creation of db entry
    public Exam(String name, String desc, String subject,String venue, LocalDate date,Boolean status)
    {
        this.taskName=name;
        this.description = desc;
        this.subject=subject;
        this.venue=venue;
        this.date = date;
        this.status=status;
    }

    //Constructor with full info for exam, useful for retrieval of db entry
    public Exam(int examId,String name, String desc, String subject,String venue,LocalDate date,Boolean status)
    {
        this.examId = examId;
        this.taskName=name;
        this.description = desc;
        this.subject=subject;
        this.venue=venue;
        this.date = date;
        this.status = status;
    }

    public int getExamId(){
        if(this.examId != 0)
        {
            return examId;
        }
        else
        {
            //Gets connection to database
            DatabaseManager databaseManager = new DatabaseManager();
            Connection connection = databaseManager.getConnection();
            String sql = "";

            //Get current user id
            int currentUid = CurrentUser.getInstance().getUser().getUserId();

            //Look for rule name with the same user id as current user
            sql = String.format("SELECT * FROM Exams WHERE (name = '%s' AND userId = '%s')",this.taskName,currentUid);
            try{
                PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery();

                //If rule exist, return ruleid
                if (DatabaseHelper.getResultSetSize(resultSet)>=1)
                {
                    this.examId = resultSet.getInt("examId");
                    return examId;
                }
                else
                {
                    return 0;
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                return 0;
            }
            finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
