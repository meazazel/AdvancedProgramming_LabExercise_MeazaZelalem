import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.image.Image;

import java.util.*;

public class Game extends Application {

    Deck deck;
    Player user;
    Player computer;
    ArrayList<card> table;

    int stage = 0;
    boolean showdown = false;

    HBox compBox = new HBox(12);
    HBox tableBox = new HBox(12);
    HBox userBox = new HBox(12);

    Button nextBtn = new Button("NEXT");
    Button evalBtn = new Button("SHOWDOWN");
    Button restartBtn = new Button("RESTART");

    Label status = new Label("POKER GAME");
    Label resultLabel = new Label("");

    BorderPane root = new BorderPane();

    @Override
    public void start(Stage stageWindow) {


        startGame();

        root.setStyle("-fx-background-color: radial-gradient(center 50% 50%, radius 80%, #0f6b45, #063a24);");

        VBox top = new VBox(10, compBox);
        top.setAlignment(Pos.CENTER);

        VBox center = new VBox(10, status, tableBox, resultLabel);
        center.setAlignment(Pos.CENTER);

        VBox bottom = new VBox(15, userBox, buttons());
        bottom.setAlignment(Pos.CENTER);

        root.setTop(top);
        root.setCenter(center);
        root.setBottom(bottom);

        nextBtn.setOnAction(e -> nextMove());
        evalBtn.setOnAction(e -> showResult());
        restartBtn.setOnAction(e -> startGame());

        Scene scene = new Scene(root, 1200, 750);
        stageWindow.setScene(scene);
        stageWindow.setTitle("Poker Game");
        stageWindow.getIcons().add(new Image("playing-cards.png"));
        stageWindow.show();

        updateUI();
    }

    // start game
    void startGame() {

        deck = new Deck();
        deck.shuffle();

        user = new Player("You");
        computer = new Player("AI");
        table = new ArrayList<>();

        stage = 0;
        showdown = false;
        resultLabel.setText("");

        // 2 cards each
        for (int i = 0; i < 2; i++) {
            user.addCard(deck.drawCard());
            computer.addCard(deck.drawCard());
        }

        // FLOP (3 cards)
        for (int i = 0; i < 3; i++) {
            table.add(deck.drawCard());
        }

        status.setText("FLOP");
        updateUI();
    }

    // floww

    void nextMove() {

        if (table.size() < 4) {
            table.add(deck.drawCard());
            status.setText("TURN");
        }
        else if (table.size() < 5) {
            table.add(deck.drawCard());
            status.setText("RIVER");
        }
        else {
            status.setText("READY FOR SHOWDOWN");
        }

        updateUI();
    }


    void updateUI() {

        compBox.getChildren().clear();
        tableBox.getChildren().clear();
        userBox.getChildren().clear();

        compBox.setAlignment(Pos.CENTER);
        tableBox.setAlignment(Pos.CENTER);
        userBox.setAlignment(Pos.CENTER);

        // computer cards
        for (card c : computer.getHand()) {

            if (showdown) {
                compBox.getChildren().add(createCard(c));
            } else {
                compBox.getChildren().add(createBackCard());
            }
        }

        // table cards
        for (card c : table) {
            tableBox.getChildren().add(createCard(c));
        }

        // user cards
        for (card c : user.getHand()) {
            userBox.getChildren().add(createCard(c));
        }
    }

    // imaage
    ImageView createCard(card c) {

        String path = System.getProperty("user.dir") + "/png/" + c.getImageName();

        Image img = new Image("file:" + path, true);
        ImageView v = new ImageView(img);

        v.setFitWidth(100);
        v.setFitHeight(140);
        v.setPreserveRatio(true);

        return v;
    }

    ImageView createBackCard() {

        String path = System.getProperty("user.dir") + "/png/back.png";

        Image img = new Image("file:" + path, true);
        ImageView v = new ImageView(img);

        v.setFitWidth(100);
        v.setFitHeight(140);
        v.setPreserveRatio(true);

        return v;
    }

    //result using the showdown button
    void showResult() {

        if (table.size() < 5) {
            resultLabel.setText("Finish community cards first!");
            return;
        }

        showdown = true;
        updateUI();

        ArrayList<card> u = new ArrayList<>(user.getHand());
        ArrayList<card> c = new ArrayList<>(computer.getHand());

        u.addAll(table);
        c.addAll(table);

        Result r1 = Evaluater.evaluate(u);
        Result r2 = Evaluater.evaluate(c);

        String msg;

        if (r1.rank > r2.rank) {
            msg = "YOU WIN\n" + explain(r1.rank, r2.rank, "Player wins with stronger hand");
        }
        else if (r2.rank > r1.rank) {
            msg = "Computer WINS\n" + explain(r1.rank, r2.rank, "Computer wins with stronger hand");
        }
        else {
            msg = "DRAW\nBoth hands equal strength";
        }

        resultLabel.setText(msg);
        resultLabel.setStyle(
                "-fx-text-fill: gold;" +
                        "-fx-font-size: 20px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-color: rgba(0,0,0,0.4);" +
                        "-fx-padding: 10;"
        );
    }

    // why they win( explanation)
    String explain(int r1, int r2, String base) {

        String hand =
                r1 >= 7 ? "Four of a Kind / Full House level" :
                        r1 == 5 ? "Flush level" :
                                r1 == 4 ? "Straight level" :
                                        r1 == 3 ? "Three of a Kind level" :
                                                r1 == 1 ? "Pair level" :
                                                        "High Card";

        return base + "\nWinning hand: " + hand;
    }


    HBox buttons() {

        style(nextBtn, "#1e90ff");
        style(evalBtn, "#ffcc00");
        style(restartBtn, "#ff4444");

        HBox box = new HBox(15, nextBtn, evalBtn, restartBtn);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    void style(Button b, String color) {

        b.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 10 25 10 25;" +
                        "-fx-background-radius: 20;"
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}