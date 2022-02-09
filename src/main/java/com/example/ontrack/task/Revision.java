package com.example.ontrack.task;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.repetition.RepetitionRule;
import com.example.ontrack.repetition.RepetitionRuleHelper;
import com.example.ontrack.repetition.Round;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class Revision extends RepeatableTask {
    private int revisionId;
    private String subject;
    private LocalDate date;

    //Constructor for first Revision in a cycle, used as a starting point
    //Assumes current round = 1
    public Revision(String name, String subject, String desc, RepetitionRule repetitionRule, LocalDate date)
    {
        this.taskName=name;
        this.description = desc;
        this.subject=subject;
        this.repetitionRule = repetitionRule;
        this.currentRound=1;
        this.date = date;
    }

    //Constructor for creating subsequent lesson where round number is known
    //current round number have to be given
    public Revision(String name, String subject, String desc, RepetitionRule repetitionRule, LocalDate date, int currentRound)
    {
        this.taskName=name;
        this.description = desc;
        this.subject=subject;
        this.repetitionRule = repetitionRule;
        this.currentRound=currentRound;
        this.date = date;
    }

    //Constructor to create revision with full info
    //Used for getting revision object from database
    public Revision(int revisionId,String name,String desc, int repetitionRuleId,LocalDate date,int currentRound,Boolean status)
    {
        this.revisionId=revisionId;
        this.taskName=name;
        this.description=desc;
        this.repetitionRule = RepetitionRuleHelper.getRepetitionRuleFromId(repetitionRuleId);
        this.date=date;
        this.currentRound=currentRound;
        this.status=status;
    }

    //Create lesson in database
    public void createRevisionInDb(){
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        //Add repetition rule into database
        sql = String.format("INSERT INTO revisions(userId,name,description,status,round,revisionDate) VALUES (%s,'%s','%s',%s,%s,'%s')",
                currentUid,
                this.taskName,
                this.description,
                0,
                this.currentRound,
                this.date.toString());
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

    //Create subsequent lesson in database
    public void createRevisionCycleInDb() {
        //Get rounds
        ObservableList<Round> rounds = this.repetitionRule.getRounds();
        int roundInterval=0;
        for(Round round : rounds)
        {
            int roundNumber = round.getRoundNumber()+1;
            roundInterval += round.getRoundInterval();

            Revision revision = new Revision(this.taskName, this.subject, this.description, this.repetitionRule, this.date.plusDays(roundInterval), roundNumber);
            revision.createRevisionInDb();
            revision.setRepetitionRule(this.repetitionRule);
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
