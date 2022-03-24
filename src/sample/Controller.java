package sample;

import java.net.URL;
import java.util.*;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;

import javax.swing.*;


public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label currMonth;

    @FXML
    private GridPane gridPane;

    public void initialize(){
        Map<Integer,Integer> currMonth = getCurrMonth(2022, 04);
        int i = 0;
        Set<Integer> setKeys = currMonth.keySet();
        for(Integer k: setKeys) {
            Label tmp = new Label(String.valueOf(k));
            gridPane.add(tmp, currMonth.get(k), i);
            if(currMonth.get(k) == 6) i++;
        }
    }

    public Map<Integer, Integer> getCurrMonth(int year, int month){
        currMonth.setText(Months.values()[month].toString());
        Map<Integer, Integer> dayToWeek = new HashMap<>();
//        System.out.println(Months.values()[2]);
//        System.out.println("  Ïí  Âò  Ñð  ×ò  Ïò  Ñá  Âñ");
        Calendar date = new GregorianCalendar(year, month, 1);
        for (int day = 2; day <= date.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
            int rightDay = (date.get(Calendar.DAY_OF_WEEK) + 5) % 7;
            System.out.println("Áûëî: " + date.get(Calendar.DAY_OF_WEEK) + " Ñòàëî: " + rightDay + " ×èñëî: " + date.get(Calendar.DAY_OF_MONTH));
            dayToWeek.put(date.get(Calendar.DAY_OF_MONTH), rightDay);
            date = new GregorianCalendar(year, month, day);
        }
        return dayToWeek;
    }


}
