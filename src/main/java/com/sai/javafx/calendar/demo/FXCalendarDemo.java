package com.sai.javafx.calendar.demo;

import com.sai.javafx.calendar.CalendarProperties;
import com.sai.javafx.calendar.FXCalendar;
import com.sai.javafx.calendar.FXCalendarUtility;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Locale;

public class FXCalendarDemo extends Application {

    private Stage stage;
    private Scene scene;
    private BorderPane root;
    private VBox center;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        this.stage = stage;
        configureScene();
        configureStage();
        configureHeader();
        configureFooter();
        configureCenter();

        configureSimpleDate();
        configureDefaultDate();
        configureWeekNumber();
        configureLocaleCalendar();
        configureCalendarTheme();
    }

    private void configureStage() {
        stage.setTitle("FX Calendar Demo");
        stage.setX(0);
        stage.setY(0);
        stage.setWidth(Screen.getPrimary().getVisualBounds().getWidth());
        stage.setHeight(Screen.getPrimary().getVisualBounds().getHeight());
        stage.setScene(this.scene);
        stage.show();
    }

    private void configureScene() {
        root = new BorderPane();
        root.autosize();
        this.scene = new Scene(root, Color.LINEN);
        loadStyleSheet(scene);
    }

    private void loadStyleSheet(Scene scene) {
        URL resource = FXCalendarDemo.class.getResource("/css/calendar_style.css");
        String urlString = resource.toExternalForm();
        System.out.println(urlString);
        scene.getStylesheets().add(urlString);
    }

    private void configureHeader() {
        StackPane sp = new StackPane();
        sp.setPrefHeight(100);
        sp.setAlignment(Pos.TOP_LEFT);
        sp.setStyle("-fx-background-color: linear-gradient(to bottom, #7A7A7A 0%, #333333 100%);-fx-opacity:.8;-fx-border-width: 0 0 2px 0;-fx-border-color: #868686;");

        Label header = new Label("JavaFX 2 .0 Calendar");
        header.setTextFill(Color.BEIGE);
        header.setTranslateX(10);
        header.setStyle("-fx-font-size:40;");
        header.setTranslateY(25);

        sp.getChildren().addAll(header);
        root.setTop(sp);
    }

    private void configureFooter() {
        StackPane sp = new StackPane();
        sp.setPrefHeight(20);
        sp.setAlignment(Pos.CENTER);
        sp.setStyle("-fx-background-color: linear-gradient(to bottom, #7A7A7A 0%, #333333 100%);-fx-opacity:.8;-fx-border-width: 2px 0 0 0;-fx-border-color: #6D6B69;");

        root.setBottom(sp);
    }

    private void configureCenter() {
        ScrollPane sp = new ScrollPane();
        sp.getStyleClass().add("centerBG");
        center = new VBox();
        center.setPadding(new Insets(10));
        center.setSpacing(25);

        sp.setContent(center);
        root.setCenter(sp);
    }

    private void configureSimpleDate() {
        VBox vb = new VBox();
        vb.setSpacing(10);
        FeatureHeader header = new FeatureHeader("Simple Calendar Control");
        FeatureLabel lbl = new FeatureLabel("Select the date : ");

        HBox hb = new HBox();
        hb.setSpacing(10);
        hb.getChildren().addAll(lbl, new FXCalendar());

        vb.getChildren().addAll(header, hb);
        center.getChildren().add(vb);
    }

    private void configureDefaultDate() {
        VBox vb = new VBox();
        vb.setSpacing(10);
        FeatureHeader header = new FeatureHeader("Calendar Control with Default date and Custom width");
        FeatureLabel lbl = new FeatureLabel("Select the date : ");
        final FXCalendar calendar = new FXCalendar();
        calendar.setPrefWidth(150);
        calendar.setValue(new FXCalendarUtility().convertStringtoDate("15/12/1982"));

        HBox hb = new HBox();
        hb.setSpacing(10);
        hb.getChildren().addAll(lbl, calendar);

        final FeatureLabel lbl2 = new FeatureLabel("");
        Button btn = new Button("Show Selected date");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                lbl2.setText(calendar.getValue().toString());
            }
        });

        HBox hb1 = new HBox();
        hb1.setSpacing(10);
        hb1.getChildren().addAll(btn, lbl2);


        vb.getChildren().addAll(header, hb, hb1);
        center.getChildren().add(vb);
    }

    private void configureWeekNumber() {
        VBox vb = new VBox();
        vb.setSpacing(10);
        FeatureHeader header = new FeatureHeader("Calendar Control with Week Number display");
        FeatureLabel lbl = new FeatureLabel("Select the date : ");
        CalendarProperties properties = new CalendarProperties();
        properties.setShowWeekNumber(true);
        FXCalendar calendar = new FXCalendar(properties);

        HBox hb = new HBox();
        hb.setSpacing(10);
        hb.getChildren().addAll(lbl, calendar);

        vb.getChildren().addAll(header, hb);
        center.getChildren().add(vb);
    }

    private void configureLocaleCalendar() {
        VBox vb = new VBox();
        vb.setSpacing(10);
        FeatureHeader header = new FeatureHeader("Calendar Control with Locale specific");
        FeatureLabel lbl1 = new FeatureLabel("Select the language : ");
        FeatureLabel lbl2 = new FeatureLabel("Select the date : ");
        final CalendarProperties properties = new CalendarProperties();
        final FXCalendar calendar = new FXCalendar(properties);

        ObservableList<String> list = FXCollections.observableArrayList("Default", "English", "French", "German");
        final ChoiceBox<String> cb = new ChoiceBox<String>(list);
        cb.getSelectionModel().select(0);
        cb.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue arg0, Number arg1, Number newValue) {
                int selectionIndex = newValue.intValue();
                if (selectionIndex == 0) {
                    properties.setLocale(Locale.getDefault());
                } else if (selectionIndex == 1) {
                    properties.setLocale(Locale.ENGLISH);
                } else if (selectionIndex == 2) {
                    properties.setLocale(Locale.FRENCH);
                } else if (selectionIndex == 3) {
                    properties.setLocale(Locale.GERMAN);
                }
            }
        });
        HBox hb = new HBox();
        hb.setSpacing(10);
        hb.getChildren().addAll(lbl1, cb);

        HBox hb1 = new HBox();
        hb1.setSpacing(10);
        hb1.getChildren().addAll(lbl2, calendar);

        vb.getChildren().addAll(header, hb, hb1);
        center.getChildren().add(vb);
    }

    private void configureCalendarTheme() {
        VBox vb = new VBox();
        vb.setSpacing(10);
        FeatureHeader header = new FeatureHeader("Calendar Control with different Themes");

        FeatureLabel lbl1 = new FeatureLabel("Select the language ( Red Theme ): ");
        CalendarProperties properties = new CalendarProperties();
        properties.setBaseColor(Color.web("#940C02"));
        final FXCalendar calendar1 = new FXCalendar(properties);

        HBox hb = new HBox();
        hb.setSpacing(10);
        hb.getChildren().addAll(lbl1, calendar1);

        FeatureLabel lbl2 = new FeatureLabel("Select the date ( Black Theme ) : ");
        CalendarProperties properties2 = new CalendarProperties();
        properties2.setBaseColor(Color.BLACK);
        final FXCalendar calendar2 = new FXCalendar(properties2);

        HBox hb1 = new HBox();
        hb1.setSpacing(10);
        hb1.getChildren().addAll(lbl2, calendar2);

        vb.getChildren().addAll(header, hb, hb1);
        center.getChildren().add(vb);
    }

}



