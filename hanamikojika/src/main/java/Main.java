import game.board.BoardState;
import javafx.application.Application;
import javafx.stage.Stage;
import ui.ScreenHandler;

public class Main extends Application {
    public static void main(String[] args) {

        BoardState boardState = new BoardState();

        System.out.println(boardState);

        launch(args);
    }

    @Override
    public void start(Stage stage) {
        ScreenHandler.setStage(stage);
    }
}
