package ui.screens;

import game.item.ItemCard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import ui.ScreenHandler;

import java.util.List;

public class TradeOffScreen extends GridPane {

    public TradeOffScreen(List<ItemCard> cards) {
        super();

        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(10);
        this.setPadding(new Insets(25, 25, 25, 25));

        Text text = new Text("Válaszd ki a kártyát!");
        text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        this.add(text, 0, 0);
        this.add(getCards(cards), 0, 1);
    }

    private Pane getCards(List<ItemCard> cards) {
        GridPane pane = new GridPane();

        for (int index = 0; index < cards.size(); index++) {
            ItemCard card = cards.get(index);
            Button cardButton = new Button("Geisha " + card.getId() + " (" + card.getCharmPoints() + ")");
            cardButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    System.out.println("Választottam");
                    ScreenHandler.startRound();
                }
            });
            pane.add(cardButton, index, 0);
        }

        return pane;
    }
}
