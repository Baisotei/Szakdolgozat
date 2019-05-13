package game.item;

import game.geisha.Geisha;

import java.io.Serializable;
import java.util.Objects;

public class ItemCard implements Serializable {

    private Geisha owner;

    public ItemCard(Geisha owner) {
        this.owner = owner;
    }

    public int getCharmPoints() {
        return owner.getCharmPoints();
    }

    public int getId() {
        return owner.getId();
    }

    public Geisha getOwner() {
        return owner;
    }

    public void setOwner(Geisha owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemCard itemCard = (ItemCard) o;
        return getId() == itemCard.getId();
    }

    @Override
    public int hashCode() {

        return Objects.hash(owner);
    }

    @Override
    public String toString() {
        return "ItemCard{" +
                "owner=" + owner +
                '}';
    }
}
