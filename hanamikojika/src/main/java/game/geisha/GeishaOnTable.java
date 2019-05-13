package game.geisha;

import game.board.Player;

import java.io.Serializable;
import java.util.Objects;

public class GeishaOnTable implements Serializable {

    private Geisha geisha;

    private Player playerA;

    private Player playerB;

    private int playerAscore;

    private int playerBscore;

    private Player currentWinner;


    public GeishaOnTable(Geisha geisha, Player playerA, Player playerB) {
        this.geisha = geisha;
        this.playerAscore = 0;
        this.playerBscore = 0;
        this.playerA = playerA;
        this.playerB = playerB;
        this.currentWinner = null;
    }

    public Geisha getGeisha() {
        return geisha;
    }

    public void setGeisha(Geisha geisha) {
        this.geisha = geisha;
    }

    public int getPlayerAscore() {
        return playerAscore;
    }

    public void setPlayerAscore(int playerAscore) {
        this.playerAscore = playerAscore;
    }

    public int getPlayerBscore() {
        return playerBscore;
    }

    public void setPlayerBscore(int playerBscore) {
        this.playerBscore = playerBscore;
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

    public Player getCurrentWinner() {
        return currentWinner;
    }

    public void setCurrentWinner(Player currentWinner) {
        this.currentWinner = currentWinner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeishaOnTable that = (GeishaOnTable) o;
        return playerAscore == that.playerAscore &&
                playerBscore == that.playerBscore &&
                Objects.equals(geisha, that.geisha) &&
                Objects.equals(playerA, that.playerA) &&
                Objects.equals(playerB, that.playerB) &&
                Objects.equals(currentWinner, that.currentWinner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(geisha, playerA, playerB, playerAscore, playerBscore, currentWinner);
    }

    @Override
    public String toString() {
        return "GeishaOnTable{" +
                "geisha=" + geisha +
                ", playerA=" + playerA +
                ", playerB=" + playerB +
                ", playerAscore=" + playerAscore +
                ", playerBscore=" + playerBscore +
                ", currentWinner=" + currentWinner +
                '}';
    }

    public Player getWinningPlayer() {
        if (getPlayerAscore() > getPlayerBscore())
            return playerA;
        else if (getPlayerBscore() > getPlayerAscore())
            return playerB;
        else
            return currentWinner;
    }

    public void addPlayerScore(Player player) {
        if (player.equals(playerA))
            playerAscore++;
        else
            playerBscore++;
    }

    public int playerScore(Player player) {
        if (player.equals(playerA))
            return getPlayerAscore();
        else
            return getPlayerBscore();
    }

}
