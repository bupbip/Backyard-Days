package sample;

import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

public class DayMenu {

    /**
     * Открываем меню дня, доступ к заметкам, запрещает нажимать кнопки
     *
     * @param buttonID Айди кнопки(дата)
     */

    public void cellSelected(String pathToFiles, String buttonID, ImageView dialogueImage, Text dialogueText, Text currentDate, ImageView placeToBlockImage, ImageView flower, ImageView dayMenuImage, ImageView exitButton, TextArea textArea, ImageView saveButton, Node... elements) {
//        String pathToNotesFile = pathToFiles;
        AtomicBoolean haveTasks = new AtomicBoolean(DayMenu.getNumOfNotes(FileWorker.searchNotesInFile(pathToFiles, new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()))) > 0);
        visible(haveTasks.get(), dialogueImage, dialogueText);
        blur(true, elements);
        visible(true, exitButton, textArea, saveButton, dayMenuImage, placeToBlockImage, flower, currentDate);
        String cellDate = buttonID.split(",")[0];
        String blockType = buttonID.split(",")[1];
        placeToBlockImage.setImage(new Image("images/blocks/" + blockType + ".jpg"));
        currentDate.setText(cellDate);
        String notes = FileWorker.searchNotesInFile(pathToFiles, cellDate).trim();
        textArea.setText(notes);
        int numOfNotes = getNumOfNotes(notes);
        flower.setImage(new Image("images/flowers/цветок" + numOfNotes + ".png"));
        saveButton.setOnMouseClicked(e -> {
            FileWorker.addNotesToFile(pathToFiles,cellDate, textArea.getText() + "\nEOT");
            haveTasks.set(DayMenu.getNumOfNotes(FileWorker.searchNotesInFile(pathToFiles, new SimpleDateFormat("dd.MM.yyyy").format(Calendar.getInstance().getTime()))) > 0);
            visible(haveTasks.get(), dialogueImage, dialogueText);
        });
        exitButton.setOnMouseClicked(t -> {
            textArea.setText("");
            blur(false, elements);
            visible(false, exitButton, textArea, saveButton, dayMenuImage, placeToBlockImage, flower, currentDate);
        });
    }

    /**
     * Блюрит и разблюривает фон
     *
     * @param blur       Блюрить или разблюрить
     * @param needToBlur Объекты, которые нужно отключить
     */

    public void blur(boolean blur, Node... needToBlur) {
        if (blur) {
            Arrays.stream(needToBlur).forEach(obj -> obj.setEffect(new GaussianBlur()));
        } else {
            Arrays.stream(needToBlur).forEach(obj -> obj.setEffect(null));
        }
        Arrays.stream(needToBlur).forEach(obj -> obj.setDisable(blur));
    }

    /**
     * Меняет видимость объектов
     *
     * @param visible       отображать|не отображать
     * @param needToVisible объекты для взаимодействия
     */

    public static void visible(boolean visible, Node... needToVisible) {
        Arrays.stream(needToVisible).forEach(obj -> obj.setVisible(visible));
    }

    /**
     * Считает количество заметок
     *
     * @param notes заметки в конкретный день
     * @return кол-во заметок
     */

    public static int getNumOfNotes(String notes) {
        if (notes.equals("\n")) {
            return 0;
        }
        int counter = 0;
        for (int i = 0; i < notes.length(); i++) {
            if (notes.charAt(i) == '\n') {
                counter++;
            }
        }
        return Math.min(counter, 3);
    }
}