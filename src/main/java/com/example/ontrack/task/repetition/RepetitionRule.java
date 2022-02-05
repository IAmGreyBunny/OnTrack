package com.example.ontrack.task.repetition;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.authentication.User;
import com.example.ontrack.database.DatabaseManager;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public class RepetitionRule {
    private int userId;
    private  int ruleId;
    private String ruleName;
    private String repeatType;
    private ObservableList<Round> rounds;

    public RepetitionRule(String ruleName,String repeatType,ObservableList<Round> rounds)
    {
        this.ruleName = ruleName;
        this.repeatType = repeatType;
        this.rounds = rounds;
    }


    //Create repetition rule in database
    public static boolean createRepetitionRule(RepetitionRule repetitionRule)
    {
        boolean success = true;
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUid();

        //Add repetition rule into database
        sql = String.format("INSERT INTO repetitionRule(ruleName,repeatType,userid) VALUES ('%s','%s',%s)",repetitionRule.ruleName,repetitionRule.repeatType, currentUid);
        try{
            System.out.println("Executing Query:\n" + sql);
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
            sql = String.format("INSERT INTO Rounds(ruleId,roundNumber,roundInterval) VALUES (%s,%s,%s)",repetitionRule.getRuleId(),round.getRoundNumber(), round.getRoundInterval());
            try{
                System.out.println("Executing Query:\n" + sql);
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

        //If ruleid exist, return ruleid, else find ruleid from database and return it
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
            int currentUid = CurrentUser.getInstance().getUser().getUid();

            //Look for rule name with the same user id as current user
            sql = String.format("SELECT * FROM repetitionRule WHERE (ruleName = '%s' AND userId = '%s')",this.getRuleName(),currentUid);
            try{
                System.out.println();
                System.out.println("Executing Query:\n" + sql);
                PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery();

                //Get size of query
                int size = 0;
                if(resultSet!=null)
                {
                    resultSet.last();
                    size=resultSet.getRow();
                    System.out.println("Result contains: "+size);
                }
                else
                {
                    System.out.println("Query Fails");
                }

                //If rule exist, return ruleid
                if (size>=1)
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
            }
        }
        return 0;
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
        return rounds;
    }

    public void setRounds(ObservableList<Round> rounds)
    {
        this.rounds = rounds;
    }
}
