package sample;

import java.util.*;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import static javafx.scene.paint.Color.LIGHTCORAL;
import static javafx.scene.paint.Color.RED;


public class Controller {

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
        for(int i = 0; i < dayToMonth.get(1); i++) {
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
        for(int i = 1; i < 43 - days - dayToMonth.get(1); i++) {
            Calendar date = new GregorianCalendar(year, month + 1, i);
            int rightDay = (date.get(Calendar.DAY_OF_WEEK) + 5) % 7;
            dayToMonthAfter.put(date.get(Calendar.DAY_OF_MONTH), rightDay);
        }
        return dayToMonthAfter;
    }

    public int addText(Map<Integer, Integer> days, int week) {
        Set<Integer> setKeys = days.keySet();
        for (Integer k : setKeys) {
            Text text = new Text(String.valueOf(k));
            text.setStyle("-fx-font-size: 25px");
            GridPane.setHalignment(text, HPos.CENTER);
            if(days.get(k) > 4 && days.get(k) < 7) {
                text.setFill(Color.RED);
            } else {
                text.setFill(Color.GRAY);
            }
            gridPane.add(text, days.get(k), week);
            if (days.get(k) == 6) week++;
        }
        return week;
    }

    public void fillCalendar() {
        Map<Integer, Integer> currMonth = getCurrMonth();
        Map<Integer, Integer> dayToMonthBefore = getDaysBefore(currMonth);
        Map<Integer, Integer> dayToMonthAfter = getDaysAfter(currMonth);
        int week = 0;
        week = addText(dayToMonthBefore, week);
        Set<Integer> setKeys = currMonth.keySet();
        for (Integer k : setKeys) {
            if(currMonth.get(k) > 4 && currMonth.get(k) < 7) {
                addButtons(k, currMonth.get(k), week, "red");
            } else {
                addButtons(k, currMonth.get(k), week, "white");
            }
            if (currMonth.get(k) == 6) week++;
        }
        addText(dayToMonthAfter, week);
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


    public void addButtons(int id, int col, int row, String color) {
        Button button = new Button(String.valueOf(id));
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: " + color + " ; -fx-font-size: 25px");
        button.setId(String.valueOf(id));
        GridPane.setConstraints(button, col, row);
        gridPane.getChildren().add(button);
        button.setOnAction(e -> {
            System.out.println(id);
        });
    }
}
