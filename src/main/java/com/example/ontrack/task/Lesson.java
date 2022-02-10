package com.example.ontrack.task;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.task.repetition.RepetitionRule;

import java.sql.*;
import java.time.LocalDate;

public class Lesson extends RepeatableTask {
    private int lessonId;
    private String subject;
    private String venue;
    private LocalDate date;

    //Constructor for when repetition rule id is not known
    public Lesson(String name,String desc,String subject, String venue,LocalDate date,int currentRound,Boolean status)
    {
        this.taskName=name;
        this.description = desc;
        this.subject=subject;
        this.venue=venue;
        this.date=date;
        this.currentRound=currentRound;
        this.status = status;
    }

    //Constructor for when ruleId is known
    public Lesson(String name,String desc,String subject, String venue, int repetitionRuleId,LocalDate date,int currentRound,Boolean status)
    {
        this.taskName=name;
        this.description = desc;
        this.subject=subject;
        this.venue=venue;
        this.ruleId = repetitionRuleId;
        this.date=date;
        this.currentRound=currentRound;
        this.status = status;
    }

    @Override
    public void setRepetitionRule(RepetitionRule repetitionRule) {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Add repetition rule into database
        sql = String.format("UPDATE lessons SET repetitionRuleId = %s WHERE lessonId = %s",
                repetitionRule.getRuleId(),
                this.getLessonId()
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

    public int getLessonId() {
        if(this.lessonId != 0)
        {
            return lessonId;
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
            sql = String.format("SELECT * FROM lessons WHERE (name = '%s' AND userId = '%s' AND lessonDate='%s')",this.taskName,currentUid,this.date.toString());
            try{
                PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
                ResultSet resultSet = statement.executeQuery();

                //If rule exist, return ruleid
                if (DatabaseHelper.getResultSetSize(resultSet)>=1)
                {
                    this.lessonId = resultSet.getInt("lessonId");
                    return lessonId;
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

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getRuleId() {
        return ruleId;
    }

    public void setRuleId(int ruleId) {
        this.ruleId = ruleId;
    }
}
