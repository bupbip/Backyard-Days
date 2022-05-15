package sample;

import java.util.*;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Controller {

    /**
     * Объявление переменных
     */

    private static final Calendar today = new GregorianCalendar();
    private static int month;
    private static int year;
    private static Map<Integer, String> currMonthForecast = new HashMap<>();
    private Weather weather;

    @FXML
    private Label currMonth;

    @FXML
    private ImageView exitButton;

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

    @FXML
    private TextArea textArea;

    @FXML
    private ImageView saveButton;

    @FXML
    private ImageView dayMenuImage;

    @FXML
    private ImageView placeToBlockImage;

    @FXML
    private ImageView resetButton;

    @FXML
    private ImageView updateText;

    @FXML
    private TextField toSearch;

    @FXML
    private ImageView searchButton;

    @FXML
    private ImageView toSearchImage;

    @FXML
    private ImageView flower;

    /**
     * Выполняется при запуске, задаёт текущие месяц и год в актуальные
     */

    public void initialize() {
        month = Calendar.getInstance().get(Calendar.MONTH);
        year = Calendar.getInstance().get(Calendar.YEAR);
        weather = new Weather();
        fillCalendar();
    }

    /**
     * Выводит надпись в левый верхний угол о текущем месяце и годе,
     * назначает каждому дню свой день недели
     *
     * @return Мапу, где ключ - день месяца, значение - день недели, который он занимает
     */

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

    /**
     * Генерирует месяц, идущий до текущего, чтобы отобразить последние даты
     *
     * @param dayToMonth Мапа дней текущего месяца, с учтенными днями недели
     * @return Мапу последних дат предыдущего месяца
     */

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

    /**
     * Генерирует месяц, идущий после текущего, чтобы отобразить первые даты
     *
     * @param dayToMonth Мапа дней текущего месяца, с учтенными днями недели
     * @return Мапу первых дат последующего месяца
     */

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

    /**
     * Если месяц меньше или больше допустимого, меняет значение года
     */

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

    /**
     * Задаёт текстуру в зависимости от погоды в этот день,
     * если день текущего месяца, он будет кликабелен
     * вызываем метод, устанавливающий текстуру на каждый день
     *
     * @param days      Мапа дней месяца, дни которого ставим в календарь
     * @param week      Неделя, в которую нужно поставить данный день
     * @param clickable Будет ли день являться кликабельным(текущий месяц)
     * @return Номер недели месяца, на котором мы остановились
     */

    public int addPanel(Map<Integer, Integer> days, int week, boolean clickable) {
        Set<Integer> setKeys = days.keySet();
        String blockType = "stone";
        if (clickable) {
            for (Integer k : setKeys) {
                if (currMonthForecast == null) {
                    blockType = "earthblock";
                } else if (currMonthForecast.get(k - 1).toLowerCase().contains("снег")) {
                    blockType = "snowblock";
                } else if (currMonthForecast.get(k - 1).contains("осадки") || currMonthForecast.get(k - 1).contains("дождь")) {
                    blockType = "waterearthblock";
                } else if (currMonthForecast.get(k - 1).toLowerCase().contains("ясно") || currMonthForecast.get(k - 1).toLowerCase().contains("облачно")) {
                    blockType = "earthblock";
                }
                int dayOfWeek = days.get(k);
                if (dayOfWeek == 5 || dayOfWeek == 6) {
                    addButtons(k, days.get(k), week, blockType, Color.CYAN);
                } else {
                    addButtons(k, days.get(k), week, blockType, Color.WHITE);
                }
                if (today.get(Calendar.DAY_OF_MONTH) == k && today.get(Calendar.MONTH) == month && today.get(Calendar.YEAR) == year) {
                    addButtons(k, days.get(k), week, blockType, Color.LIME);
                }
                if (dayOfWeek == 6) week++;
            }
        } else {
            for (Integer k : setKeys) {
                ImageView block = new ImageView(new Image("images/blocks/" + blockType + ".jpg"));
                int dayOfWeek = days.get(k);
                Text text = new Text(String.valueOf(k));
                text.setStyle("-fx-font-size: 35px; -fx-stroke-color: black; -fx-stroke-width: 1px");
                text.setStroke(Color.BLACK);
                GridPane.setHalignment(text, HPos.CENTER);
                if (dayOfWeek == 5 || dayOfWeek == 6) {
                    text.setFill(Color.CYAN);
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

    /**
     * Генерирует текущий месяц, предыдущий и следующий,
     * получает погоду,
     * меняет счетчик недели в зависимости от возвращенной,
     * вызывает метод добавления дней каждого месяца в календарь.
     */

    public void fillCalendar() {
        Map<Integer, Integer> currMonth = getCurrMonth();
        Map<Integer, Integer> dayToMonthBefore = getDaysBefore(currMonth);
        Map<Integer, Integer> dayToMonthAfter = getDaysAfter(currMonth);
        int week = 0;
        currMonthForecast = weather.getWeather(month, year);
        week = addPanel(dayToMonthBefore, week, false);
        week = addPanel(currMonth, week, true);
        addPanel(dayToMonthAfter, week, false);
    }

    /**
     * Меняет месяц на предыдущий и генерирует календарь заново
     */

    public void toLowerMonth() {
        clearGridPane();
        --month;
        fillCalendar();
    }

    /**
     * Меняет месяц на следующий и генерирует календарь заново
     */

    public void toHigherMonth() {
        clearGridPane();
        ++month;
        fillCalendar();
    }

    /**
     * Поиск необходимого месяца и года
     */

    public void search() {
        String[] searchArray;
        searchArray = toSearch.getText().split("[.]");
        int searchMonth = Integer.parseInt(searchArray[0]);
        int searchYear = Integer.parseInt(searchArray[1]);
        if (searchMonth > 0 && searchMonth < 13) {
            month = searchMonth - 1;
            year = searchYear;
            clearGridPane();
            fillCalendar();
        } else {
            System.out.println("Миссия невыполнима");
        }
        toSearch.setText("");
    }

    /**
     * Очистка поля
     */

    public void clearGridPane() {
        Node node = gridPane.getChildren().get(0);
        gridPane.getChildren().clear();
        gridPane.getChildren().add(0, node);
    }

    /**
     * Добавляет кликабельные картинки
     *
     * @param day       День
     * @param col       Колонка в таблице
     * @param row       Строка в таблице
     * @param blockType Тип картинки
     * @param color     Цвет цифры дня на картинке
     */

    public void addButtons(int day, int col, int row, String blockType, Color color) {
        String currentDate = String.format("%02d.%02d.%d", day, month + 1, year);
        ImageView button = new ImageView(new Image("images/blocks/" + blockType + ".jpg"));
        Text date = new Text(String.valueOf(day));
        date.setStyle("-fx-font-size: 35px; -fx-stroke-color: black; -fx-stroke-width: 1px");
        GridPane.setHalignment(date, HPos.CENTER);
        date.setFill(color);
        date.setStroke(Color.BLACK);
        button.setId(currentDate + "," + blockType);
        GridPane.setConstraints(button, col, row);
        gridPane.getChildren().add(button);
        gridPane.add(date, col, row);
        gridPane.setCursor(Cursor.HAND);
        DayMenu thisDayMenu = new DayMenu();
        button.setOnMouseClicked(e -> {
            thisDayMenu.cellSelected(button.getId(), placeToBlockImage, flower, dayMenuImage, exitButton, textArea, saveButton, backgroundImage, gridPane, daysOfWeekLabel, nextMonthButton, prevMonthButton, toSearch, searchButton, resetButton, updateText, toSearchImage);
            System.out.println(currentDate);
            System.out.println(currMonthForecast.get(day - 1));
        });
        date.setOnMouseClicked(e -> {
            thisDayMenu.cellSelected(button.getId(), placeToBlockImage, flower, dayMenuImage, exitButton, textArea, saveButton, backgroundImage, gridPane, daysOfWeekLabel, nextMonthButton, prevMonthButton, toSearch, searchButton, resetButton, updateText, toSearchImage);
            System.out.println(currentDate);
            System.out.println(currMonthForecast.get(day - 1));
        });
    }

    @FXML
    private void clearForecast() {
        weather.clearForecast();
    }

}