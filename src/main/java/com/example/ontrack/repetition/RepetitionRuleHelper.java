package com.example.ontrack.repetition;

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
}
