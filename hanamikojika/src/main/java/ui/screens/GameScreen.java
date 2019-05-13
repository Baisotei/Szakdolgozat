package ui.screens;

import game.action.Action;
import game.geisha.GeishaOnTable;
import game.item.ItemCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import nisei.Transition;
import ui.ScreenHandler;
import ui.components.ActionButton;
import ui.components.CardButton;
import ui.components.GeishaComponent;

import java.util.ArrayList;
import java.util.List;

import static ui.ScreenHandler.endRound;

public class GameScreen extends GridPane {
    private final HBox geishas = new HBox();
    private final HBox CPUActions = new HBox();
    private final HBox playerActions;
    private final HBox playerHand = new HBox();
    private final HBox CPUCards = new HBox();
    private final Text actionText = new Text();

    public GameScreen() {
        super();
        this.playerActions = initActions();

        init();
    }

    public void selectCard(ActionButton actionButton, String text, int count) {
        playerActions.getChildren().remove(actionButton);
        actionText.setText(text);
        disableActions(true);
        disableCards(false);

        List<ItemCard> selectedCards = new ArrayList<>();

        for (CardButton card : getPlayerCards()) {
            card.select(event -> {
                selectedCards.add(card.getItemCard());
                card.setDisable(true);
                if (selectedCards.size() == count) {
                    actionButton.handleCards(selectedCards);
                    updateHand();
                    disableActions(true);
                    disableCards(true);
                    actionText.setText("Az ellenfél gondolkodik!");
                    endRound(false);
                }
            });
        }
    }

    private void init() {
        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(25, 25, 25, 25));

        add(CPUActions, 0, 0);
        add(geishas, 0, 1);
        add(playerActions, 0, 2);
        add(playerHand, 0, 3);
        add(actionText, 0, 4);
        add(CPUCards, 0, 5);
        CPUActions.setAlignment(Pos.CENTER);
        selectAction();
    }

    public void CPUGift(Transition transition) {
        List<ItemCard> cards = transition.getCards();
        for (int i = 0; i < cards.size(); i++) {
            final int index = i;
            CardButton cardButton = new CardButton(cards.get(index));
            cardButton.select(event -> {
                clearCPUCards();
                transition.getOpponentChoices().add(index);
                ScreenHandler.setBoardState(transition.applyTransition(ScreenHandler.getBoardState(), ScreenHandler.getCPU(), ScreenHandler.getPlayer()));
                endRound(true);
            });
            CPUCards.getChildren().add(cardButton);
        }
    }

    public void CPUCompetition(Transition transition) {
        List<ItemCard> cards = transition.getCards();
        for (int i = 0; i < 2; i++) {
            final int index = i + 1;
            VBox box = new VBox();
            HBox cardsBox = new HBox();
            for (ItemCard card : cards.subList(i * 2, (i * 2) + 2)) {
                CardButton cardButton = new CardButton(card);
                cardButton.setDisable(true);
                cardsBox.getChildren().add(cardButton);
            }
            Button button = new Button(i == 1 ? "Jobb" : "Bal");
            button.setOnAction(event -> {
                clearCPUCards();
                transition.getOpponentChoices().add(index);
                ScreenHandler.setBoardState(transition.applyTransition(ScreenHandler.getBoardState(), ScreenHandler.getCPU(), ScreenHandler.getPlayer()));
                endRound(true);
            });

            box.getChildren().add(cardsBox);
            box.getChildren().add(button);
            CPUCards.getChildren().add(box);
        }
    }

    public void clearCPUCards() {
        CPUCards.getChildren().remove(0, CPUCards.getChildren().size());
    }

    private HBox initActions() {
        final HBox pane = new HBox();
        final List<Action> actions = ScreenHandler.getPlayer().getPossibleActions();

        pane.setAlignment(Pos.CENTER);

        for (int index = 0; index < actions.size(); index++) {
            ActionButton actionButton = new ActionButton(actions.get(index));
            pane.getChildren().add(actionButton);
        }

        return pane;
    }

    public void updateScreen() {
        updateCPUActions();
        updateHand();
        updateGeishas();
    }

    private void updateCPUActions() {
        CPUActions.getChildren().remove(0, CPUActions.getChildren().size());
        for (Action action : ScreenHandler.getCPU().getPossibleActions()) {
            ActionButton actionButton = new ActionButton(action);
            actionButton.setDisable(true);
            CPUActions.getChildren().add(actionButton);
        }
    }

    private void updateHand() {
        playerHand.getChildren().remove(0, playerHand.getChildren().size());

        for (ItemCard card : ScreenHandler.getPlayer().getHand()) {
            CardButton cardButton = new CardButton(card);
            cardButton.setDisable(true);
            playerHand.getChildren().add(cardButton);
        }
    }

    private void updateGeishas() {
        geishas.getChildren().remove(0, geishas.getChildren().size());

        for (GeishaOnTable geisha : ScreenHandler.getBoardState().getTable().getGeishas().getGeishaOnTableList()) {
            GeishaComponent component = new GeishaComponent(geisha);
            geishas.getChildren().add(component);
        }
    }

    public void selectAction() {
        updateScreen();
        disableActions(false);
        disableCards(true);
        actionText.setText("Válassz akciót!");
    }

    private void disableActions(boolean value) {
        for (Node node : playerActions.getChildren()) {
            node.setDisable(value);
        }
    }

    private void disableCards(boolean value) {
        for (Node node : playerHand.getChildren()) {
            node.setDisable(value);
        }
    }

    private List<CardButton> getPlayerCards() {
        List<CardButton> cards = new ArrayList<>();
        for (Node node : playerHand.getChildren()) {
            cards.add((CardButton) node);
        }
        return cards;
    }

    public void setActionText(String actionText) {
        this.actionText.setText(actionText);
    }
}
