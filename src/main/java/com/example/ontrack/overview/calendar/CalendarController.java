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
    Label yearLabel;
    @FXML
    Button nextMonthButton;
    @FXML
    Button previousMonthButton;

    LocalDate currentDisplayDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Load calendar
        currentDisplayDate = LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonth(),1);
        loadCalendar(currentDisplayDate.getMonth().getValue(), currentDisplayDate.getYear());
    }

    @FXML
    public void onNextMonthButtonClick() {
        currentDisplayDate = currentDisplayDate.plusMonths(1);
        clearCalendarCells();
        loadCalendar(currentDisplayDate.getMonth().getValue(), currentDisplayDate.getYear());
    }

    @FXML
    public void onPreviousMonthButtonClick() {
        currentDisplayDate = currentDisplayDate.minusMonths(1);
        clearCalendarCells();
        loadCalendar(currentDisplayDate.getMonth().getValue(), currentDisplayDate.getYear());


    }
    public void loadCalendar(int month, int year) {
        LocalDate localDate = LocalDate.of(year, month, 1); //LocalDate object of 1st day of month
        currentDisplayDate=localDate;
        int firstDayOfMonth = localDate.getDayOfWeek().getValue();   //Get 1st day of month as the starting column
        monthLabel.setText(localDate.getMonth().toString());
        yearLabel.setText(String.valueOf(localDate.getYear()));

        int row = 1; //Initialise row to 1, 0 is header
        for (int column = firstDayOfMonth; column <= 7; column++) {
            //If first day of month is sunday, column is 7
            //which is invalid column as max is 6
            if(column==7)
            {
                column=0;
            }

            //If date belongs to next month, break the whole loop and ignore creation of calendar cell
            if (localDate.getMonth().getValue() > month) {
                break;
            }
            //If date belongs to next year, break the whole loop and ignore creation of calendar cell
            if (localDate.getYear() > year) {
                break;
            }
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
            calendarGridPane.add(calendarCell, column, row);

            if (column == 6) {
                row++;
                column = -1;
            }

            //Increment day
            localDate = localDate.plusDays(1);
        }

    }

    //Clear content of cells,used to prepare calendar for new data
    public void clearCalendarCells() {
        ObservableList<Node> nodesToRemove = FXCollections.observableArrayList();
        for (Node node : calendarGridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) != null && GridPane.getRowIndex(node) != null) {
                if (GridPane.getColumnIndex(node) >= 0 && GridPane.getRowIndex(node) >= 1) {
                    nodesToRemove.add(node);
                }
            }
        }
        for (Node node : nodesToRemove) {
            calendarGridPane.getChildren().remove(node);
        }
    }
}
