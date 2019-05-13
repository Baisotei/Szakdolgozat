package game.item;

import game.geisha.Geisha;
import game.geisha.GeishaDeck;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemDeck implements Serializable {

    private List<ItemCard> itemDeck;

    public ItemDeck() {
        this(new GeishaDeck());
    }

    public ItemDeck(GeishaDeck geishaDeck) {
        this.itemDeck = new ArrayList<>();
        for (Geisha geisha: geishaDeck.getGeishaDeck()) {
            for (int i = 0; i < geisha.getCharmPoints(); i++) {
                itemDeck.add(new ItemCard(geisha));
            }
        }
   }

    public List<ItemCard> getItemDeck() {
        return itemDeck;
    }


    public void shuffleDeck() {
        Collections.shuffle(itemDeck);
    }


    public ItemCard draw() {
        ItemCard card = itemDeck.get(0);
        itemDeck.remove(0);
        return card;
    }

}
