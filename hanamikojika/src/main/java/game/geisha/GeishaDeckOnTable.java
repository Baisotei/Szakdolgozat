package game.geisha;

import game.board.Player;
import game.item.ItemCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GeishaDeckOnTable implements Serializable {

    private GeishaDeck geishaDeck;

    private List<GeishaOnTable> geishaOnTableList;

    public GeishaDeckOnTable(GeishaDeck geishaDeck, Player playerA, Player playerB) {
        this.geishaDeck = geishaDeck;
        this.geishaOnTableList = new ArrayList<>();
        for (Geisha geisha: geishaDeck.getGeishaDeck()) {
            geishaOnTableList.add(new GeishaOnTable(geisha, playerA, playerB));
        }
    }

    public GeishaDeck getGeishaDeck() {
        return geishaDeck;
    }

    public void setGeishaDeck(GeishaDeck geishaDeck) {
        this.geishaDeck = geishaDeck;
    }

    public List<GeishaOnTable> getGeishaOnTableList() {
        return geishaOnTableList;
    }

    public void setGeishaOnTableList(List<GeishaOnTable> geishaOnTableList) {
        this.geishaOnTableList = geishaOnTableList;
    }

    public GeishaOnTable getGeishaOnTable(Geisha geisha) {
        return geishaOnTableList.get(geisha.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GeishaDeckOnTable that = (GeishaDeckOnTable) o;
        return Objects.equals(getGeishaDeck(), that.getGeishaDeck()) &&
                Objects.equals(getGeishaOnTableList(), that.getGeishaOnTableList());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getGeishaDeck(), getGeishaOnTableList());
    }

    @Override
    public String toString() {
        return "GeishaDeckOnTable{" +
                "geishaDeck=" + geishaDeck +
                ", geishaOnTableList=" + geishaOnTableList +
                '}';
    }

    public GeishaOnTable getMatchingGeisha(ItemCard itemCard) {
        for (GeishaOnTable geisha: geishaOnTableList) {
            if (geisha.getGeisha().equals(itemCard.getOwner()))
                    return geisha;
        }
        return null;
    }

}
