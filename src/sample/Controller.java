package sample;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.*;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Controller {
    public static Calendar today = new GregorianCalendar();
    public static int month;
    public static int year;
    private static Map<Integer, String> currMonthForecast = new HashMap<>();

    @FXML
    private Label currMonth;

    @FXML
    private ImageView exitButton;

    @FXML
    private TextField toSearch;

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView backgroundImage;

    @FXML
    private Label daysOfWeekLabel;

    @FXML
    private ImageView nextMonthButton;

    @FXML
    private ImageView prevMonthButton;


    public void initialize() {
        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);
        fillCalendar();
    }

    public Map<Integer, Integer> getCurrMonth() {
        updateYear();
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
        updateYear();
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
        updateYear();
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

    public void updateYear() {
        if (month == 12) {
            month = 0;
            ++year;
        }
        if (month == -1) {
            month = 11;
            --year;
        }
    }

    public int addPanel(Map<Integer, Integer> days, int week, boolean clickable) {
        Set<Integer> setKeys = days.keySet();
        String blockType = "stone";
        if (clickable) {
            for (Integer k : setKeys) {
                if (currMonthForecast.get(k - 1).toLowerCase().contains("снег")) {
                    blockType = "snowblock";
                } else if (currMonthForecast.get(k - 1).contains("осадки") || currMonthForecast.get(k - 1).contains("дождь")) {
                    blockType = "waterearthblock";
                } else if (currMonthForecast.get(k - 1).toLowerCase().contains("ясно") || currMonthForecast.get(k - 1).toLowerCase().contains("облачно")) {
                    blockType = "earthblock";
                }
                int dayOfWeek = days.get(k);
                if (dayOfWeek == 5 || dayOfWeek == 6) {
                    addButtons(k, days.get(k), week, blockType, Color.FIREBRICK, false);
                } else {
                    addButtons(k, days.get(k), week, blockType, Color.WHITE, false);
                }
                if (today.get(Calendar.DAY_OF_MONTH) == k && today.get(Calendar.MONTH) == month && today.get(Calendar.YEAR) == year) {
                    addButtons(k, days.get(k), week, blockType, Color.LIME, true);
                }
                if (dayOfWeek == 6) week++;
            }
        } else {
            for (Integer k : setKeys) {
                ImageView block = new ImageView(new Image("images/" + blockType + ".jpg"));
                int dayOfWeek = days.get(k);
                Text text = new Text(String.valueOf(k));
                text.setStyle("-fx-font-size: 25px");
                GridPane.setHalignment(text, HPos.CENTER);
                if (dayOfWeek == 5 || dayOfWeek == 6) {
                    text.setFill(Color.RED);
                } else {
                    text.setFill(Color.WHITE);
                }
                gridPane.add(block, dayOfWeek, week);
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
        currMonthForecast = Weather.getWeather(month, year);
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

    public void search() {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("M.uuuu").withResolverStyle(ResolverStyle.STRICT);
            dateFormatter.parse(toSearch.getText());
            String[] searchArray;
            searchArray = toSearch.getText().split("[.]");
            int searchMonth = Integer.parseInt(searchArray[0]);
            int searchYear = Integer.parseInt(searchArray[1]);
            month = searchMonth - 1;
            year = searchYear;
            clearGridPane();
            fillCalendar();
        } catch (DateTimeParseException e) {
            System.out.println("Date is not valid");
        } finally {
            toSearch.setText("");
        }
    }

    public void clearGridPane() {
        Node node = gridPane.getChildren().get(0);
        gridPane.getChildren().clear();
        gridPane.getChildren().add(0, node);
    }


    public void addButtons(int day, int col, int row, String blockType, Color color, boolean border) {
        String currentDate = String.format("%02d.%02d.%d", day, month + 1, year);
        ImageView button = new ImageView(new Image("images/" + blockType + ".jpg"));
        if (border) {
            button.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(255,0,0,0.8), 10, 0, 0, 0);");
        }
        Text date = new Text(String.valueOf(day));
        date.setStyle("-fx-font-size: 25px/*; -fx-stroke-color: black; -fx-stroke-width: 1px*/");
        GridPane.setHalignment(date, HPos.CENTER);
        date.setFill(color);
        button.setId(currentDate);
        GridPane.setConstraints(button, col, row);
        gridPane.getChildren().add(button);
        gridPane.add(date, col, row);
        gridPane.setCursor(Cursor.HAND);
        button.setOnMouseClicked(e -> {
            cellSelected();
            System.out.println(currentDate);
            System.out.println(currMonthForecast.get(day - 1));
        });
    }

    public void cellSelected() {
        blur(true, backgroundImage, gridPane, daysOfWeekLabel, nextMonthButton, prevMonthButton);
        exitButton.setVisible(true);
        exitButton.setOnMouseClicked(t -> {
            blur(false, backgroundImage, gridPane, daysOfWeekLabel, nextMonthButton, prevMonthButton);
            clearGridPane();
            fillCalendar();
            exitButton.setVisible(false);
        });
    }

    public void blur(boolean blur, Node... objects) {
        if (blur) {
            Arrays.stream(objects).forEach(obj -> obj.setEffect(new GaussianBlur()));
            Arrays.stream(objects).forEach(obj -> obj.setDisable(blur));
        } else {
            Arrays.stream(objects).forEach(obj -> obj.setEffect(null));
            Arrays.stream(objects).forEach(obj -> obj.setDisable(blur));
        }
    }

}
//внедриться в getcurrmonth