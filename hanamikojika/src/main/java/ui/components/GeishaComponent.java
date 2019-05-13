package ui.components;

import game.geisha.GeishaOnTable;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import ui.ScreenHandler;

public class GeishaComponent extends GridPane {
    public GeishaComponent(GeishaOnTable geisha) {
        Text aiScore = new Text("      " + (ScreenHandler.playerStarted() ? geisha.getPlayerBscore() : geisha.getPlayerAscore()));
        Text playerScore = new Text("      " + (ScreenHandler.playerStarted() ? geisha.getPlayerAscore() : geisha.getPlayerBscore()));

        this.setAlignment(Pos.CENTER);
        this.add(aiScore, 0, 0);
        this.add(new GeishaMainCard(geisha), 0, 1);
        this.add(playerScore, 0, 2);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), new BorderWidths(1))));
    }
}
