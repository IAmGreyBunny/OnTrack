package com.example.ontrack.overview.calendar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

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
    }
}
