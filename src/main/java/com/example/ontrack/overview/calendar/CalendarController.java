package com.example.ontrack.overview.calendar;

import com.example.ontrack.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    @FXML
    GridPane calendarGridPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Load calendar
        LocalDate todayDate = LocalDate.now();
        loadCalendar(todayDate.getMonth().getValue(),todayDate.getYear());


    }


    public void loadCalendar(int month,int year) {
        LocalDate localDate = LocalDate.of(year,month,1); //LocalDate object of 1st day of month
        int firstDayOfMonth = localDate.getDayOfWeek().getValue();   //Get 1st day of month as the starting column

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
}
