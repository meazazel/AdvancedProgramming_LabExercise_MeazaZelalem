import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.*;

public class note_pad extends Application {

    TextArea textArea = new TextArea();
    int fontSize = 14;

    @Override
    public void start(Stage primaryStage) {


        primaryStage.getIcons().add(new Image("file:src/note.png"));


        BorderPane root = new BorderPane();

        // menu bar
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem newFile = new MenuItem("New");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");
        fileMenu.getItems().addAll(newFile, open, save, exit);

        Menu editMenu = new Menu("Edit");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        MenuItem cut = new MenuItem("Cut");
        MenuItem bigFont = new MenuItem("Big Font");
        MenuItem smallFont = new MenuItem("Small Font");
        MenuItem find = new MenuItem("Find");
        editMenu.getItems().addAll(copy, paste, cut, bigFont, smallFont, find);

        Menu themeMenu = new Menu("Themes");
        MenuItem dark = new MenuItem("Dark");
        MenuItem light = new MenuItem("Light");
        themeMenu.getItems().addAll(dark, light);

        menuBar.getMenus().addAll(fileMenu, editMenu, themeMenu);

        // choosing a file
        FileChooser fileChooser = new FileChooser();

        // count (num of words)
        Label wordCount = new Label("Words: 0");

        textArea.textProperty().addListener((obs, oldText, newText) -> {
            if (newText.trim().isEmpty()) {
                wordCount.setText("Words: 0");
            } else {
                String[] words = newText.trim().split("\\s+");
                wordCount.setText("Words: " + words.length);
            }
        });



        newFile.setOnAction(e -> textArea.clear());
        exit.setOnAction(e -> primaryStage.close());

        copy.setOnAction(e -> textArea.copy());
        paste.setOnAction(e -> textArea.paste());
        cut.setOnAction(e -> textArea.cut());

        // save
        save.setOnAction(e -> {
            try {
                File file = fileChooser.showSaveDialog(primaryStage);
                if (file != null) {
                    FileWriter writer = new FileWriter(file);
                    writer.write(textArea.getText());
                    writer.close();
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });

        // open
        open.setOnAction(e -> {
            try {
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    StringBuilder content = new StringBuilder();

                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }

                    textArea.setText(content.toString());
                    reader.close();
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });

        // font
        bigFont.setOnAction(e -> {
            fontSize += 2;
            textArea.setStyle("-fx-font-size: " + fontSize + ";");
        });

        smallFont.setOnAction(e -> {
            fontSize -= 2;
            textArea.setStyle("-fx-font-size: " + fontSize + ";");
        });

        // find
        find.setOnAction(e -> {

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Find");
            dialog.setHeaderText("Highlight All Matches");
            dialog.setContentText("Enter word:");

            dialog.showAndWait().ifPresent(word -> {

                String text = textArea.getText();
                String lowerText = text.toLowerCase();
                String search = word.toLowerCase();

                if (!lowerText.contains(search)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("No matches found");
                    alert.showAndWait();
                    return;
                }

                TextFlow flow = new TextFlow();
                int lastIndex = 0;
                int index;

                while ((index = lowerText.indexOf(search, lastIndex)) >= 0) {


                    Text normal = new Text(text.substring(lastIndex, index));
                    normal.setStyle("-fx-font-size: " + fontSize + ";");
                    // highlighted text
                    Text highlight = new Text(text.substring(index, index + word.length()));
                    highlight.setStyle(
                            "-fx-fill: black;" +
                                    "-fx-background-color: yellow;" +
                                    "-fx-font-size: " + fontSize + ";"
                    );

                    flow.getChildren().addAll(normal, highlight);

                    lastIndex = index + word.length();
                }

                // remaining text
                Text remaining = new Text(text.substring(lastIndex));
                remaining.setStyle("-fx-font-size: " + fontSize + ";");

                flow.getChildren().add(remaining);

                ScrollPane scrollPane = new ScrollPane(flow);
                scrollPane.setFitToWidth(true);

                Stage resultStage = new Stage();
                resultStage.setTitle("Highlighted Results");
                resultStage.setScene(new Scene(scrollPane, 600, 400));
                resultStage.show();
            });
        });

        // theme
        dark.setOnAction(e -> {
            root.getStyleClass().remove("light");
            root.getStyleClass().add("dark");
        });

        light.setOnAction(e -> {
            root.getStyleClass().remove("dark");
            root.getStyleClass().add("light");
        });

        // layout
        root.setTop(menuBar);
        root.setCenter(textArea);
        root.setBottom(wordCount);

        Scene scene = new Scene(root, 700, 450);
        root.getStyleClass().add("light"); // default theme
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        scene.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode().toString().equals("S")) {
                save.fire();
            }
        });
        primaryStage.getIcons().add(new Image("note (1).png"));
        primaryStage.setTitle("Notepad");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
