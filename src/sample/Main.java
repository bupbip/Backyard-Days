package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.getIcons().add(new Image("https://thumbs.dreamstime.com/b/vector-pixel-art-watering-tree-isolated-cartoon-vector-pixel-art-watering-tree-122893540.jpg"));
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.primaryStage.setTitle("BACKYARD DAYS");
        this.primaryStage.setScene(new Scene(root));
        this.primaryStage.show();

    }

    public static void switchScenes(Scene scene){
        primaryStage.setScene(scene);
    }
    public static void switchScenesBack(){
        Parent root = null;
        try {
            root = FXMLLoader.load(Main.class.getResource("sample.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setScene(new Scene(root));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
