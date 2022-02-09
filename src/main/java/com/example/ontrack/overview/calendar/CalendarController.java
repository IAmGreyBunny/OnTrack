package com.example.ontrack.overview.calendar;

import com.example.ontrack.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    @FXML
    GridPane calendarGridPane;
    @FXML
    Label monthLabel;
    @FXML
    Button nextMonthButton;
    @FXML
    Button previousMonthButton;

    LocalDate currentDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Load calendar
        currentDate = LocalDate.now();
        long startTime = System.currentTimeMillis();
        loadCalendar(currentDate.getMonth().getValue(),currentDate.getYear());
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");


    }

    @FXML
    public void onNextMonthButtonClick()
    {
        currentDate=currentDate.plusMonths(1);
        clearCalendarCells();
        loadCalendar(currentDate.getMonth().getValue(),currentDate.getYear());
    }

    @FXML
    public void onPreviousMonthButtonClick()
    {
        currentDate=currentDate.minusMonths(1);
        clearCalendarCells();
        loadCalendar(currentDate.getMonth().getValue(),currentDate.getYear());
    }

    public void loadCalendar(int month,int year) {
        LocalDate localDate = LocalDate.of(year,month,1); //LocalDate object of 1st day of month
        int firstDayOfMonth = localDate.getDayOfWeek().getValue();   //Get 1st day of month as the starting column
        monthLabel.setText(localDate.getMonth().toString());

        int row = 1; //Initialise row to 1, 0 is header
        for(int column = firstDayOfMonth; column<=7;column++)
        {
            //Create calendar cell for date
            FXMLLoader calendarCellLoader = new FXMLLoader(Main.class.getResource("overview/calendar/CalendarCell.fxml"));
            CalendarCellController calendarCellController;
            Parent calendarCell = null;

            //Load calendar cell and load its content
            try {
                calendarCell = calendarCellLoader.load();
                calendarCellController = calendarCellLoader.getController();
                calendarCellController.loadCalendarCellForDate(localDate); //Load calendar cell content based on date given
            } catch (IOException e) {
                e.printStackTrace();
            }

            //If date belongs to next month, break the whole loop and ignore creation of calendar cell
            if(localDate.getMonth().getValue()>month)
            {
                break;
            }
            calendarGridPane.add(calendarCell,column,row);

            if(column==6)
            {
                row++;
                column=-1;
            }

            //Increment day
            localDate = localDate.plusDays(1);
        }

    }

    public void clearCalendarCells()
    {
        ObservableList<Node> nodesToRemove = FXCollections.observableArrayList();
        for (Node node : calendarGridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) != null && GridPane.getRowIndex(node) != null) {
                if (GridPane.getColumnIndex(node) >= 0 && GridPane.getRowIndex(node) >= 1) {
                   nodesToRemove.add(node);
                }
            }
        }
        for(Node node:nodesToRemove)
        {
            calendarGridPane.getChildren().remove(node);
        }
    }
}
