package com.example.ontrack.task.lesson;

import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.database.DatabaseHelper;
import com.example.ontrack.database.DatabaseManager;
import com.example.ontrack.task.repetition.RepetitionRule;
import com.example.ontrack.task.revision.Revision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class LessonCycle {
    ObservableList<Lesson> lessonsInCycle;
    String lessonCycleName;//same as name of every lesson in the cycle

    public LessonCycle(String lessonCycleName)
    {
        this.lessonCycleName = lessonCycleName;
        lessonsInCycle = getUserLessonCycleWithName(lessonCycleName);
    }

    public void deleteLessonCycle() {
        for (Lesson lesson : lessonsInCycle)
        {
            //Gets connection to database
            DatabaseManager databaseManager = new DatabaseManager();
            Connection connection = databaseManager.getConnection();
            String sql = "";

            sql = String.format("DELETE FROM lessons WHERE (lessonId=%s)",
                    lesson.getLessonId()); //+1 because sql is not zero-indexed
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

    public void createAhead()
    {
    }

    //Get all user lesson with name
    public ObservableList<Lesson> getUserLessonCycleWithName(String name)
    {
        ObservableList<Lesson> listOfLessons = FXCollections.observableArrayList();

        //Database Connection
        DatabaseManager databaseManager = new DatabaseManager();
        Connection connection = databaseManager.getConnection();

        String sql = String.format("SELECT * FROM lessons WHERE (userId = '%s' AND name='%s')",
                CurrentUser.getInstance().getUser().getUserId(),
                name);
        try{
            PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet resultSet = statement.executeQuery();
            if (DatabaseHelper.getResultSetSize(resultSet) >=1)
            {
                do{
                    Lesson lesson = new Lesson(
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getString("subject"),
                            resultSet.getString("venue"),
                            resultSet.getInt("repetitionRuleId"),
                            resultSet.getObject("lessonDate", LocalDate.class),
                            resultSet.getInt("round"),
                            resultSet.getBoolean("status"));
                    listOfLessons.add(lesson);
                }while(resultSet.next());

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return listOfLessons;
    }

    public Lesson getFirstLessonInCycle()
    {
        return lessonsInCycle.get(0);
    }

    //Get a percentage of user completion rate for a cycle
    public double getCompletionRateOfLessonCycle()
    {
        double completed=0;
        for(Lesson lesson:lessonsInCycle)
        {
            if(lesson.getStatus())
            {
                completed++;
            }
        }
        return (completed/lessonsInCycle.size())*100;
    }
}
