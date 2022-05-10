package sample;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static sample.Files.addToFile;
import static sample.Files.searchInFile;

public class Weather {

    public static Map<Integer, String> getWeather(int month, int year) {
        month += 1;
        String currentMonth = Month.of(month).name() + "-" + year;
        String fileWeather = searchInFile("forecast", currentMonth);
        Map<Integer, String> forecastCurrentMonth = new HashMap<>();
        if (fileWeather == null) {
            String urlString = "https://world-weather.ru/pogoda/russia/moscow/" + currentMonth + "/";
            System.out.println(urlString);
            ArrayList<String> celsius = new ArrayList<>();
            ArrayList<String> weather = new ArrayList<>();
            try {
                Document page = Jsoup.connect(urlString).get();
                Elements weatherDays = page.getElementsByClass("ww-month");
                String[] forecast = page.select(".ww-month").toString().split("=");
                String[] monthWeatherString = weatherDays.text().split(" ");
                int dayNum = 1;
                for (int i = 0; i < monthWeatherString.length; i += 2) {
                    celsius.add(monthWeatherString[i].replaceFirst(dayNum + "", ""));
                    dayNum++;
                }
                for (int i = 0; i < forecast.length; i++) {
                    if (forecast[i].endsWith("title")) {
                        weather.add(forecast[i + 1].replace(" class", ""));
                    }
                }
                for (int i = 0; i < celsius.size(); i++) {
                    forecastCurrentMonth.put(i, celsius.get(i) + " " + weather.get(i));
                }
                addToFile("forecast", currentMonth, String.valueOf(forecastCurrentMonth));
            } catch (IOException e) {
                System.out.println("Ошибка");
            }
        } else {
            String[] monthWeather = fileWeather.split(", ");
            for (int i = 0; i < monthWeather.length; i++) {
                String degree = monthWeather[i].substring(monthWeather[i].indexOf('=') + 1, monthWeather[i].indexOf(' '));
                String weather = monthWeather[i].substring(monthWeather[i].indexOf(' ') + 1);
                forecastCurrentMonth.put(i, degree + " " + weather);
            }
            System.out.println("взял из файла");
        }
        return forecastCurrentMonth;
    }
}
