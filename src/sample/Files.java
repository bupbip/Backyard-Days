package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Files {
    public static void addToFile(String filename, String... lines) {
        try (FileWriter writer = new FileWriter("src/files/" + filename + ".txt", true)) {
            for (String line : lines) {
                writer.write(line);
            }
            writer.write("\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static String searchInFile(String filename, String search) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/files/" + filename + ".txt"))) {
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

    public static String searchNotesInFile(String filename, String search) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/files/" + filename + ".txt"))) {
            String line = reader.readLine();
            boolean write = false;
            StringBuilder lines = new StringBuilder();
            while (line != null) {
                if(line.equals("EOT")) {
                    write = false;
                }
                if(write) {
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

    public static void addNotesToFile(String filename,String buttonID, String newNote) {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/files/" + filename + ".txt"))) {
            String line = reader.readLine();
            boolean save = true;
            StringBuilder savedNotes = new StringBuilder();
            while (line != null) {
                if (line.equals(buttonID)) {
                    save = false;
                }
                if (save) {
                    savedNotes.append(line).append("\n");
                }
                if (line.equals("EOT")) {
                    save = true;
                }
                line = reader.readLine();
            }
            savedNotes.append(newNote);
        try (FileWriter writer = new FileWriter("src/files/" + filename + ".txt", false)) {
            writer.write(savedNotes.toString());
        }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}