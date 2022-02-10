package com.example.ontrack.task;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.repetition.RepetitionRule;

import java.sql.*;
import java.time.LocalDate;

public class Exam extends Task {
    private int examId;
    private String subject;
    private String venue;
    private LocalDate date;

    //Constructor with partial info for exam, useful for creation of db entry
    public Exam(String name, String desc, String venue, String subject,LocalDate date)
    {
        this.taskName=name;
        this.description = desc;
        this.subject=subject;
        this.venue=venue;
        this.date = date;
    }

    //Constructor with full info for exam, useful for retrieval of db entry
    public Exam(int examId,String name, String desc, String subject,String venue,Boolean status,LocalDate date)
    {
        this.examId = examId;
        this.taskName=name;
        this.description = desc;
        this.subject=subject;
        this.venue=venue;
        this.date = date;
        this.status = status;
    }

    //Create lesson in database
    public void createExamInDb()
    {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        //Add repetition rule into database
        sql = String.format("INSERT INTO Exams(userId,name,description,venue,subject,status,examDate) VALUES (%s,'%s','%s','%s','%s',%s,'%s')",
                currentUid,
                this.taskName,
                this.description,
                this.venue,
                this.subject,
                0,
                this.date.toString());
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
