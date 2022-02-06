package com.example.ontrack.task;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.repetition.RepetitionRule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Revision extends RepeatableTask {
    private int revisionId;
    private String subject;

    public Revision(String name, String subject, String desc, RepetitionRule repetitionRule)
    {
        this.taskName=name;
        this.description = desc;
        this.subject=subject;
        this.repetitionRule = repetitionRule;
        this.currentRound=1;
    }

    //Create lesson in database
    public void createRevisionInDb()
    {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        //Add repetition rule into database
        sql = String.format("INSERT INTO revisions(userId,name,description,status,round) VALUES (%s,'%s','%s',%s,%s)",
                currentUid,
                this.taskName,
                this.description,
                0,
                this.currentRound);
        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
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
                this.getRevisionIdId()
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
        }
    }

    public int getRevisionIdId() {
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
            sql = String.format("SELECT * FROM revisions WHERE (name = '%s' AND userId = '%s')",this.taskName,currentUid);
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
        }
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

    @Override
    public Task getPreviousTask(Task currentTask) {
        return null;
    }

    @Override
    public Task getNextTask(Task currentTask) {
        return null;
    }

    @Override
    public Task createNextTask(Task currentTask) {
        return null;
    }
}
