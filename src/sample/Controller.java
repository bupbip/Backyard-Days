package sample;

import java.util.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;


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
        if(month == 12){
            month = 0;
            ++year;
        }
        if(month == -1){
            month = 11;
            --year;
        }
        currMonth.setText(Months.values()[month].toString() + " " + year);
        Map<Integer, Integer> dayToWeek = new HashMap<>();
        Calendar maxDays = new GregorianCalendar(year, month,1);
        int days = maxDays.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int day = 1; day <= days; day++) {
            Calendar date = new GregorianCalendar(year, month, day);
            int rightDay = (date.get(Calendar.DAY_OF_WEEK) + 5) % 7;
            dayToWeek.put(date.get(Calendar.DAY_OF_MONTH), rightDay);
        }
        return dayToWeek;
    }

    public void fillCalendar() {
        Map<Integer,Integer> currMonth = getCurrMonth();
        int i = 0;
        Set<Integer> setKeys = currMonth.keySet();
        for(Integer k: setKeys) {
            Label tmp = new Label(String.valueOf(k));
            tmp.setTextFill(Color.web("#FFFFFF"));
            gridPane.add(tmp, currMonth.get(k), i);
            if(currMonth.get(k) == 6) i++;
        }
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
        gridPane.getChildren().add(0,node);
    }


}
