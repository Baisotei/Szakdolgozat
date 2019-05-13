package nisei;

import game.action.Action;
import game.board.BoardState;
import game.board.Player;
import game.geisha.GeishaOnTable;
import game.item.ItemCard;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Transition {

    private Action action;

    private List<ItemCard> cards;

    private List<Integer> opponentChoices;

    public Transition() {
        this.action = null;
        this.cards = new ArrayList<>();
        this.opponentChoices = new ArrayList<>();
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public List<ItemCard> getCards() {
        return cards;
    }

    public void setCards(List<ItemCard> cards) {
        this.cards = cards;
    }

    public List<Integer> getOpponentChoices() {
        return opponentChoices;
    }

    public void setOpponentChoices(List<Integer> opponentChoices) {
        this.opponentChoices = opponentChoices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transition that = (Transition) o;
        return getAction() == that.getAction() &&
                Objects.equals(getCards(), that.getCards());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getAction(), getCards());
    }

    @Override
    public String toString() {
        return "Transition{" +
                "action=" + action +
                ", cards=" + cards +
                '}';
    }


    public static Transition getTransition(BoardState a, BoardState b, Player me) {

        Transition transition = new Transition();

        for (Action action: a.whichPlayer(me).getPossibleActions()) {
            if (!b.whichPlayer(me).getPossibleActions().contains(action)) {
                transition.setAction(action);
            }
        }

        if (transition.getAction() != Action.COMPETITION) {

            List<ItemCard> l = new ArrayList<>();
            l.addAll(a.whichPlayer(me).getHand());
            for (ItemCard c : b.whichPlayer(me).getHand()) {
                l.remove(c);
            }
            transition.getCards().addAll(l);
        } else {

            Player opponent;
            if (a.getPlayerA().equals(me))
                opponent = a.getPlayerB();
            else
                opponent = a.getPlayerA();

            for (GeishaOnTable geisha: b.getTable().getGeishas().getGeishaOnTableList()) {
                GeishaOnTable oldGeisha = a.getTable().getGeishas().getGeishaOnTable(geisha.getGeisha());

                for (int i=0; i< geisha.playerScore(b.whichPlayer(me)) - oldGeisha.playerScore(a.whichPlayer(me)); i++) {
                    transition.getCards().add(new ItemCard(geisha.getGeisha()));
                }

                for (int i=0; i< geisha.playerScore(b.whichPlayer(opponent)) - oldGeisha.playerScore(a.whichPlayer(opponent)); i++) {
                    transition.getCards().add(new ItemCard(geisha.getGeisha()));
                }

            }

            if (me.getSecretCard() != null)
                transition.getCards().remove(me.getSecretCard());

        }

        return transition;
    }

    public BoardState applyTransition(BoardState boardState, Player me, Player opponent) {

        BoardState newState = (BoardState) SerializationUtils.clone(boardState);

        if (action == Action.SECRET) {
            newState.whichPlayer(me).secretAction(getCards().get(0));
        } else if (action == Action.TRADEOFF) {
            List<ItemCard> l = Arrays.asList(cards.get(0), cards.get(1));
            newState.whichPlayer(me).tradeoffAction(l);
        } else if (action == Action.GIFT) {
            ItemCard opponentsPick = cards.get(opponentChoices.get(0));
            cards.remove(opponentsPick);
            newState.whichPlayer(me).giftAction(cards, opponent, opponentsPick);
        } else {
            List<ItemCard> opponentsPicks = new ArrayList<>();
            List<ItemCard> myPicks = new ArrayList<>();

            if (opponentChoices.get(0)==1) {
                opponentsPicks.add(cards.get(0));
                opponentsPicks.add(cards.get(1));
                myPicks.add(cards.get(2));
                myPicks.add(cards.get(3));
            } else {
                opponentsPicks.add(cards.get(2));
                opponentsPicks.add(cards.get(3));
                myPicks.add(cards.get(0));
                myPicks.add(cards.get(1));
            }

            newState.whichPlayer(me).competitionAction(myPicks, opponent, opponentsPicks);
        }

        return newState;
    }

}
