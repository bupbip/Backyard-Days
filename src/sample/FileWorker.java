package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileWorker {

    /**
     * Добавляет погоду в файл
     *
     * @param filepath Путь до актуального файла
     * @param line     Строчка для сохранения
     */

    public static void addWeatherToFile(String filepath, String line) {
        checkIsFileValid(filepath);
        try (FileWriter writer = new FileWriter(filepath, true)) {
            writer.write(line);
            writer.write("\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Ищет погоду по нужной дате в файле
     *
     * @param filepath Путь до актуального файла
     * @param search   Дата, для поиска
     * @return Строка с нужной погодой
     */

    public static String searchWeatherInFile(String filepath, String search) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
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

    public static String searchNotesInFile(String filepath, String search) {
        String pathToFile = filepath + "notes\\" + search.substring(3) + "_notes.txt";
        checkIsFileValid(pathToFile);
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
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

    public static void addNotesToFile(String filepath, String buttonID, String newNote) {
        String pathToFile = filepath + "notes\\" + buttonID.substring(3) + "_notes.txt";
        checkIsFileValid(pathToFile);
        try (BufferedReader reader = new BufferedReader(new FileReader(pathToFile))) {
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
            savedNotes.append(buttonID).append("\n").append(newNote);
            try (FileWriter writer = new FileWriter(pathToFile, false)) {
                writer.write(savedNotes.toString());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Создание файла в случае его отсутствия
     *
     * @param filepath Путь до файла
     */

    private static void checkIsFileValid(String filepath) {
        File notesFile = new File(filepath);
        if (!notesFile.exists()) {
            try {
                notesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}