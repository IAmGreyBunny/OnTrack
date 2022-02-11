package com.example.ontrack.task.revision;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class RevisionCycle {
    ObservableList<Revision> revisionsInCycle;
    String revisionCycleName;//same as name of every revision in the cycle

    public RevisionCycle(String revisionCycleName)
    {
        this.revisionCycleName = revisionCycleName;
        revisionsInCycle = getUserRevisionCycleWithName(revisionCycleName);
    }

    public void deleteRevisionCycle() {
        for (Revision revision : revisionsInCycle)
        {
            //Gets connection to database
            DatabaseManager databaseManager = new DatabaseManager();
            Connection connection = databaseManager.getConnection();
            String sql = "";

            sql = String.format("DELETE FROM revisions WHERE (revisionId=%s)",
                    revision.getRevisionId());
            try{
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void createAhead()
    {
    }

    //Get all user revision with name
    public static ObservableList<Revision> getUserRevisionCycleWithName(String name)
    {
        ObservableList<Revision> listOfRevisions = FXCollections.observableArrayList();

        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        String sql = String.format("SELECT * FROM revisions WHERE (userId = '%s' AND name='%s')",
                CurrentUser.getInstance().getUser().getUserId(),
                name);
        try{
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            if (DatabaseHelper.getResultSetSize(resultSet) >=1)
            {
                do{
                    Revision revision = new Revision(
                            resultSet.getInt("revisionId"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("subject"),
                            resultSet.getInt("repetitionRuleId"),
                            resultSet.getObject("revisionDate", LocalDate.class),
                            resultSet.getInt("round"),
                            resultSet.getBoolean("status"));
                    listOfRevisions.add(revision);
                }while(resultSet.next());

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return listOfRevisions;
    }
}
