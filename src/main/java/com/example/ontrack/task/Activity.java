package com.example.ontrack.task;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;

public class Activity extends Task {
    private int activityId;
    private String venue;
    private LocalDate date;

    //Constructor with partial info for activity, useful for creation of db entry
    public Activity(String name, String desc, String venue, LocalDate date)
    {
        this.taskName=name;
        this.description = desc;
        this.venue=venue;
        this.date = date;
    }

    //Constructor with full info for activity, useful for retrieval of db entry
    public Activity(int activityId,String name, String desc, String venue,Boolean status,LocalDate date)
    {
        this.activityId = activityId;
        this.taskName=name;
        this.description = desc;
        this.venue=venue;
        this.date = date;
        this.status = status;
    }

    //Create lesson in database
    public void createActivityInDb()
    {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        //Add repetition rule into database
        sql = String.format("INSERT INTO Activities(userId,name,description,venue,status,activityDate) VALUES (%s,'%s','%s','%s',%s,'%s')",
                currentUid,
                this.taskName,
                this.description,
                this.venue,
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

    public int getActivityId() throws SQLException {
        if(this.activityId != 0)
        {
            return activityId;
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
            sql = String.format("SELECT * FROM Activities WHERE (name = '%s' AND userId = '%s')",this.taskName,currentUid);
            try{
                PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery();

                //If rule exist, return ruleid
                if (DatabaseHelper.getResultSetSize(resultSet)>=1)
                {
                    this.activityId = resultSet.getInt("activityId");
                    return activityId;
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

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getVenue() {
        return this.venue;
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
