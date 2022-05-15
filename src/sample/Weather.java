package sample;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Weather {

    /**
     * Парсит сайт с погодой на нужный месяц и год,
     * или если погода уже была получена выводит из файла
     *
     * @param month Необходимый месяц
     * @param year  Необходимый год
     * @return Данные о погоде и температуре или null в случае неудачи
     */

    public Map<Integer, String> getWeather(int month, int year) {
        month += 1;
        String currentMonth = Month.of(month).name() + "-" + year;
        String fileWeather = FileWorker.searchWeatherInFile(currentMonth);
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
                FileWorker.addWeatherToFile("forecast", currentMonth + String.valueOf(forecastCurrentMonth));
            } catch (IOException e) {
                return null;
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

    /**
     * Очищает файл с погодой
     */

    public void clearForecast() {
        try (FileWriter writer = new FileWriter("src/files/forecast.txt", false)) {
            writer.write("");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
