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

public class RepetitionRule {
    private int userId;
    private  int ruleId;
    private String ruleName;
    private String repeatType;
    private ObservableList<Round> rounds;

    public RepetitionRule(String ruleName,String repeatType,ObservableList<Round>rounds)
    {
        this.ruleName = ruleName;
        this.repeatType = repeatType;
        this.rounds = rounds;
    }

    //Overloaded constructor for rounds not being given
    public RepetitionRule(int ruleId,String ruleName,String repeatType)
    {
        this.ruleId=ruleId;
        this.ruleName = ruleName;
        this.repeatType = repeatType;
        this.rounds = getRounds();
    }


    //Create repetition rule in database
    public boolean createRepetitionRuleInDb(RepetitionRule repetitionRule)
    {
        boolean success = true;
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        //Add repetition rule into database
        sql = String.format("INSERT INTO repetitionRules(ruleName,repeatType,userid) VALUES ('%s','%s',%s)",this.ruleName,this.repeatType, currentUid);
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            success=false;
        }

        //Add round info of repetition rule into database
        for(Round round: repetitionRule.rounds)
        {
            sql = String.format("INSERT INTO Rounds(ruleId,roundNumber,roundInterval) VALUES (%s,%s,%s)",this.getRuleId(),round.getRoundNumber(), round.getRoundInterval());
            try{
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            }
            catch(Exception e)
            {
                e.printStackTrace();
                success=false;
            }
        }

        return success;
    }

    public int getRuleId()
    {
        // If ruleid exist,ruleid can never be null,only 0,
        // return ruleid, else find ruleid from database and return it
        // ruleid can never be null,only 0,
        if(this.ruleId!=0)
        {
            return this.ruleId;
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
            sql = String.format("SELECT * FROM repetitionRules WHERE (ruleName = '%s' AND userId = '%s')",this.getRuleName(),currentUid);
            try{
                PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery();

                //If rule exist, return ruleid
                if (DatabaseHelper.getResultSetSize(resultSet)>=1)
                {
                    this.ruleId = resultSet.getInt("ruleId");
                    return ruleId;
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
        }

    }

    public void setRuleId(int ruleId)
    {
        this.ruleId = ruleId;
    }

    public String getRuleName()
    {
        return ruleName;
    }

    public void setRuleName(String ruleName)
    {
        this.ruleName = ruleName;
    }

    public String getRepeatType()
    {
        return repeatType;
    }

    public void setRepeatType(String repeatType)
    {
        this.repeatType = repeatType;
    }

    public ObservableList<Round> getRounds()
    {
        if(this.rounds != null)
        {
            return this.rounds;
        }
        else
        {
            ObservableList<Round> rounds = FXCollections.observableArrayList();

            //Database Connection
            DatabaseManager databaseManager = new DatabaseManager();
            Connection connection = databaseManager.getConnection();

            String sql = String.format("SELECT * FROM rounds WHERE (ruleId = %s)",getRuleId());
            try{
                PreparedStatement statement = connection.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery();

                if (DatabaseHelper.getResultSetSize(resultSet)>=1)
                {
                    do{
                        int roundNumber = resultSet.getInt("roundNumber");
                        int roundInterval = resultSet.getInt("roundInterval");
                        Round round = new Round(roundNumber,roundInterval);
                        rounds.add(round);
                    }while(resultSet.next());
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return rounds;
        }
    }

    public void setRounds(ObservableList<Round> rounds)
    {
        this.rounds = rounds;
    }
}
