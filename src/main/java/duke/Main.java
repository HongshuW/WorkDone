package duke;

import java.io.IOException;
import java.nio.file.Paths;

import duke.ui.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private String filePath = System.getProperty("user.dir");
    private Duke duke = new Duke(Paths.get(filePath, "data", "duke1.txt"));

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setDuke(duke);
            stage.show();
            fxmlLoader.<MainWindow>getController().greetTheUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}