package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileWorker {

    /**
     * Добавляет погоду в файл
     *
     * @param line
     */

    public static void addWeatherToFile(String filename, String line) {
        try (FileWriter writer = new FileWriter("src/files/forecast.txt", true)) {
            writer.write(line);
            writer.write("\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Ищет погоду по нужной дате в файле
     *
     * @param search Дата, для поиска
     * @return Строка с нужной погодой
     */

    public static String searchWeatherInFile(String search) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/files/forecast.txt"))) {
            String line = reader.readLine();
            while (line != null) {
                if (line.startsWith(search)) {
                    return line;
                }
                line = reader.readLine();
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    /**
     * Ищет заметки по нужной дате
     *
     * @param search Дата для поиска
     * @return Заметки на текущий день или null при их отсутствии
     */

    public static String searchNotesInFile(String search) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/files/notes.txt"))) {
            String line = reader.readLine();
            boolean write = false;
            StringBuilder lines = new StringBuilder();
            while (line != null) {
                if (line.equals("EOT")) {
                    write = false;
                }
                if (write) {
                    lines.append(line).append("\n");
                }
                if (line.startsWith(search)) {
                    write = true;
                }
                line = reader.readLine();
            }
            return lines.toString();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    /**
     * Добавляет заметки в файл на нужную дату
     *
     * @param buttonID Дата
     * @param newNote  Заметки для добавления
     */

    public static void addNotesToFile(String buttonID, String newNote) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/files/notes.txt"))) {
            String line = reader.readLine();
            boolean save = true;
            StringBuilder savedNotes = new StringBuilder();
            while (line != null) {
                if (line.equals(buttonID)) {
                    save = false;
                }
                if (save && !line.equals("\n")) {
                    savedNotes.append(line).append("\n");
                }
                if (line.equals("EOT")) {
                    save = true;
                }
                line = reader.readLine();
            }
            savedNotes.append(buttonID + newNote + "\n");
            try (FileWriter writer = new FileWriter("src/files/notes.txt", false)) {
                writer.write(savedNotes.toString());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}