package com.example.ontrack.overview.calendar;

import com.example.ontrack.Main;
import com.example.ontrack.authentication.CurrentUser;
import com.example.ontrack.task.*;
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
                        System.out.println(activity.getVenue());
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
                        System.out.println(exam.getVenue());
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
                        System.out.println(lesson.getVenue());
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
                    }
                });
                taskListVbox.getChildren().add(taskItemCell);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
}
