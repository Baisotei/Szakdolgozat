package game.board;

import game.geisha.GeishaOnTable;

import java.io.Serializable;
import java.util.Objects;

public class BoardState implements Serializable {

    private Player playerA;

    private Player playerB;

    private Player nextPlayer; //either playerA or playerB, playerA starts

    private Table table;

    public BoardState() {

        this.playerA = new Player(0);
        this.playerB = new Player(1);

        this.table = new Table(this.playerA, this.playerB);

        playerA.setTable(this.table);
        playerB.setTable(this.table);

        this.nextPlayer = playerA;

        playerA.drawStartingHand();
        playerB.drawStartingHand();
    }

    public Player getPlayerA() {
        return playerA;
    }

    public void setPlayerA(Player playerA) {
        this.playerA = playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public void setPlayerB(Player playerB) {
        this.playerB = playerB;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardState that = (BoardState) o;
        return Objects.equals(getPlayerA(), that.getPlayerA()) &&
                Objects.equals(getPlayerB(), that.getPlayerB()) &&
                Objects.equals(getTable(), that.getTable());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getPlayerA(), getPlayerB(), getTable());
    }

    @Override
    public String toString() {
        return "BoardState{" +
                "playerA=" + playerA +
                ", playerB=" + playerB +
                ", table=" + table +
                '}';
    }


    public void swapNextPlayer() {
        if (nextPlayer.equals(playerA))
            nextPlayer = playerB;
        else
            nextPlayer = playerA;
    }

    public void addPlayerSecretCard(Player player) {
        if (whichPlayer(player).getSecretCard() != null) {
            GeishaOnTable geishaA = table.getGeishas().getMatchingGeisha(whichPlayer(player).getSecretCard());
            geishaA.addPlayerScore(whichPlayer(player));
        }
    }

    public void addSecretCards() {
        addPlayerSecretCard(playerA);
        addPlayerSecretCard(playerB);
    }


    public int heuristic(Player player) {

        int score;

        if (player.equals(playerA)) {
            score = table.score(playerA) - table.score(playerB);
            if (table.score(playerB)>=11)
                score -= 100;
            else if (table.numberOfWonGeishas(playerB) >= 4)
                score -= 50;
            if (table.score(playerA)>=11)
                score += 100;
            else if (table.numberOfWonGeishas(playerA) >= 4)
                score += 50;
        } else {
            score = table.score(playerB) - table.score(playerA);
            if (table.score(playerA)>=11)
                score -= 100;
            else if (table.numberOfWonGeishas(playerA) >= 4)
                score -= 50;
            if (table.score(playerB)>=11)
                score += 100;
            else if (table.numberOfWonGeishas(playerB) >= 4)
                score += 50;
        }

        return score;
    }

    public void setWinningMarkers() {
        for (GeishaOnTable geisha: table.getGeishas().getGeishaOnTableList()) {
            geisha.setCurrentWinner(geisha.getWinningPlayer());
        }
    }

    public boolean isOver() {
        return playerA.getPossibleActions().isEmpty() && playerB.getPossibleActions().isEmpty();
    }

    public Player whichPlayer(Player player) {
        if (player.equals(playerA))
            return playerA;
        else
            return playerB;
    }


}
