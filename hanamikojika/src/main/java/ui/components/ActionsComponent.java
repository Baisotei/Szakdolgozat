package ui.components;

import game.action.Action;
import game.board.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;
import java.util.List;

public class ActionsComponent extends GridPane {
    final private Player player;
    final private List<Button> buttons = new LinkedList<>();

    public ActionsComponent(Player player) {
        super();
        this.player = player;

        this.setAlignment(Pos.CENTER);
        this.add(initButton(Action.SECRET), 0, 0);
        this.add(initButton(Action.TRADEOFF), 1, 0);
        this.add(initButton(Action.GIFT), 2, 0);
        this.add(initButton(Action.COMPETITION), 3, 0);
        this.disableButtons(true);
    }

    public void disableButtons(boolean value) {
        for (Button button : buttons) {
            button.setDisable(value);
        }
    }

    private Button initButton(Action action) {
        Button button = new Button(this.getImage(action));
        button.setMinSize(40, 40);
        button.setMaxSize(40, 40);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                int index = player.getPossibleActions().indexOf(action);
                System.out.println(index);
                button.setDisable(true);
            }
        });
        buttons.add(button);
        return button;
    }

    private String getImage(Action action) {
        switch (action) {
            case SECRET: return "SECRET";
            case TRADEOFF: return "TRADEOFF";
            case GIFT: return "GIFT";
            case COMPETITION: return "COMPETITION";
            default: return "WTF";
        }
    }
}
