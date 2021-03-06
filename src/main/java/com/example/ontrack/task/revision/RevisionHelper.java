package com.example.ontrack.task.revision;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.task.repetition.RepetitionRule;
import com.example.ontrack.task.repetition.Round;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class RevisionHelper {
    public static ObservableList<Revision> getAllRevisionsFromDate(int userId,LocalDate date)
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
                do{
                    Revision revision = new Revision(resultSet.getInt("revisionId"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("subject"),
                            resultSet.getInt("repetitionRuleId"),
                            resultSet.getObject("revisionDate",LocalDate.class),
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

    public static ObservableList<Revision> getAllRevisionsFromDate(LocalDate date)
    {
        ObservableList<Revision> listOfRevisions = FXCollections.observableArrayList();

        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        String sql = String.format("SELECT * FROM revisions WHERE (userId = '%s' AND revisionDate='%s')",currentUid,date.toString());
        try{
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            if (DatabaseHelper.getResultSetSize(resultSet) >=1)
            {
                Revision revision = new Revision(resultSet.getInt("revisionId"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getString("subject"),
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

    //Create revision in database
    public static void createRevisionInDb(Revision revision,RepetitionRule repetitionRule){
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        //Add repetition rule into database
        sql = String.format("INSERT INTO revisions(userId,name,description,subject,repetitionRuleId,revisionDate,round,status) " +
                        "VALUES (%s,'%s','%s','%s',%s,'%s',%s,%s)",
                currentUid,
                revision.getTaskName(),
                revision.getDescription(),
                revision.getSubject(),
                repetitionRule.getRuleId(),
                revision.getDate().toString(),
                revision.getCurrentRound(),
                revision.getStatus());
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //Create subsequent revision in database
    public static void createRevisionCycleInDb(Revision firstRevisionInCycle, RepetitionRule repetitionRule) {
        //Get rounds
        ObservableList<Round> rounds = firstRevisionInCycle.getRepetitionRule().getRounds();
        int roundInterval=0;
        for(Round round : rounds)
        {
            int roundNumber = round.getRoundNumber()+1;
            roundInterval += round.getRoundInterval();

            Revision revision = new Revision(firstRevisionInCycle.getTaskName(),
                    firstRevisionInCycle.getDescription(),
                    firstRevisionInCycle.getSubject(),
                    repetitionRule.getRuleId(),
                    firstRevisionInCycle.getDate().plusDays(roundInterval),
                    roundNumber,
                    false);
            RevisionHelper.createRevisionInDb(revision,repetitionRule);
            revision.setRepetitionRule(repetitionRule);
        }
    }

    public static void updateRevisionInDb(Revision oldRevision, Revision newRevision) {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        sql = String.format("UPDATE revisions SET name='%s',description='%s',subject='%s',status=%s,round=%s,revisionDate='%s' WHERE revisionId=%s",
                newRevision.getTaskName(),
                newRevision.getDescription(),
                newRevision.getSubject(),
                newRevision.getStatus(),
                newRevision.getCurrentRound(),
                newRevision.getDate().toString(),
                oldRevision.getRevisionId());
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
