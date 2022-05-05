package sample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
}