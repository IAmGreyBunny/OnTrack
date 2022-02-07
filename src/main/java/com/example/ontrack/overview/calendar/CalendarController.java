package com.example.ontrack.overview.calendar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

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


    public void loadCalendar(int month,int year)
    {
        LocalDate localDate = LocalDate.of(year,month,1);
        System.out.println(localDate);
        int firstDayOfMonth = localDate.getDayOfWeek().getValue();
        System.out.println(firstDayOfMonth);


        int row = 1;
        for(int column = firstDayOfMonth; column<=7;column++)
        {
            Label label = new Label(localDate.toString());
            if(localDate.getMonth().getValue()>month)
            {
                break;
            }
            calendarGridPane.add(label,column,row);
            if(column==6)
            {
                row++;
                column=-1;
            }
            localDate = localDate.plusDays(1);
        }

    }
}
