package game.board;

import game.geisha.Geisha;
import game.geisha.GeishaDeck;
import game.geisha.GeishaDeckOnTable;
import game.geisha.GeishaOnTable;
import game.item.ItemCard;
import game.item.ItemDeck;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Table implements Serializable {

    private ItemDeck itemDeck;

    private GeishaDeckOnTable geishas;

    public Table(Player playerA, Player playerB) {
        this.itemDeck = new ItemDeck();
        this.itemDeck.shuffleDeck();

        this.geishas = new GeishaDeckOnTable(new GeishaDeck(), playerA, playerB);
    }

    public ItemDeck getItemDeck() {
        return itemDeck;
    }

    public void setItemDeck(ItemDeck itemDeck) {
        this.itemDeck = itemDeck;
    }

    public GeishaDeckOnTable getGeishas() {
        return geishas;
    }

    public void setGeishas(GeishaDeckOnTable geishas) {
        this.geishas = geishas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return Objects.equals(getItemDeck(), table.getItemDeck()) &&
                Objects.equals(getGeishas(), table.getGeishas());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getItemDeck(), getGeishas());
    }

    @Override
    public String toString() {
        return "Table{" +
                "itemDeck=" + itemDeck +
                ", geishas=" + geishas +
                '}';
    }


    public void addToGeisha(Player player, ItemCard card) {
        GeishaOnTable geisha = geishas.getMatchingGeisha(card);
        geisha.addPlayerScore(player);
    }

    private List<GeishaOnTable> getWonGeishas(Player player) {
        return geishas.getGeishaOnTableList().stream()
                .filter(e -> e.getWinningPlayer() != null && e.getWinningPlayer().equals(player))
                .collect(Collectors.toList());
    }

    public int weightedCharmPoints(Player player) {
        return getWonGeishas(player).stream()
                .mapToInt(e -> {
                    if (e.playerScore(player) >= e.getGeisha().getCharmPoints()/2.0)
                        return e.getGeisha().getCharmPoints() * 2;
                    else
                        return e.getGeisha().getCharmPoints();
                })
                .sum();
    }

    public int charmPoints(Player player) {
        return getWonGeishas(player).stream()
                .mapToInt(e -> e.getGeisha().getCharmPoints())
                .sum();
    }

    public int numberOfWonGeishas(Player player) {
        return getWonGeishas(player).size();
    }

    public int score(Player player) {
        return weightedCharmPoints(player) + numberOfWonGeishas(player);
    }





}
