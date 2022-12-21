package diary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application {
    static Stage window;


    @Override
    public void start(Stage primaryStage) throws Exception{

        window=primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("diaryCover.fxml"));
        Image icon= new Image("book.png");
        window.getIcons().add(icon);
        window.setTitle("My Digital Diary");

        window.setResizable(false);
        window.setScene(new Scene(root, 800, 502));
        window.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
