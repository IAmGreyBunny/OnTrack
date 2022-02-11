package com.example.ontrack.task.revision;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.task.RepeatableTask;
import com.example.ontrack.task.repetition.RepetitionRule;
import com.example.ontrack.task.repetition.RepetitionRuleHelper;

import java.sql.*;
import java.time.LocalDate;

public class Revision extends RepeatableTask {
    private int revisionId;
    private String subject;
    private LocalDate date;



    //Constructor for when repetition rule id is not known
    public Revision(String name, String subject, String desc, LocalDate date, int currentRound,Boolean status)
    {
        this.taskName=name;
        this.description = desc;
        this.subject=subject;
        this.currentRound=currentRound;
        this.date = date;
        this.status=status;
    }

    //Constructor to create revision with full info
    //Used for getting revision object from database
    public Revision(int revisionId,String name,String desc,String subject, int repetitionRuleId,LocalDate date,int currentRound,Boolean status)
    {
        this.revisionId=revisionId;
        this.taskName=name;
        this.description=desc;
        this.repetitionRule = RepetitionRuleHelper.getRepetitionRuleFromId(repetitionRuleId);
        this.subject=subject;
        this.date=date;
        this.currentRound=currentRound;
        this.status=status;
    }

    @Override
    public void setRepetitionRule(RepetitionRule repetitionRule) {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Add repetition rule into database
        sql = String.format("UPDATE revisions SET repetitionRuleId = %s WHERE revisionId = %s",
                repetitionRule.getRuleId(),
                this.getRevisionId()
        );
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            //If repetition rule is successfully linked in db, set current lesson object repetition rule to the same one
            this.repetitionRule = repetitionRule;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int getRevisionId() {
        if(this.revisionId != 0)
        {
            return revisionId;
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
            sql = String.format("SELECT * FROM revisions WHERE (name = '%s' AND userId = '%s' AND  revisionDate = '%s')",this.taskName,currentUid,this.date);
            try{
                PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery();

                //If rule exist, return ruleid
                if (DatabaseHelper.getResultSetSize(resultSet)>=1)
                {
                    this.revisionId = resultSet.getInt("revisionId");
                    return revisionId;
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
            finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setRevisionId(int lessonId) {
        this.revisionId = lessonId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
