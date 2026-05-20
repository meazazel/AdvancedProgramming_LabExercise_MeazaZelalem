import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class gui extends VBox {

    note n = new note();
    TextArea area = new TextArea();

    Button lockBtn = new Button("🔒 Lock");
    Button unlockBtn = new Button("🔓 Unlock");
    Button newBtn = new Button("➕ New");
    Button colorBtn = new Button("🎨 Color");

    public gui() {

        area.setPromptText("Write your note...");
        area.setWrapText(true);

        HBox topBar = new HBox();
        HBox bottomBar = new HBox();
        Button closeBtn = new Button("✖");

        topBar.getChildren().add(closeBtn);
        topBar.setStyle("-fx-alignment: top-right;");

        setSpacing(10);
        setPadding(new Insets(10));

        // 👇 add this here
        setStyle("""
        -fx-background-color: #fff9a8;
        -fx-background-radius: 20;
        -fx-padding: 10;
        -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 20, 0, 0, 5);
    """);

        getChildren().addAll(topBar, area, bottomBar);

        // actions
        lockBtn.setOnAction(e -> lock());
        unlockBtn.setOnAction(e -> unlock());
        newBtn.setOnAction(e -> newNote());
        colorBtn.setOnAction(e -> changeColor());
    }



    void lock() {
        if (!n.isLocked()) {
            TextInputDialog d = new TextInputDialog();
            d.setHeaderText("Set Password");

            d.showAndWait().ifPresent(pass -> {
                n.setText(area.getText());
                n.lock(pass);

                area.setText(n.getText());
                area.setEditable(false);
            });
        }
    }

    void unlock() {
        if (n.isLocked()) {
            TextInputDialog d = new TextInputDialog();
            d.setHeaderText("Enter Password");

            d.showAndWait().ifPresent(pass -> {
                if (n.unlock(pass)) {
                    area.setText(n.getText());
                    area.setEditable(true);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Wrong password").show();
                }
            });
        }
    }

    void newNote() {
        Main.openNewNote();
    }

    void changeColor() {
        // simple color switch
        String style = area.getStyle();

        if (style.contains("#fff9a8")) {
            area.setStyle("-fx-control-inner-background: #a8f0ff;");
        } else if (style.contains("#a8f0ff")) {
            area.setStyle("-fx-control-inner-background: #ffb3ba;");
        } else {
            area.setStyle("-fx-control-inner-background: #fff9a8;");
        }
    }
}