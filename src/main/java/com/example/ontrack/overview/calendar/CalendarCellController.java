package com.example.ontrack.overview.calendar;

import com.example.ontrack.Main;
import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.task.*;
import com.example.ontrack.task.info.ActivityInfoController;
import com.example.ontrack.task.info.ExamInfoController;
import com.example.ontrack.task.info.LessonInfoController;
import com.example.ontrack.task.info.RevisionInfoController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CalendarCellController {

    @FXML
    Label cellDateLabel;
    @FXML
    VBox taskListVbox;

    //Load calendar cell content based on date given
    public void loadCalendarCellForDate(LocalDate localDate)
    {
        this.cellDateLabel.setText(String.valueOf(localDate.getDayOfMonth()));
        cellDateLabel.setText(String.valueOf(localDate.getDayOfMonth()));

        //Get All Task
        int userId = CurrentUser.getInstance().getUser().getUserId();
        ObservableList<Activity> listOfActivities = ActivityHelper.getAllActivtiesFromDate(userId,localDate);
        ObservableList<Exam> lisOfExams = ExamHelper.getAllExamsFromDate(userId,localDate);
        ObservableList<Lesson> listOfLessons = LessonHelper.getAllLessonsFromDate(userId,localDate);
        ObservableList<Revision> listOfRevisions = RevisionHelper.getAllLessonsFromDate(userId,localDate);

        //Load calendar cell content (Item cells)
        for(Activity activity:listOfActivities)
        {
            //Create calendar cell for date
            FXMLLoader taskItemCellLoader = new FXMLLoader(Main.class.getResource("overview/calendar/TaskItemCell.fxml"));
            TaskItemCellController taskItemCellController;
            HBox taskItemCell = null;
            try {
                taskItemCell = taskItemCellLoader.load();
                taskItemCellController = taskItemCellLoader.getController();
                taskItemCellController.setTaskToCell(activity); //Load calendar cell content based on date given
                taskItemCell.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null , null)));
                taskItemCell.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                taskItemCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        FXMLLoader activityInfoLoader = new FXMLLoader(Main.class.getResource("task/info/ActivityInfo.fxml"));
                        Parent activityInfo;
                        ActivityInfoController activityInfoController;
                        try
                        {
                            BorderPane borderPane = (BorderPane) ((Parent)mouseEvent.getSource()).getScene().getRoot();
                            activityInfo = activityInfoLoader.load();
                            activityInfoController = activityInfoLoader.getController();
                            activityInfoController.setActivity(activity);
                            borderPane.setLeft(activityInfo);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                taskListVbox.getChildren().add(taskItemCell);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(Exam exam:lisOfExams)
        {
            //Create calendar cell for date
            FXMLLoader taskItemCellLoader = new FXMLLoader(Main.class.getResource("overview/calendar/TaskItemCell.fxml"));
            TaskItemCellController taskItemCellController;
            HBox taskItemCell = null;
            try {
                taskItemCell = taskItemCellLoader.load();
                taskItemCellController = taskItemCellLoader.getController();
                taskItemCellController.setTaskToCell(exam); //Load calendar cell content based on date given
                taskItemCell.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null , null)));
                taskItemCell.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                taskItemCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        FXMLLoader examInfoLoader = new FXMLLoader(Main.class.getResource("task/info/ExamInfo.fxml"));
                        Parent examInfo;
                        ExamInfoController examInfoController;
                        try
                        {
                            BorderPane borderPane = (BorderPane) ((Parent)mouseEvent.getSource()).getScene().getRoot();
                            examInfo = examInfoLoader.load();
                            examInfoController = examInfoLoader.getController();
                            examInfoController.setExam(exam);
                            borderPane.setLeft(examInfo);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                taskListVbox.getChildren().add(taskItemCell);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(Lesson lesson:listOfLessons)
        {
            //Create calendar cell for date
            FXMLLoader taskItemCellLoader = new FXMLLoader(Main.class.getResource("overview/calendar/TaskItemCell.fxml"));
            TaskItemCellController taskItemCellController;
            HBox taskItemCell = null;
            try {
                taskItemCell = taskItemCellLoader.load();
                taskItemCellController = taskItemCellLoader.getController();
                taskItemCellController.setTaskToCell(lesson); //Load calendar cell content based on date given
                taskItemCell.setBorder(new Border(new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, null , null)));
                taskItemCell.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                taskItemCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        FXMLLoader lessonInfoLoader = new FXMLLoader(Main.class.getResource("task/info/LessonInfo.fxml"));
                        Parent lessonInfo;
                        LessonInfoController lessonInfoController;
                        try
                        {
                            BorderPane borderPane = (BorderPane) ((Parent)mouseEvent.getSource()).getScene().getRoot();
                            lessonInfo = lessonInfoLoader.load();
                            lessonInfoController = lessonInfoLoader.getController();
                            lessonInfoController.setLesson(lesson);
                            borderPane.setLeft(lessonInfo);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                taskListVbox.getChildren().add(taskItemCell);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(Revision revision:listOfRevisions)
        {
            //Create calendar cell for date
            FXMLLoader taskItemCellLoader = new FXMLLoader(Main.class.getResource("overview/calendar/TaskItemCell.fxml"));
            TaskItemCellController taskItemCellController;
            HBox taskItemCell = null;
            try {
                taskItemCell = taskItemCellLoader.load();
                taskItemCellController = taskItemCellLoader.getController();
                taskItemCellController.setTaskToCell(revision); //Load calendar cell content based on date given
                taskItemCell.setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, null , null)));
                taskItemCell.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                taskItemCell.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        FXMLLoader revisionInfoLoader = new FXMLLoader(Main.class.getResource("task/info/RevisionInfo.fxml"));
                        Parent revisionInfo;
                        RevisionInfoController revisionInfoController;
                        try
                        {
                            BorderPane borderPane = (BorderPane) ((Parent)mouseEvent.getSource()).getScene().getRoot();
                            revisionInfo = revisionInfoLoader.load();
                            revisionInfoController = revisionInfoLoader.getController();
                            revisionInfoController.setRevision(revision);
                            borderPane.setLeft(revisionInfo);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
                taskListVbox.getChildren().add(taskItemCell);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
}
