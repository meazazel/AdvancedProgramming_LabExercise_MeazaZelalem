import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        openNewNote(); // first note
    }

    public static void openNewNote() {
        Stage stage = new Stage();

        gui root = new gui();

        Scene scene = new Scene(root, 320, 260);
        scene.setFill(null); // VERY IMPORTANT

        stage.initStyle(javafx.stage.StageStyle.TRANSPARENT);
        stage.setTitle("🟡 Sticky Note");
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}