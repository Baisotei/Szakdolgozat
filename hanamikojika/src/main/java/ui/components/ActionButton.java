package ui.components;

import game.action.Action;
import game.board.BoardState;
import game.board.Player;
import game.item.ItemCard;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import ui.ScreenHandler;

import java.util.List;

public class ActionButton extends Button {
    private Action actionType;

    public ActionButton(Action actionType) {
        super(getImage(actionType));
        this.actionType = actionType;

        this.setMinSize(65, 65);
        this.setMaxSize(65, 65);
        this.setOnAction(initAction());
    }

    public void handleCards(List<ItemCard> selectedCards) {
        BoardState boardState = ScreenHandler.getBoardState();
        Player CPU = ScreenHandler.getCPU();
        Player player = ScreenHandler.getPlayer();

        switch (this.actionType) {
            case COMPETITION: {
                System.out.println("4 - Versengés: válassz két-két lapot a kezedből!");
                List<ItemCard> picks1 = selectedCards.subList(0, 2);
                List<ItemCard> picks2 = selectedCards.subList(2, 4);
                int myPick = ScreenHandler.getAi().competitionChoice(boardState, CPU, player, picks1, picks2);

                System.out.println("A gép a " + myPick + ". lehetőséget választotta.");

                if (myPick == 1) {
                    player.competitionAction(picks2, CPU, picks1);
                } else {
                    player.competitionAction(picks1, CPU, picks2);
                }

                System.out.println("Versengés kész!");
                return;
            }
            case GIFT: {
                ItemCard myPick = ScreenHandler.getAi().giftChoice(boardState, CPU, player, selectedCards);
                System.out.println("A gép ezt választotta: " + myPick.getId() + "(" + myPick.getCharmPoints() + ")");
                selectedCards.remove(myPick);
                player.giftAction(selectedCards, CPU, myPick);
                System.out.println("Ajándék kész!");
                return;
            }
            case SECRET: {
                System.out.println("1 - Titok: válassz egy lapot a kezedből!");
                player.secretAction(selectedCards.get(0));
                System.out.println("Titok kész!");
                return;
            }
            case TRADEOFF: {
                System.out.println("2 - Tradeoff: válassz két lapot a kezedből!");
                player.tradeoffAction(selectedCards);
                System.out.println("Tradeoff kész!");
            }
        }
    }

    private EventHandler<ActionEvent> initAction() {
        switch (this.actionType) {
            case COMPETITION: return competition(this);
            case GIFT: return gift(this);
            case SECRET: return secret(this);
            case TRADEOFF: return tradeoff(this);
        }
        return null;
    }

    private static EventHandler<ActionEvent> competition(ActionButton button) {
        return e -> ScreenHandler.selectCard(button, "Válassz 2-2 kártyát", 4);
    }

    private static EventHandler<ActionEvent> gift(ActionButton button) {
        return e -> ScreenHandler.selectCard(button, "Válassz 3 kártyát", 3);
    }

    private static EventHandler<ActionEvent> secret(ActionButton button) {
        return e -> ScreenHandler.selectCard(button, "Válassz 1 kártyát", 1);
    }

    private static EventHandler<ActionEvent> tradeoff(ActionButton button) {
        return e -> ScreenHandler.selectCard(button, "Válassz 2 kártyát", 2);
    }

    private static String getImage(Action action) {
        switch (action) {
            case SECRET: return "SECRET";
            case TRADEOFF: return "TRADE";
            case GIFT: return "GIFT";
            case COMPETITION: return "COMP";
            default: return ":(";
        }
    }
}
