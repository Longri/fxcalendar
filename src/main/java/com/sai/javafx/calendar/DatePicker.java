package com.sai.javafx.calendar;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.Calendar;
import java.util.Date;

public class DatePicker implements DateSelection {

    private SimpleIntegerProperty selectedDate = new SimpleIntegerProperty();
    private SimpleIntegerProperty selectedMonth = new SimpleIntegerProperty();
    private SimpleIntegerProperty selectedYear = new SimpleIntegerProperty();
    private final CalendarProperties properties;
    private final DateSelection selection;

    public DatePicker(CalendarProperties properties, DateSelection selection) {
        this.properties = properties;
        this.selection = selection;
        setTo(selection.getSelection());
    }

    public int getSelectedDate() {
        return selectedDate.get();
    }


    public int getSelectedMonth() {
        return selectedMonth.get();
    }

    public int getSelectedYear() {
        return selectedYear.get();
    }

    public void setSelectedDate(int selectedDate) {
        this.selectedDate.set(selectedDate);
    }

    public void setSelectedMonth(int selectedMonth) {
        this.selectedMonth.set(selectedMonth);
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear.set(selectedYear);
    }

    public SimpleIntegerProperty selectedDateProperty() {
        return selectedDate;
    }

    public SimpleIntegerProperty selectedMonthProperty() {
        return selectedMonth;
    }

    public SimpleIntegerProperty selectedYearProperty() {
        return selectedYear;
    }

    public void incrementMonth() {
        int currentMonth = selectedMonth.get();
        if (currentMonth >= (getIndexOfLastMonth())) {
            selectedMonth.set(0);
            increaseYear();
        } else {
            increaseMonth(currentMonth);
        }
    }

    public void decrementMonth() {
        int currentMonth = selectedMonth.get();
        if (currentMonth <= 0) {
            selectedMonth.set(getIndexOfLastMonth());
            decreaseYear();
        } else {
            decreaseMonth(currentMonth);
        }
    }

    private int getIndexOfLastMonth() {
        return FXCalendarUtility.getMonths(properties.getLocale()).length - 2;
    }

    private void increaseMonth(int currentMonth) {
        selectedMonth.set(currentMonth + 1);
    }

    private void decreaseMonth(int currentMonth) {
        selectedMonth.set(currentMonth - 1);
    }

    private void increaseYear() {
        selectedYear.set(selectedYear.get() + 1);
    }

    private void decreaseYear() {
        selectedYear.set(selectedYear.get() - 1);
    }

    @Override
    public void select(Date time) {
        setTo(time);
        selection.select(time);
    }

    @Override
    public Date getSelection() {
        return selection.getSelection();
    }

    private void setTo(Date date) {
        Calendar calendar = FXCalendarUtility.getDateCalendar(date);
        selectedDate.set(calendar.get(Calendar.DAY_OF_MONTH));
        selectedMonth.set(calendar.get(Calendar.MONTH));
        selectedYear.set(calendar.get(Calendar.YEAR));
    }
}