package ui.components;

import game.item.ItemCard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

public class CardButton extends Button {
    private ItemCard itemCard;

    public CardButton(ItemCard itemCard) {
        super(itemCard.getId() + " (" + itemCard.getCharmPoints() + ")");
        this.itemCard = itemCard;

        this.setMinSize(60, 100);
        this.setMaxSize(60, 100);
    }

    public ItemCard getItemCard() {
        return this.itemCard;
    }

    public void select(EventHandler<ActionEvent> eventHandler) {
        this.setOnAction(eventHandler);
    }
}
