package sample;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Controller {
    public static Calendar today = new GregorianCalendar();
    public static int month;
    public static int year;

    @FXML
    private Label currMonth;

    @FXML
    private GridPane gridPane;

    public void initialize() {
        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);
        fillCalendar();
    }

    public Map<Integer, Integer> getCurrMonth() {
        if (month == 12) {
            month = 0;
            ++year;
        }
        if (month == -1) {
            month = 11;
            --year;
        }
        currMonth.setText(Months.values()[month].toString() + " " + year);
        Map<Integer, Integer> dayToMonth = new HashMap<>();
        Calendar maxDays = new GregorianCalendar(year, month, 1);
        int days = maxDays.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= days; day++) {
            Calendar date = new GregorianCalendar(year, month, day);
            int rightDay = (date.get(Calendar.DAY_OF_WEEK) + 5) % 7;
            dayToMonth.put(date.get(Calendar.DAY_OF_MONTH), rightDay);
        }
        getDaysBefore(dayToMonth);
        return dayToMonth;
    }

    public Map<Integer, Integer> getDaysBefore(Map<Integer, Integer> dayToMonth) {
        Map<Integer, Integer> dayToMonthBefore = new HashMap<>();
        Calendar maxDays = new GregorianCalendar(year, month - 1, 1);
        int days = maxDays.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < dayToMonth.get(1); i++) {
            Calendar date = new GregorianCalendar(year, month - 1, days - i);
            int rightDay = (date.get(Calendar.DAY_OF_WEEK) + 5) % 7;
            dayToMonthBefore.put(date.get(Calendar.DAY_OF_MONTH), rightDay);
        }
        return dayToMonthBefore;
    }

    public Map<Integer, Integer> getDaysAfter(Map<Integer, Integer> dayToMonth) {
        Map<Integer, Integer> dayToMonthAfter = new HashMap<>();
        Calendar maxDays = new GregorianCalendar(year, month, 1);
        int days = maxDays.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day < 43 - days - dayToMonth.get(1); day++) {
            Calendar date = new GregorianCalendar(year, month + 1, day);
            int rightDay = (date.get(Calendar.DAY_OF_WEEK) + 5) % 7;
            dayToMonthAfter.put(date.get(Calendar.DAY_OF_MONTH), rightDay);
        }
        return dayToMonthAfter;
    }

    public int addPanel(Map<Integer, Integer> days, int week, boolean clickable) {
        Set<Integer> setKeys = days.keySet();


        if (clickable) {
            for (Integer k : setKeys) {
                int dayOfWeek = days.get(k);
                if (dayOfWeek == 5 || dayOfWeek == 6) {
                    addButtons(k, days.get(k), week, "-fx-text-fill: red;", "");

                } else {
                    addButtons(k, days.get(k), week, "-fx-text-fill: white;", "");
                }
                if (today.get(Calendar.DAY_OF_MONTH) == k && today.get(Calendar.MONTH) == month && today.get(Calendar.YEAR) == year) {
                    addButtons(k, days.get(k), week, "-fx-text-fill: transparent;", "; -fx-border-color: red; -fx-border-width: 2px");
                }
                if (dayOfWeek == 6) week++;
            }
        } else {
            for (Integer k : setKeys) {
                int dayOfWeek = days.get(k);
                Text text = new Text(String.valueOf(k));
                text.setStyle("-fx-font-size: 25px");
                GridPane.setHalignment(text, HPos.CENTER);
                if (dayOfWeek == 5 || dayOfWeek == 6) {
                    text.setFill(Color.CORAL);
                } else {
                    text.setFill(Color.GRAY);
                }
                gridPane.add(text, dayOfWeek, week);
                if (dayOfWeek == 6) week++;
            }
        }
        return week;
    }


    public void fillCalendar() {
        Map<Integer, Integer> currMonth = getCurrMonth();
        Map<Integer, Integer> dayToMonthBefore = getDaysBefore(currMonth);
        Map<Integer, Integer> dayToMonthAfter = getDaysAfter(currMonth);
        int week = 0;
        week = addPanel(dayToMonthBefore, week, false);
        week = addPanel(currMonth, week, true);
        addPanel(dayToMonthAfter, week, false);
    }

    public void toLowerMonth() {
        clearGridPane();
        --month;
        fillCalendar();
    }

    public void toHigherMonth() {
        clearGridPane();
        ++month;
        fillCalendar();
    }

    public void clearGridPane() {
        Node node = gridPane.getChildren().get(0);
        gridPane.getChildren().clear();
        gridPane.getChildren().add(0, node);
    }


    public void addButtons(int day, int col, int row, String color, String border) {
        String currentDate = String.format("%02d.%02d.%d", day, month, year);
        Button button = new Button(String.valueOf(day));
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: transparent; " + color + " -fx-font-size: 25px" + border);
        button.setId(currentDate);
        GridPane.setConstraints(button, col, row);
        gridPane.getChildren().add(button);
        button.setOnAction(e -> {
            System.out.println(currentDate);
            Scene scene2 = null;
            try {
                scene2 = new Scene(FXMLLoader.load(getClass().getResource("sample2.fxml")));
            } catch (IOException a) {
                a.printStackTrace();//переписать
            }
            Scene finalScene = scene2;
            Main.switchScenes(finalScene);
        });
    }


    public void cellSelected(String currentDate) {
        clearGridPane();
//        AnchorPane newPane = new AnchorPane();
//        root.setTop(newPane);
        Button buttonBack = createBackButton();
        TextArea notes = createNotesArea();
        Text date = createDateText(currentDate);
        Text textNote = createNoteText();
//        buttonBack.setOnAction(t -> {
//            gridPane.getChildren().remove(buttonBack);
//            gridPane.getChildren().remove(date);
//            gridPane.getChildren().remove(notes);
//            gridPane.getChildren().remove(textNote);
//            fillCalendar();
//        });
        buttonBack.setOnAction(t -> {
            Scene scene2 = null;
            try {
                scene2 = new Scene(FXMLLoader.load(getClass().getResource("sample2.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Scene finalScene = scene2;
            Main.switchScenes(finalScene);
        });
    }

    private Button createBackButton() {
        Button buttonBack = new Button("X");
        buttonBack.setMaxWidth(10);
        buttonBack.setMaxHeight(10);
        buttonBack.setStyle("-fx-background-color: red;");
        GridPane.setConstraints(buttonBack, 6, 0);
        GridPane.setHalignment(buttonBack, HPos.RIGHT);
        GridPane.setValignment(buttonBack, VPos.TOP);
        gridPane.getChildren().add(buttonBack);
        return buttonBack;
    }

    private Text createDateText(String currentDate) {
        Text date = new Text(currentDate);
        date.setStyle("-fx-font-size: 50;");
        date.setFill(Color.WHITE);
        GridPane.setValignment(date, VPos.CENTER);
        gridPane.setConstraints(date, 0, 0);
        gridPane.getChildren().add(date);
        return date;
    }

    private TextArea createNotesArea() {
        TextArea textArea = new TextArea();
        gridPane.setConstraints(textArea, 4, 3, 4, 5);
        gridPane.getChildren().add(textArea);
        return textArea;
    }

    private Text createNoteText() {
        Text textNote = new Text("Заметки");
        textNote.setStyle("-fx-font-size: 45;");
        textNote.setFill(Color.WHITE);
        GridPane.setHalignment(textNote, HPos.CENTER);
        gridPane.setConstraints(textNote, 3, 1);
        gridPane.getChildren().add(textNote);
        return textNote;
    }
}

//внедриться в getcurrmonth