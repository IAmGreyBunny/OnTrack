package com.example.ontrack.task.revision;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.task.lesson.LessonCycle;
import com.example.ontrack.task.repetition.RepetitionRule;
import com.example.ontrack.task.repetition.Round;
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

    public RevisionCycle(RevisionCycle another)
    {
        this.revisionsInCycle = another.revisionsInCycle;
        this.revisionCycleName = another.revisionCycleName;
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
    public void truncateRevisionCycle(int roundToStartFrom) {
        for (Revision revision : revisionsInCycle)
        {
            if(revision.getCurrentRound()>roundToStartFrom)
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
    }

    public Revision getFirstRevisionInCycle()
    {
        return revisionsInCycle.get(0);
    }

    public Revision getLastRevisionInCycle() {
        return revisionsInCycle.get(revisionsInCycle.size()-1);
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
        return (completed/revisionsInCycle.size())*100;
    }

    //For restarting cycle
    public void restartCycle(RepetitionRule repetitionRule)
    {
        Revision lastCompletedRevision = new Revision(getLastCompletedRevisionInCycle());
        Revision newLastCompletedRevision = new Revision(lastCompletedRevision);
        newLastCompletedRevision.setCurrentRound(1);//Restart
        newLastCompletedRevision.setStatus(false);
        RevisionHelper.updateRevisionInDb(lastCompletedRevision,newLastCompletedRevision);
        RevisionHelper.createRevisionInDb(newLastCompletedRevision,repetitionRule);
        lastCompletedRevision.setRepetitionRule(repetitionRule);
        deleteRevisionCycle();//This deletes current cycle from db to make new ones
        RevisionHelper.createRevisionCycleInDb(lastCompletedRevision,repetitionRule);
        getUserRevisionCycleWithName(lastCompletedRevision.getTaskName());
    }

    //For expansion of cycle
    public void repeatLast(Revision lastRevisionInCycle, RepetitionRule repetitionRule, int numberOfTimes)
    {
        //Get round
        ObservableList<Round> rounds = repetitionRule.getRounds();
        Round lastRound = rounds.get(rounds.size()-1);
        int roundNumber = lastRevisionInCycle.getCurrentRound()+1;
        int roundInterval=0;
        for(int i = roundNumber;i<roundNumber+numberOfTimes;i++)
        {
            roundInterval += lastRound.getRoundInterval();
            Revision revision = new Revision(lastRevisionInCycle.getTaskName(),
                    lastRevisionInCycle.getDescription(),
                    lastRevisionInCycle.getSubject(),
                    lastRevisionInCycle.getRuleId(),
                    lastRevisionInCycle.getDate().plusDays(roundInterval),
                    i,
                    false);
            RevisionHelper.createRevisionInDb(revision,repetitionRule);
            revision.setRepetitionRule(repetitionRule);
        }
    }

    //For expansion of cycle
    public void extendCycle(Revision revisionToStartFrom, RepetitionRule repetitionRule)
    {
        //Get round
        ObservableList<Round> rounds = repetitionRule.getRounds();
        int lastCompletedRound = revisionToStartFrom.getCurrentRound();
        int roundInterval=0;
        for(int roundNumber = lastCompletedRound;roundNumber<rounds.size()+1;roundNumber++)
        {

            roundInterval += rounds.get(roundNumber-1).getRoundInterval();
            Revision revision = new Revision(revisionToStartFrom.getTaskName(),
                    revisionToStartFrom.getDescription(),
                    revisionToStartFrom.getSubject(),
                    revisionToStartFrom.getRuleId(),
                    revisionToStartFrom.getDate().plusDays(roundInterval),
                    roundNumber+1,
                    false);
            RevisionHelper.createRevisionInDb(revision,repetitionRule);
            revision.setRepetitionRule(repetitionRule);
        }
    }

    //For updating revision cycle
    public static void updateRevisionsInCycle(Revision oldRevision, Revision newRevision, RepetitionRule oldRepetitionRule, RepetitionRule newRepetitionRule)
    {
        //Update everything else with oldRepetitionRule first
        RevisionCycle revisionCycle = new RevisionCycle(oldRevision.getTaskName());
        RevisionCycle newRevisionCycle = new RevisionCycle(newRevision.getTaskName());

        for(Revision revision:revisionCycle.revisionsInCycle)
        {
            //Use new revision as a template and make modification
            newRevision.setCurrentRound(revision.getCurrentRound());
            newRevision.setDate(revision.getDate());
            newRevision.setStatus(revision.getStatus());
            newRevision.setRuleId(newRepetitionRule.getRuleId());
            newRevisionCycle.revisionsInCycle.add(newRevision);

            //Update
            RevisionHelper.updateRevisionInDb(revision,newRevision);
        }

//        //Contain changed name
//
//        //Update repetition rule
//        if(newRevisionCycle.getLastRevisionInCycle().getCurrentRound()<=newRepetitionRule.getRounds().size())
//        {
//            Revision lastCompletedRevision = new Revision(newRevisionCycle.getLastCompletedRevisionInCycle());
//            newRevisionCycle.truncateRevisionCycle(lastCompletedRevision.getCurrentRound());
//            Revision revisionTemp = new Revision(lastCompletedRevision);
//
//            RevisionHelper.createRevisionInDb(revisionTemp,newRepetitionRule); //create last completed
//            for(int i = lastCompletedRevision.getCurrentRound()-1;i>0;i--)     //Use last completed as a starting point to create all the others
//            {
//                revisionTemp.setCurrentRound(i);
//                int roundInterval = oldRepetitionRule.getRounds().get(i-1).getRoundInterval();
//                revisionTemp.setDate(revisionTemp.getDate().minusDays(roundInterval));
//                RevisionHelper.createRevisionInDb(revisionTemp,newRepetitionRule);
//            }
//            newRevisionCycle.extendCycle(lastCompletedRevision,newRepetitionRule);
//        }
    }
}
