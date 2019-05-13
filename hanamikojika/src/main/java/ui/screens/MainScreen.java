package ui.screens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ui.ScreenHandler;

public class MainScreen extends GridPane {

    public MainScreen() {
        super();

        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(10);
        this.setPadding(new Insets(25, 25, 25, 25));

        Text text = new Text("Válaszd ki a kezdőjátékost!");
        text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        this.add(text, 0, 0);

        Button userButton = new Button("Én kezdek!");
        userButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("Én kezdek!");
                ScreenHandler.newGame(true);
            }
        });
        this.add(userButton, 0, 1);

        Button aiButton = new Button("A gép kezdjen!");
        aiButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println("A gép kezd!");
                ScreenHandler.newGame(false);
            }
        });
        this.add(aiButton, 0, 2);
    }
}
