package com.sai.javafx.calendar;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FXCalendar extends HBox implements DateSelection {

    private SimpleIntegerProperty selectedDate = new SimpleIntegerProperty();
    private SimpleIntegerProperty selectedMonth = new SimpleIntegerProperty();
    private SimpleIntegerProperty selectedYear = new SimpleIntegerProperty();
    private SimpleDoubleProperty dateTextWidth = new SimpleDoubleProperty(74);
    private SimpleObjectProperty<Date> value = new SimpleObjectProperty<>();
    private DateTextField dateTxtField;
    private final String DEFAULT_STYLE_CLASS = "fx-calendar";
    private CalendarProperties properties;
    private DatePickerPopup pickerPopup;

    public FXCalendar() {
        this(new CalendarProperties());
    }

    public FXCalendar(CalendarProperties properties) {
        super.getStyleClass().add(DEFAULT_STYLE_CLASS);
        this.properties = properties;
        this.pickerPopup = new DatePickerPopup(this, properties, new TodayDefaultSelection(this));
        setAlignment(Pos.CENTER);
        configureCalendar();
        configureListeners();
    }

    private void configureCalendar() {
        final DateFormatValidator dateFormatValidator = new DateFormatValidator();

        addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                if (KeyCode.UP.equals(event.getCode()) || KeyCode.DOWN.equals(event.getCode()) || KeyCode.ENTER.equals(event.getCode())) {
                    pickerPopup.show();
                } else if (KeyCode.TAB.equals(event.getCode())) {
                    pickerPopup.hide();
                }
            }
        });
        createDateTextField(dateFormatValidator);
        Node popupButton = new DatePopupButton(pickerPopup).getComponent();
        getChildren().addAll(dateTxtField, popupButton);
    }

    @Override
    public Date getSelection() {
        return getValue();
    }

    private void createDateTextField(final DateFormatValidator dateFormatValidator) {
        dateTxtField = new DateTextField();
        dateTxtField.prefWidthProperty().bind(dateTextWidth);
        ChangeListener<Boolean> focusOutListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                // Handling only when focus is out.
                if (!arg2) {
                    String value = dateTxtField.getText();
                    if (!dateFormatValidator.isValid(value)) {
                        clear(); // TODO : Error styling for invalid date format.
                        dateTxtField.setText(value);
                    } else {
                        Date date = FXCalendarUtility.convertStringtoDate(value);
                        if (date != null) {
                            setValue(date);
                        } else {
                            // TODO : Error styling the text field for invalid date
                            // entry.
                            clear();
                        }
                    }
                }
            }
        };
        dateTxtField.focusedProperty().addListener(focusOutListener);
    }

    private void configureListeners() {
        /*
           * Changes to be done in text box on change of seletedDate ,
           * selectedMonth and selectedYear in DatePicker.
           */
        ChangeListener<Object> listener = new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
                showDateInTextField();
            }
        };

        selectedDateProperty().addListener(listener);
        selectedMonthProperty().addListener(listener);
        selectedYearProperty().addListener(listener);
        showDateInTextField();

        properties.localeProperty().addListener(new ChangeListener<Locale>() {
            @Override
            public void changed(ObservableValue<? extends Locale> arg0, Locale arg1, Locale arg2) {
                pickerPopup.refreshDisplayText();
            }
        });

        /* Adding listeners for styles. */
        getStyleClass().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(javafx.collections.ListChangeListener.Change<? extends String> paramChange) {
                dateTxtField.getStyleClass().clear();
                dateTxtField.getStyleClass().addAll("text-input", "text-field");
                for (String clazz : getStyleClass()) {
                    if (!clazz.equals(DEFAULT_STYLE_CLASS)) {
                        dateTxtField.getStyleClass().add(clazz);
                    }
                }
            }
        });
    }

    public void showDateInTextField() {
        int date = selectedDateProperty().get();
        int month = selectedMonthProperty().get();
        int year = selectedYearProperty().get();
        if (date != 0 && month != -1 && year != 0) {
            dateTxtField.setText(FXCalendarUtility.getFormattedDate(date, month, year));
        } else {
            dateTxtField.setText("");
        }
    }

    public Double getDateTextWidth() {
        return dateTextWidth.get();
    }

    public void setDateTextWidth(Double width) {
        this.dateTextWidth.set(width);
    }

    public SimpleDoubleProperty dateTextWidthProperty() {
        return dateTextWidth;
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

    public Date getValue() {
        return this.value.get();
    }

    public void setValue(Date date) {
        this.value.set(date);
        if (date != null) {
            Calendar calendar = FXCalendarUtility.getDateCalendar(date);
            selectedDateProperty().set(calendar.get(Calendar.DAY_OF_MONTH));
            selectedMonthProperty().set(calendar.get(Calendar.MONTH));
            selectedYearProperty().set(calendar.get(Calendar.YEAR));
        } else {
            selectedDateProperty().set(0);
            selectedMonthProperty().set(0);
            selectedYearProperty().set(0);
        }
    }

    /**
     * Method to clear the value in the calendar.
     */
    public void clear() {
        setValue(null);
    }

    public SimpleObjectProperty<Date> valueProperty() {
        return value;
    }

    public TextField getTextField() {
        return dateTxtField;
    }

    @Override
    public void select(Date time) {
        setValue(time);
        getTextField().requestFocus();
        showDateInTextField();
        pickerPopup.hide();
    }
}