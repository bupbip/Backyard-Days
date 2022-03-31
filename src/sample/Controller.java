package sample;

import java.util.*;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;


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
//            Text tmp = new Text(String.valueOf(k));
            addButtons(k,currMonth.get(k),i);
//            gridPane.add(tmp, currMonth.get(k), i);
//            GridPane.setHalignment(tmp, HPos.CENTER);
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


    public void addButtons(int id, int col, int row){
        Button button = new Button(String.valueOf(id));
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMaxHeight(Double.MAX_VALUE);
        button.setStyle("-fx-background-color: transparent;");
        button.setId(String.valueOf(id));
        GridPane.setConstraints(button, col, row);
        gridPane.getChildren().add(button);
        button.setOnAction(e ->{
            System.out.println(id);
        });
    }
}
