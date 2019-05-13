package ui.components;

import game.geisha.GeishaOnTable;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class GeishaMainCard extends GridPane {
    final private GeishaOnTable geisha;

    public GeishaMainCard(GeishaOnTable geisha) {
        super();
        this.geisha = geisha;

        Text upper = new Text("");
        this.add(upper, 0, 0);
        Text center = new Text("Geisha (" + this.geisha.getGeisha().getCharmPoints() + ")");
        this.add(center, 0, 1);
        Text lower = new Text("");
        this.add(lower, 0, 2);
    }
}
