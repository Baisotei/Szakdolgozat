package game.board;

import game.action.Action;
import game.geisha.GeishaOnTable;
import game.item.ItemCard;
import game.item.ItemDeck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Player implements Serializable {

    private int id;

    private List<ItemCard> hand;

    private List<Action> possibleActions;

    private ItemCard secretCard;

    private List<ItemCard> tradeoffCards;

    private Table table;

    public Player(int id) {
        this.hand = new ArrayList<>();
        this.possibleActions = new ArrayList<>(Arrays.asList(Action.SECRET, Action.TRADEOFF, Action.GIFT, Action.COMPETITION));
        this.secretCard = null;
        this.tradeoffCards = new ArrayList<>();
        this.table = null;
        this.id = id;
    }

    public List<ItemCard> getHand() {
        return hand;
    }

    public void setHand(List<ItemCard> hand) {
        this.hand = hand;
    }

    public List<Action> getPossibleActions() {
        return possibleActions;
    }

    public void setPossibleActions(List<Action> possibleActions) {
        this.possibleActions = possibleActions;
    }

    public ItemCard getSecretCard() {
        return secretCard;
    }

    public void setSecretCard(ItemCard secretCard) {
        this.secretCard = secretCard;
    }

    public List<ItemCard> getTradeoffCards() {
        return tradeoffCards;
    }

    public void setTradeoffCards(List<ItemCard> tradeoffCards) {
        this.tradeoffCards = tradeoffCards;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return getId() == player.getId();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getHand(), getPossibleActions(), getSecretCard(), getTradeoffCards(), getTable());
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", possibleActions=" + possibleActions +
                '}';
    }

    public void drawStartingHand() {
        for (int i = 0; i < 6; i++) {
            hand.add(table.getItemDeck().draw());
        }
    }

    public void draw() {
        hand.add(table.getItemDeck().draw());
    }

    public void secretAction(ItemCard card) {
        secretCard = card;
        hand.remove(card);
        possibleActions.remove(Action.SECRET);
    }

    public void tradeoffAction(List<ItemCard> cards) { //two cards
        for (ItemCard card: cards) {
            tradeoffCards.add(card);
            hand.remove(card);
        }
        possibleActions.remove(Action.TRADEOFF);
    }

    public void giftAction(List<ItemCard> myCards, Player opponent, ItemCard opponentsCard) {
        for (ItemCard myCard: myCards) {
            table.addToGeisha(this, myCard);
            hand.remove(myCard);
        }

        table.addToGeisha(opponent, opponentsCard);
        hand.remove(opponentsCard);
        possibleActions.remove(Action.GIFT);
    }

    public void competitionAction(List<ItemCard> myCards, Player opponent, List<ItemCard> opponentsCards) {
        for (ItemCard myCard: myCards) {
            table.addToGeisha(this, myCard);
            hand.remove(myCard);
        }

        for (ItemCard opponentsCard: opponentsCards) {
            table.addToGeisha(opponent, opponentsCard);
            hand.remove(opponentsCard);
        }
        possibleActions.remove(Action.COMPETITION);
    }

    public List<ItemCard> getUnknownCards() {
        ItemDeck deck = new ItemDeck();

        for (ItemCard c: hand) {
            deck.getItemDeck().remove(c);
        }
        deck.getItemDeck().remove(secretCard);
        for (ItemCard c: tradeoffCards) {
            deck.getItemDeck().remove(c);
        }

        for (GeishaOnTable geisha: table.getGeishas().getGeishaOnTableList()) {
            for (int i=0; i< geisha.getPlayerAscore() + geisha.getPlayerBscore(); i++) {
                deck.getItemDeck().remove(new ItemCard(geisha.getGeisha()));
            }
        }

        return deck.getItemDeck();
    }


    public void opponentSecretAction(ItemCard card) {
        secretCard = card;
        //possibleActions.remove(Action.SECRET);
    }

    public void opponentTradeoffAction(List<ItemCard> cards) { //two cards
        for (ItemCard card: cards) {
            tradeoffCards.add(card);
            //possibleActions.remove(Action.TRADEOFF);
        }
    }

    public void opponentGiftAction(List<ItemCard> myCards, Player opponent, ItemCard opponentsCard) {
        for (ItemCard myCard: myCards) {
            table.addToGeisha(this, myCard);
        }

        table.addToGeisha(opponent, opponentsCard);
        //possibleActions.remove(Action.GIFT);
    }

    public void opponentCompetitionAction(List<ItemCard> myCards, Player opponent, List<ItemCard> opponentsCards) {
        for (ItemCard myCard: myCards) {
            table.addToGeisha(this, myCard);
        }

        for (ItemCard opponentsCard: opponentsCards) {
            table.addToGeisha(opponent, opponentsCard);
        }
        //possibleActions.remove(Action.COMPETITION);
    }

}
