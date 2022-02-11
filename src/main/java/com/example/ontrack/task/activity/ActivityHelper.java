package com.example.ontrack.task.activity;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class ActivityHelper {
    public static ObservableList<Activity> getAllActivtiesFromDate(int userId,LocalDate date)
    {
        ObservableList<Activity> listOfActivity = FXCollections.observableArrayList();

        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        String sql = String.format("SELECT * FROM activities WHERE (userId = '%s' AND activityDate='%s')",userId,date.toString());
        try{
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            if (DatabaseHelper.getResultSetSize(resultSet) >=1)
            {
                do {
                    Activity activity = new Activity(resultSet.getInt("activityId"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("venue"),
                            resultSet.getObject("activityDate",LocalDate.class),
                            resultSet.getBoolean("status")
                    );
                    listOfActivity.add(activity);
                }
                while(resultSet.next());

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return listOfActivity;
    }

    public static ObservableList<Activity> getAllActivtiesFromDate(LocalDate date)
    {
        ObservableList<Activity> listOfActivity = FXCollections.observableArrayList();

        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        String sql = String.format("SELECT * FROM activities WHERE (userId = '%s' AND activityDate='%s')",currentUid,date.toString());
        try{
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            if (DatabaseHelper.getResultSetSize(resultSet) >=1)
            {
                do {
                    Activity activity = new Activity(resultSet.getInt("activityId"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("venue"),
                            resultSet.getObject("activityDate",LocalDate.class),
                            resultSet.getBoolean("status")
                    );
                    listOfActivity.add(activity);
                }
                while(resultSet.next());

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return listOfActivity;
    }

    //Create Activity in database
    public static void createActivityInDb(Activity activity)
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
                activity.getTaskName(),
                activity.getDescription(),
                activity.getVenue(),
                activity.getStatus(),
                activity.getDate().toString());
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

    //Update Activity in database
    public static void updateActivityInDb(Activity oldActivity,Activity newActivity)
    {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        //Edit activity entry in database
        sql = String.format("UPDATE activities SET userId=%s,name='%s',description='%s',venue='%s',status=%s,activityDate='%s' WHERE (activityId = '%s')",
                currentUid,
                newActivity.getTaskName(),
                newActivity.getDescription(),
                newActivity.getVenue(),
                newActivity.getStatus(),
                newActivity.getDate().toString(),
                oldActivity.getActivityId());
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
