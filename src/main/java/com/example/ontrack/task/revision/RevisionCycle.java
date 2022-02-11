package com.example.ontrack.task.revision;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.task.repetition.RepetitionRule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
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

    public Revision getFirstRevisionInCycle()
    {
        return revisionsInCycle.get(0);
    }

    //Get Last completed revision in cycle
    public Revision getLastCompletedRevisionInCycle()
    {
        Revision lastCompletedrevision =  getFirstRevisionInCycle();
        for(Revision revision:revisionsInCycle)
        {
            if(revision.getStatus())
            {
                lastCompletedrevision=revision;
            }
        }
        return lastCompletedrevision;
    }

    //Get all user revision with name
    public ObservableList<Revision> getUserRevisionCycleWithName(String name)
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

    //Get a percentage of user completion rate for a cycle
    public double getCompletionRateOfRevisionCycle()
    {
        double completed=0;
        for(Revision revision:revisionsInCycle)
        {
            if(revision.getStatus())
            {
                completed++;
            }
        }
        System.out.println(completed +"/"+ revisionsInCycle.size());
        return (completed/revisionsInCycle.size())*100;
    }

    //For start over repeat type
    public void restartCycle(RepetitionRule repetitionRule)
    {
        Revision lastCompletedRevision = new Revision(getLastCompletedRevisionInCycle());
        Revision newLastCompletedrevision = new Revision(lastCompletedRevision);
        newLastCompletedrevision.setCurrentRound(1);//Restart
        newLastCompletedrevision.setStatus(false);
        RevisionHelper.updateRevisionInDb(lastCompletedRevision,newLastCompletedrevision);
        RevisionHelper.createRevisionInDb(newLastCompletedrevision,repetitionRule);
        lastCompletedRevision.setRepetitionRule(repetitionRule);
        deleteRevisionCycle();//This deletes current cycle from db to make new ones
        RevisionHelper.createRevisionCycleInDb(lastCompletedRevision,repetitionRule);
        getUserRevisionCycleWithName(lastCompletedRevision.getTaskName());
    }
}
