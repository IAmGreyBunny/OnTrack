package com.example.ontrack.task.repetition;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class RepetitionRuleHelper {
    public static RepetitionRule getRepetitionRuleFromId(int ruleId)
    {
        RepetitionRule repetitionRule=null;

        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        String sql = String.format("SELECT * FROM repetitionRules WHERE (ruleId = %s)", ruleId);
        try{
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();


            if (DatabaseHelper.getResultSetSize(resultSet)>=1)
            {
                do
                {
                    repetitionRule = new RepetitionRule(ruleId,resultSet.getString(2),resultSet.getString(3));
                }while(resultSet.next());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return repetitionRule;
    }

    //Get all of user repetition rules
    public static ObservableList<RepetitionRule> getUserRepetitionRules()
    {
        ObservableList<RepetitionRule> userRepetitionRule = FXCollections.observableArrayList();

        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        String sql = String.format("SELECT * FROM repetitionRules WHERE (userId = %s)",CurrentUser.getInstance().getUser().getUserId());
        try{
            PreparedStatement statement = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();


            if (DatabaseHelper.getResultSetSize(resultSet)>=1)
            {
                resultSet.beforeFirst();
                while(resultSet.next())
                {
                    int ruleId = resultSet.getInt("ruleId");
                    String ruleName = resultSet.getString("ruleName");
                    String repeatType = resultSet.getString("repeatType");
                    RepetitionRule repetitionRule = new RepetitionRule(ruleId,ruleName,repeatType);
                    repetitionRule.getRounds();
                    userRepetitionRule.add(repetitionRule);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return userRepetitionRule;
    }

    //Create repetition rule in database
    public static void createRepetitionRuleInDb(RepetitionRule repetitionRule)
    {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        //Add repetition rule into database
        sql = String.format("INSERT INTO repetitionRules(ruleName,repeatType,userid) VALUES ('%s','%s',%s)",repetitionRule.getRuleName(),repetitionRule.getRepeatType(), currentUid);
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //Add round info of repetition rule into database
        for(Round round: repetitionRule.getRounds())
        {
            sql = String.format("INSERT INTO Rounds(ruleId,roundNumber,roundInterval) VALUES (%s,%s,%s)",repetitionRule.getRuleId(),round.getRoundNumber(), round.getRoundInterval());
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

    //Update repetition rule
    public static void updateRepetitionRule(RepetitionRule oldRepetitionRule,RepetitionRule newRepetitionRule)
    {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //If new old repetition rule have more cycles, delete the extras
        if(lengthOfCycle(oldRepetitionRule)>lengthOfCycle(newRepetitionRule))
        {
            for(int i=lengthOfCycle(newRepetitionRule);i<lengthOfCycle(oldRepetitionRule);i++)
            {
                sql = String.format("DELETE FROM rounds WHERE (ruleId=%s AND roundNumber=%s)",
                        oldRepetitionRule.getRuleId(), i+1); //+1 because sql is not zero-indexed
                try{
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(sql);
                    System.out.println("Delete Extra");
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
        else if(lengthOfCycle(oldRepetitionRule)<lengthOfCycle(newRepetitionRule))
        {
            for(int i=lengthOfCycle(oldRepetitionRule);i<lengthOfCycle(newRepetitionRule);i++)
            {
                //Add rounds into database
                sql = String.format("INSERT INTO rounds(ruleId,roundNumber,roundInterval) VALUES (%s,%s,%s)",
                        oldRepetitionRule.getRuleId(),i+1, newRepetitionRule.getRounds().get(i).getRoundInterval());//+1 because sql is not zero-indexed
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


        //Add repetition rule into database
        sql = String.format("UPDATE repetitionRules SET ruleName='%s',repeatType='%s' WHERE ruleId=%s",newRepetitionRule.getRuleName(),newRepetitionRule.getRepeatType(), oldRepetitionRule.getRuleId());
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //Add round info of repetition rule into database
        for(Round round: newRepetitionRule.getRounds())
        {
            sql = String.format("UPDATE rounds SET roundInterval=%s WHERE (ruleId=%s AND roundNumber=%s)",round.getRoundInterval(),oldRepetitionRule.getRuleId(),round.getRoundNumber());
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

    //Get number of round in cycle
    public static int lengthOfCycle(RepetitionRule repetitionRule)
    {
        return repetitionRule.getRounds().size();
    }

    public static int lengthOfCycle(ObservableList<Round> rounds)
    {
        return rounds.size();
    }
}
