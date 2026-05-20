import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Clientgui extends Application {

    PrintWriter out;
    BufferedReader in;

    VBox chatBox = new VBox(10);
    ScrollPane scrollPane = new ScrollPane(chatBox);
    TextField input = new TextField();
    Button sendBtn = new Button("Send");

    @Override
    public void start(Stage stage) throws Exception {
        stage.getIcons().add(new javafx.scene.image.Image("share.png"));


        Socket socket = new Socket("localhost", 4000);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        
        chatBox.setStyle("-fx-padding: 10;");
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #121212; -fx-background-color: #121212;");
        scrollPane.setPrefHeight(450);

        input.setPromptText("Type message...");
        input.setStyle(
                "-fx-background-color: #2b2b2b;" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: gray;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8;"
        );

        sendBtn.setStyle(
                "-fx-background-color: #25D366;" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8 15 8 15;"
        );

        sendBtn.setOnAction(e -> sendMessage());
        input.setOnAction(e -> sendMessage());

        // ===== RECEIVE THREAD =====
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    String finalMsg = msg;
                    Platform.runLater(() -> addMessage(finalMsg, false));
                }
            } catch (Exception ignored) {}
        }).start();

        HBox bottom = new HBox(10, input, sendBtn);
        bottom.setStyle("-fx-padding: 10; -fx-background-color: #1e1e1e;");

        VBox root = new VBox(10, scrollPane, bottom);
        root.setStyle("-fx-background-color: #121212;");

        stage.setScene(new Scene(root, 360, 520));
        stage.setTitle("Chat App");
        stage.show();
    }

    void sendMessage() {
        String msg = input.getText();
        if (msg.isEmpty()) return;

        out.println(msg);
        addMessage(msg, true);
        input.clear();
    }

    void addMessage(String msg, boolean isMe) {
        Label label = new Label(msg);
        label.setWrapText(true);
        label.setMaxWidth(220);

        label.setStyle(
                "-fx-background-color: " + (isMe ? "#25D366" : "#3a3a3a") + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 8;" +
                        "-fx-background-radius: 15;"
        );

        HBox container = new HBox(label);
        container.setMaxWidth(340);

        if (isMe) {
            container.setStyle("-fx-alignment: center-right;");
        } else {
            container.setStyle("-fx-alignment: center-left;");
        }

        chatBox.getChildren().add(container);
        scrollPane.setVvalue(1.0);
    }

    public static void main(String[] args) {
        launch();
    }
}