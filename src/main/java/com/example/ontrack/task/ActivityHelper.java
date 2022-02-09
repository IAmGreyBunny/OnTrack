package com.example.ontrack.task;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.authentication.User;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
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
                            resultSet.getBoolean("status"),
                            resultSet.getObject("activityDate",LocalDate.class));
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
}
