package com.example.ontrack.overview.calendar;

public class CalendarControllerHolder {
    private CalendarController calendarController;

    private final static CalendarControllerHolder INSTANCE = new CalendarControllerHolder();

    private CalendarControllerHolder() {
    }

    public static CalendarControllerHolder getInstance()
    {
        return INSTANCE;
    }

    public CalendarController getCalendarController() {
        return calendarController;
    }

    public void setCalendarController(CalendarController calendarController) {
        this.calendarController = calendarController;
    }

    public void refreshCalendar()
    {
        System.out.println("Refreshing calendar");
        calendarController.refreshCalendar();
    }

}
