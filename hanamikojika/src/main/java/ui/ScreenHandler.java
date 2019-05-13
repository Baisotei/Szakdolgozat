package ui;

import game.action.Action;
import game.board.BoardState;
import game.board.Player;
import game.geisha.GeishaOnTable;
import game.item.ItemCard;
import javafx.scene.Scene;
import javafx.stage.Stage;
import nisei.Minimax;
import nisei.Transition;
import ui.components.ActionButton;
import ui.screens.GameScreen;
import ui.screens.MainScreen;

public class ScreenHandler {
    final private static int height = 600;
    final private static int width = 800;
    final private static Minimax ai = new Minimax();

    private static BoardState boardState;
    private static Player CPU;
    private static Player player;
    private static Stage stage;
    private static GameScreen table;
    private static boolean playerStart;

    private ScreenHandler() {}

    public static void selectCard(ActionButton actionButton, String text, int count) {
        table.selectCard(actionButton, text, count);
    }

    private static void printHuman(Player opponent) {
        System.out.println("A kezed:");
        System.out.print("[ ");
        for (ItemCard card : opponent.getHand()) {
            System.out.print(card.getOwner().getId() + "(" + card.getCharmPoints() + "), ");
        }
        System.out.println(" ]");

        if (opponent.getSecretCard() != null) {
            System.out.println("Titkos kártya: "+ opponent.getSecretCard().getOwner().getId()  + "(" + opponent.getSecretCard().getCharmPoints() + ")");
        }

        if (!opponent.getPossibleActions().contains(Action.TRADEOFF)) {
            System.out.print(" Tradeoff kártyák: [ ");
            for (ItemCard card : opponent.getTradeoffCards()) {
                System.out.print(card.getOwner().getId() + "(" + card.getCharmPoints() + "), ");
            }
            System.out.println(" ]");
        }
    }

    private static void printBoardState(BoardState boardState, Player me, Player opponent) {
        System.out.println("Asztal: ");
        for (GeishaOnTable geisha: boardState.getTable().getGeishas().getGeishaOnTableList()) {
            System.out.print("Gésa "+ geisha.getGeisha().getId()+": pontszámod: "+geisha.playerScore(opponent)+ ", a gépe: "+geisha.playerScore(me));
            System.out.println();
        }
        System.out.println("Player keze");
        printHuman(player);
    }

    public static void startRound() {
        table.updateScreen();
        if (!ScreenHandler.boardState.isOver()) {
            if (ScreenHandler.boardState.getNextPlayer().equals(player)) {
                player.draw();

                printBoardState(ScreenHandler.boardState, CPU, player);
                System.out.println("Mit lépsz?");
                table.selectAction();
            } else {
                CPU.draw();
                printBoardState(ScreenHandler.boardState, CPU, player);
                System.out.println("A gép gondolkodik.");
                Transition transition = ai.miniMaxTransition(ScreenHandler.boardState, CPU, player);

                System.out.println("A gép lépése: " + transition.getAction());

                if (transition.getAction() == Action.SECRET || transition.getAction() == Action.TRADEOFF) {
                    ScreenHandler.boardState = transition.applyTransition(ScreenHandler.boardState, CPU, player);
                    endRound(true);
                } else if (transition.getAction() == Action.GIFT) {
                    System.out.println("A gép ajándékozni akar. Ezek a lehetőségek: (1,2,3)");
                    for (ItemCard c : transition.getCards()) {
                        System.out.print(c.getId() + "(" + c.getCharmPoints() + "), ");
                    }
                    table.CPUGift(transition);
                } else {
                    System.out.println("A gép versengeni akar. Ezek a lehetőségek: (1,2)");
                    table.CPUCompetition(transition);
                }

                ScreenHandler.CPU = ScreenHandler.boardState.whichPlayer(ScreenHandler.CPU);
                ScreenHandler.player = ScreenHandler.boardState.whichPlayer(ScreenHandler.player);
            }
        } else {
            System.out.println("VÉGE");
            boardState.addSecretCards();

            int myCharm = boardState.getTable().charmPoints(CPU);
            int opponentsCharm = boardState.getTable().charmPoints(player);

            int myGeishas = boardState.getTable().numberOfWonGeishas(CPU);
            int opponentsGeishas = boardState.getTable().numberOfWonGeishas(player);

            System.out.println("Az asztal végállapota:");
            printBoardState(boardState, CPU, player);
            table.updateScreen();

            if (myCharm >= 11) {
                System.out.println("Győzött a gép! Pontszáma: "+ myCharm + ", a tiéd: "+ opponentsCharm);
                table.setActionText("Győzött a gép! Pontszáma: "+ myCharm + ", a tiéd: "+ opponentsCharm);
            } else if (opponentsCharm >= 11) {
                System.out.println("Győztél! Pontszámod: "+ opponentsCharm + ", a gépé: "+ myCharm);
                table.setActionText("Győztél! Pontszámod: "+ opponentsCharm + ", a gépé: "+ myCharm);
            } else if (myGeishas >= 4) {
                System.out.println("Győzött a gép! Gésák száma: "+ myGeishas + ", a tiéd: "+ opponentsGeishas);
                table.setActionText("Győzött a gép! Gésák száma: "+ myGeishas + ", a tiéd: "+ opponentsGeishas);
            } else if (opponentsGeishas >= 4) {
                System.out.println("Győztél! Gésáid száma: "+ opponentsGeishas + ", a gépé: "+ myGeishas);
                table.setActionText("Győztél! Gésáid száma: "+ opponentsGeishas + ", a gépé: "+ myGeishas);
            } else {
                System.out.println("Döntetlen!");
                System.out.println("Pontszámod: "+ opponentsCharm+", a gép pontszáma: "+myCharm);
                System.out.println("Gésáid száma: "+opponentsGeishas+", a gép gésáinak száma: "+myGeishas);
                table.setActionText("Döntetlen!");
            }
        }
    }

    public static void endRound(boolean updatePlayers) {
        System.out.println("--------------");
        System.out.println();
        if (updatePlayers) {
            ScreenHandler.CPU = ScreenHandler.boardState.whichPlayer(ScreenHandler.CPU);
            ScreenHandler.player = ScreenHandler.boardState.whichPlayer(ScreenHandler.player);
        }
        ScreenHandler.boardState.swapNextPlayer();
        startRound();
    }

    public static void newGame(boolean playerStart) {
        ScreenHandler.playerStart = playerStart;
        ScreenHandler.boardState = new BoardState();

        if (playerStart) {
            CPU = ScreenHandler.boardState.getPlayerB();
            player = ScreenHandler.boardState.getPlayerA();
        } else {
            CPU = ScreenHandler.boardState.getPlayerA();
            player = ScreenHandler.boardState.getPlayerB();
        }

        table = new GameScreen();
        stage.setScene(new Scene(table, width, height));
        startRound();
    }

    public static void newStart() {
        stage.setScene(new Scene(new MainScreen(), width, height));
    }

    public static void setStage(Stage stage) {
        stage.setTitle("Hanamikoji");
        stage.show();
        ScreenHandler.stage = stage;
        newStart();
    }

    public static boolean playerStarted() {
        return ScreenHandler.playerStart;
    }

    public static Minimax getAi() {
        return ai;
    }

    public static BoardState getBoardState() {
        return ScreenHandler.boardState;
    }

    public static Player getCPU() {
        return CPU;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setBoardState(BoardState boardState) {
        ScreenHandler.boardState = boardState;
    }
}
