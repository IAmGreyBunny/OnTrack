package com.example.ontrack.task.repetition;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
}
