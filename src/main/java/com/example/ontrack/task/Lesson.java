package com.example.ontrack.task;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.repetition.RepetitionRule;
import com.example.ontrack.repetition.RepetitionRuleHelper;
import com.example.ontrack.repetition.Round;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class Lesson extends RepeatableTask {
    private int lessonId;
    private String subject;
    private String venue;
    private LocalDate date;

    //Constructor for first Lesson in a cycle, used as a starting point
    //Assumes current round = 1
    public Lesson(String name, String subject,String desc, String venue, RepetitionRule repetitionRule,LocalDate date)
    {
        this.taskName=name;
        this.description = desc;
        this.subject=subject;
        this.venue=venue;
        this.repetitionRule = repetitionRule;
        this.date=date;
        this.currentRound=1;
    }

    //Constructor for creating subsequent lesson where round number is known
    //current round number have to be given
    public Lesson(String name, String subject,String desc, String venue, RepetitionRule repetitionRule,LocalDate date,int currentRound)
    {
        this.taskName=name;
        this.description = desc;
        this.subject=subject;
        this.venue=venue;
        this.repetitionRule = repetitionRule;
        this.date=date;
        this.currentRound=currentRound;
    }

    //Constructor with full info
    //Use this to Lesson objects from db
    public Lesson(int lessonId,String name,String desc, String venue, int repetitionRuleId,LocalDate date,int currentRound,Boolean status)
    {
        this.lessonId=lessonId;
        this.taskName=name;
        this.description=desc;
        this.venue=venue;
        this.repetitionRule = RepetitionRuleHelper.getRepetitionRuleFromId(repetitionRuleId);
        this.date=date;
        this.currentRound=currentRound;
        this.status=status;
    }

    //Create lesson in database
    public void createLessonInDb()
    {
        //Gets connection to database
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();
        String sql = "";

        //Gets current user id
        int currentUid = CurrentUser.getInstance().getUser().getUserId();

        //Add repetition rule into database
        sql = String.format("INSERT INTO lessons(userId,name,description,venue,status,round,lessonDate) VALUES (%s,'%s','%s','%s',%s,%s,'%s')",
                currentUid,
                this.taskName,
                this.description,
                this.venue,
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
    }

    //Create subsequent lesson in database
    public void createLessonCycleInDb()
    {
        //Get rounds
        ObservableList<Round> rounds = this.repetitionRule.getRounds();
        int roundInterval=0;
        for(Round round : rounds)
        {
            int roundNumber = round.getRoundNumber()+1;
            roundInterval += round.getRoundInterval();

            Lesson lesson = new Lesson(this.taskName, this.subject, this.description,this.venue, this.repetitionRule, this.date.plusDays(roundInterval), roundNumber);
            lesson.createLessonInDb();
            lesson.setRepetitionRule(this.repetitionRule);
        }
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
