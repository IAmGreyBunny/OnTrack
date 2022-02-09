package com.example.ontrack.task;

import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class RevisionHelper {
    public static ObservableList<Revision> getAllLessonsFromDate(int userId,LocalDate date)
    {
        ObservableList<Revision> listOfRevisions = FXCollections.observableArrayList();

        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        String sql = String.format("SELECT * FROM revisions WHERE (userId = '%s' AND revisionDate='%s')",userId,date.toString());
        try{
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            if (DatabaseHelper.getResultSetSize(resultSet) >=1)
            {
                Revision revision = new Revision(resultSet.getInt("revisionId"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("repetitionRuleId"),
                        resultSet.getObject("revisionDate",LocalDate.class),
                        resultSet.getInt("round"),
                        resultSet.getBoolean("status"));
                listOfRevisions.add(revision);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return listOfRevisions;
    }
}
