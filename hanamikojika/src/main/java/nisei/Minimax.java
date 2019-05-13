package nisei;

import game.action.Action;
import game.board.BoardState;
import game.board.Player;
import game.geisha.GeishaOnTable;
import game.item.ItemCard;
import org.apache.commons.lang3.SerializationUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Minimax {

    private Set<List<ItemCard>> getAllPossibleCombinations(List<ItemCard> cards, int number) {

        Set<List<ItemCard>> list = new HashSet<>();

        if (number == 1) {

            for (ItemCard card: cards) {
                list.add(Arrays.asList(card));
            }

        } else if (number == 2) {

            for (int i=0; i<cards.size(); i++) {
                for (int j = i + 1; j < cards.size(); j++) {
                    list.add(Arrays.asList(cards.get(i), cards.get(j)).stream().sorted(Comparator.comparingInt(ItemCard::getId)).collect(Collectors.toList()));
                }
            }

        } else if (number == 3) {
            for (int i=0; i<cards.size(); i++) {
                for (int j = i + 1; j < cards.size(); j++) {
                    for (int k = j + 1; k < cards.size(); k++)
                        list.add(Arrays.asList(cards.get(i), cards.get(j), cards.get(k)).stream().sorted(Comparator.comparingInt(ItemCard::getId)).collect(Collectors.toList()));
                }
            }
        } else {

            for (int i=0; i<cards.size(); i++) {
                for (int j = i + 1; j < cards.size(); j++) {
                    for (int k = j + 1; k < cards.size(); k++) {
                        for (int l = k + 1; l < cards.size(); l++)
                            list.add(Arrays.asList(cards.get(i), cards.get(j), cards.get(k), cards.get(l)).stream().sorted(Comparator.comparingInt(ItemCard::getId)).collect(Collectors.toList()));
                    }
                }
            }

        }

        return list;
    }




    private List<BoardState> myPossibleSecrets(BoardState boardState, Player me) {
        List<BoardState> children = new ArrayList<>();

        for (ItemCard card : me.getHand()) {
            BoardState newState = (BoardState) SerializationUtils.clone(boardState);
            Player player = newState.whichPlayer(me);
            player.secretAction(card);
            newState.swapNextPlayer();
            children.add(newState);
        }

        return children;

    }

    private List<BoardState> opponentsPossibleSecrets(BoardState boardState, Player me, Player opponent) {
        List<BoardState> children = new ArrayList<>();
        List<ItemCard> unknownCards = me.getUnknownCards();

        for (ItemCard card: unknownCards) {
            BoardState newState = (BoardState) SerializationUtils.clone(boardState);
            Player player = newState.whichPlayer(opponent);
            player.opponentSecretAction(card);
            newState.swapNextPlayer();
            children.add(newState);
        }

        return children;
    }

    private List<BoardState> myPossibleTradeoffs(BoardState boardState, Player me) {
        List<BoardState> children = new ArrayList<>();

        for (List<ItemCard> tradeoffs: getAllPossibleCombinations(me.getHand(), 2)) {
                BoardState newState = (BoardState) SerializationUtils.clone(boardState);
                Player player = newState.whichPlayer(me);
                player.tradeoffAction(tradeoffs);
                newState.swapNextPlayer();
                children.add(newState);
        }

        return children;
    }

    private List<BoardState> opponentsPossibleTradeoffs(BoardState boardState, Player me, Player opponent) {
        List<BoardState> children = new ArrayList<>();
        List<ItemCard> unknownCards = me.getUnknownCards();

        for (List<ItemCard> tradeoffs: getAllPossibleCombinations(unknownCards, 2)) {
                BoardState newState = (BoardState) SerializationUtils.clone(boardState);
                Player player = newState.whichPlayer(opponent);
                player.opponentTradeoffAction(tradeoffs);
                newState.swapNextPlayer();
                children.add(newState);
        }

        return children;
    }

    private List<BoardState> myPossibleGifts(BoardState boardState, Player me, Player opponent) {
        List<BoardState> children = new ArrayList<>();

        for (List<ItemCard> gifts: getAllPossibleCombinations(me.getHand(), 3)) {
            for (List<ItemCard> opponentsPick: getAllPossibleCombinations(gifts, 1)) {
                BoardState newState = (BoardState) SerializationUtils.clone(boardState);
                Player playerMe = newState.whichPlayer(me);
                Player playerOpponent = newState.whichPlayer(opponent);
                List<ItemCard> myCards = new ArrayList<>();
                myCards.addAll(gifts);
                ItemCard pick = opponentsPick.get(0);
                myCards.remove(pick);
                playerMe.giftAction(myCards, playerOpponent, pick);
                newState.swapNextPlayer();
                children.add(newState);
            }
        }

        return children;
    }

    private List<BoardState> opponentsPossibleGifts(BoardState boardState, Player me, Player opponent) {
        List<BoardState> children = new ArrayList<>();
        List<ItemCard> unknownCards = me.getUnknownCards();

        for (List<ItemCard> gifts: getAllPossibleCombinations(unknownCards, 3)) {
            for (List<ItemCard> opponentsPick: getAllPossibleCombinations(gifts, 1)) {
                BoardState newState = (BoardState) SerializationUtils.clone(boardState);
                Player playerMe = newState.whichPlayer(me);
                Player playerOpponent = newState.whichPlayer(opponent);
                List<ItemCard> myCards = new ArrayList<>();
                myCards.addAll(gifts);
                ItemCard pick = opponentsPick.get(0);
                myCards.remove(pick);
                playerOpponent.opponentGiftAction(myCards, playerMe, pick);
                newState.swapNextPlayer();
                children.add(newState);
            }
        }

        return children;
    }


    private List<BoardState> myPossibleCompetitions(BoardState boardState, Player me, Player opponent) {
        List<BoardState> children = new ArrayList<>();

        for (List<ItemCard> competitions: getAllPossibleCombinations(me.getHand(), 4)) {
            for (List<ItemCard> opponentsPick: getAllPossibleCombinations(competitions, 2)) {
                BoardState newState = (BoardState) SerializationUtils.clone(boardState);
                Player playerMe = newState.whichPlayer(me);
                Player playerOpponent = newState.whichPlayer(opponent);
                List<ItemCard> myPick = new ArrayList<>();
                myPick.addAll(competitions);
                for (ItemCard op: opponentsPick) {
                    myPick.remove(op);
                }
                playerMe.competitionAction(myPick, playerOpponent,opponentsPick);
                newState.swapNextPlayer();
                children.add(newState);
            }
        }

        return children;
    }

    private List<BoardState> opponentsPossibleCompetitions(BoardState boardState, Player me, Player opponent) {
        List<BoardState> children = new ArrayList<>();
        List<ItemCard> unknownCards = me.getUnknownCards();

        for (List<ItemCard> competitions: getAllPossibleCombinations(unknownCards, 4)) {
            for (List<ItemCard> myPick: getAllPossibleCombinations(competitions, 2)) {
                BoardState newState = (BoardState) SerializationUtils.clone(boardState);
                Player playerMe = newState.whichPlayer(me);
                Player playerOpponent = newState.whichPlayer(opponent);
                List<ItemCard> opponentsPick = new ArrayList<>();
                opponentsPick.addAll(competitions);
                for (ItemCard mp: myPick) {
                    opponentsPick.remove(mp);
                }
                playerOpponent.opponentCompetitionAction(opponentsPick, playerMe, myPick);
                newState.swapNextPlayer();
                children.add(newState);
            }
        }

        return children;
    }

    public List<BoardState> getAllMyMoves(BoardState boardState, Player me, Player opponent) {

        List<BoardState> allMyMoves = new ArrayList<>();

        for (Action action: me.getPossibleActions()) {

            if (action == Action.SECRET) {
                allMyMoves.addAll(myPossibleSecrets(boardState, me));
            } else if (action == Action.TRADEOFF) {
                allMyMoves.addAll(myPossibleTradeoffs(boardState, me));
            } else if (action == Action.GIFT) {
                allMyMoves.addAll(myPossibleGifts(boardState, me, opponent));
            } else if (action == Action.COMPETITION) {
                allMyMoves.addAll(myPossibleCompetitions(boardState, me, opponent));
            }
        }

        return allMyMoves;
    }

    public List<BoardState> getAllOpponentsMoves(BoardState boardState, Player me, Player opponent) {

        List<BoardState> allOppenentsMoves = new ArrayList<>();

        for (Action action: opponent.getPossibleActions()) {

            if (action == Action.SECRET) {
                allOppenentsMoves.addAll(opponentsPossibleSecrets(boardState, me, opponent));
            } else if (action == Action.TRADEOFF) {
                allOppenentsMoves.addAll(opponentsPossibleTradeoffs(boardState, me, opponent));
            } else if (action == Action.GIFT) {
                allOppenentsMoves.addAll(opponentsPossibleGifts(boardState, me, opponent));
            } else if (action == Action.COMPETITION) {
                allOppenentsMoves.addAll(opponentsPossibleCompetitions(boardState, me, opponent));
            }
        }

        return allOppenentsMoves;
    }

    public int minChildValue(BoardState boardState, Player me, Player opponent) {

        List<BoardState> moves = getAllOpponentsMoves(boardState, me, opponent);

        for (BoardState move: moves) {
            Player o = move.whichPlayer(opponent);
            if (o.getPossibleActions().contains(Action.SECRET))
                move.addPlayerSecretCard(o);
        }


        return moves.stream().mapToInt(e->e.heuristic(e.whichPlayer(me))).min().orElse(-1000);

    }


    public BoardState minMaxMove(BoardState boardState, Player me, Player opponent) {

        List<BoardState> moves = getAllMyMoves(boardState, me, opponent);

        if (boardState.whichPlayer(me).getPossibleActions().size() == 1 && boardState.whichPlayer(opponent).getPossibleActions().isEmpty()) {

            for (BoardState move: moves) {
                move.addSecretCards();
            }

            return moves.stream().max(Comparator.comparingInt(e->e.heuristic(me))).get();

        } else {

            for (BoardState move: moves) {
                move.addPlayerSecretCard(me);
            }

            return moves.stream().max(Comparator.comparingInt(e->minChildValue(e, me, opponent))).get();

        }

    }

    public Transition miniMaxTransition(BoardState boardState, Player me, Player opponent) {

        return Transition.getTransition(boardState, minMaxMove(boardState, me, opponent), me);

    }


    public ItemCard giftChoice(BoardState boardState, Player me, Player opponent, List<ItemCard> choices) {
        ItemCard c = choices.get(0);
        int maxHeuristic = -9999999;

        for (ItemCard choice: choices) {
            BoardState newState = (BoardState) SerializationUtils.clone(boardState);
            Player playerMe = newState.whichPlayer(me);
            Player playerOpponent = newState.whichPlayer(opponent);
            List<ItemCard> cards = new ArrayList<>(choices);
            cards.remove(choice);
            playerOpponent.opponentGiftAction(cards, playerMe, choice);
            if (newState.heuristic(playerMe) > maxHeuristic) {
                maxHeuristic = newState.heuristic(playerMe);
                c = choice;
            }
        }

        return c;

    }


    public int competitionChoice(BoardState boardState, Player me, Player opponent, List<ItemCard> choice1, List<ItemCard> choice2) {
            int h1, h2;

            BoardState newState1 = (BoardState) SerializationUtils.clone(boardState);
            Player playerMe = newState1.whichPlayer(me);
            Player playerOpponent = newState1.whichPlayer(opponent);

            playerOpponent.opponentCompetitionAction(choice2, playerMe, choice1);
            h1 = newState1.heuristic(me);

            BoardState newState2 = (BoardState) SerializationUtils.clone(boardState);
            Player playerMe2 = newState2.whichPlayer(me);
            Player playerOpponent2 = newState2.whichPlayer(opponent);

            playerOpponent.opponentCompetitionAction(choice1, playerMe, choice2);
            h2 = newState2.heuristic(me);

            if (h1>h2)
                return 1;
            else
                return 2;
    }

    private static void printBoardState(BoardState boardState, Player me, Player opponent) {
        System.out.println("Asztal: ");
        for (GeishaOnTable geisha: boardState.getTable().getGeishas().getGeishaOnTableList()) {
            System.out.print("Gésa "+ geisha.getGeisha().getId()+": pontszámod: "+geisha.playerScore(opponent)+ ", a gépe: "+geisha.playerScore(me));
            System.out.println();
        }

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

    private static void printActions(Player me, Player opponent) {
        System.out.println("Ellenfél lehetséges akciói: ");
        for (Action action: me.getPossibleActions()) {
            if (action == Action.SECRET) {
                System.out.print(" 1 - titok, ");
            } else if (action == Action.TRADEOFF) {
                System.out.print(" 2 - eldobás, ");
            } else if (action == Action.GIFT) {
                System.out.print(" 3 - ajándék, ");
            } else {
                System.out.print(" 4 - versengés, ");
            }
        }
        System.out.println();

        System.out.println("Lehetséges akciók: ");
        for (Action action: opponent.getPossibleActions()) {
            if (action == Action.SECRET) {
                System.out.print(" 1 - titok, ");
            } else if (action == Action.TRADEOFF) {
                System.out.print(" 2 - eldobás, ");
            } else if (action == Action.GIFT) {
                System.out.print(" 3 - ajándék, ");
            } else {
                System.out.print(" 4 - versengés, ");
            }
        }
    }

    public static void main(String[] args) {

        Scanner s = new Scanner(System.in);

        BoardState boardState = new BoardState();
        Player me, opponent;

        System.out.println("Melyik játékos akarsz lenni? 1 - első, 2 - második");

        if (s.nextLine().equals("1")) {
             me = boardState.getPlayerB();
             opponent = boardState.getPlayerA();
        } else {
             me = boardState.getPlayerA();
             opponent = boardState.getPlayerB();
        }

        Minimax ai = new Minimax();

        while (!boardState.isOver()) {


            System.out.println("---------------------------------------");

            if (boardState.getNextPlayer().equals(opponent)) {

                opponent.draw();

                printHuman(opponent);

                printBoardState(boardState, me, opponent);

                printActions(me, opponent);

                System.out.println("Mit lépsz? ");
                String m = s.nextLine();
                if (m.equals("1")) {
                    System.out.println("1 - Titok: válassz egy lapot a kezedből! (1-től sorszámozva)");
                    opponent.secretAction(opponent.getHand().get(Integer.parseInt(s.nextLine())-1));
                    System.out.println("Titok kész!");
                } else if (m.equals("2")) {
                    System.out.println("2 - Tradeoff: válassz két lapot a kezedből! (1-től sorszámozva)");
                    List<ItemCard> picks = new ArrayList<>();
                    picks.add(opponent.getHand().get(Integer.parseInt(s.nextLine())-1));
                    picks.add(opponent.getHand().get(Integer.parseInt(s.nextLine())-1));
                    opponent.tradeoffAction(picks);
                    System.out.println("Tradeoff kész!");
                } else if (m.equals("3")) {
                    System.out.println("3 - Ajándék: válassz három lapot a kezedből! (1-től sorszámozva)");
                    List<ItemCard> picks = new ArrayList<>();
                    picks.add(opponent.getHand().get(Integer.parseInt(s.nextLine())-1));
                    picks.add(opponent.getHand().get(Integer.parseInt(s.nextLine())-1));
                    picks.add(opponent.getHand().get(Integer.parseInt(s.nextLine())-1));
                    ItemCard myPick = ai.giftChoice(boardState, me, opponent, picks);
                    System.out.println("A gép ezt választotta: "+myPick.getId()+ "("+myPick.getCharmPoints()+")");
                    picks.remove(myPick);
                    opponent.giftAction(picks, me, myPick);
                    System.out.println("Ajándék kész!");
                } else if (m.equals("4")) {
                    System.out.println("4 - Versengés: válassz két-két lapot a kezedből! (1-től sorszámozva)");
                    List<ItemCard> picks1 = new ArrayList<>();
                    List<ItemCard> picks2 = new ArrayList<>();
                    System.out.println("Első kettő:");
                    picks1.add(opponent.getHand().get(Integer.parseInt(s.nextLine())-1));
                    picks1.add(opponent.getHand().get(Integer.parseInt(s.nextLine())-1));
                    System.out.println("Második kettő:");
                    picks2.add(opponent.getHand().get(Integer.parseInt(s.nextLine())-1));
                    picks2.add(opponent.getHand().get(Integer.parseInt(s.nextLine())-1));
                    int myPick = ai.competitionChoice(boardState, me, opponent, picks1, picks2);

                    System.out.println("A gép a "+myPick+". lehetőséget választotta.");

                    if (myPick == 1) {
                        opponent.competitionAction(picks2, me, picks1);
                    } else {
                        opponent.competitionAction(picks1, me, picks2);
                    }

                    System.out.println("Versengés kész!");
                }

                boardState.swapNextPlayer();

            } else {

                printBoardState(boardState, me, opponent);

                me.draw();
                Transition transition = ai.miniMaxTransition(boardState, me, opponent);

                System.out.println("A gép lépése: "+transition.getAction());

                if (transition.getAction() == Action.SECRET || transition.getAction() == Action.TRADEOFF) {
                    boardState = transition.applyTransition(boardState, me, opponent);
                } else if (transition.getAction() == Action.GIFT) {

                    System.out.println("A gép ajándékozni akar. Ezek a lehetőségek: (1,2,3)");
                    for (ItemCard c: transition.getCards()) {
                        System.out.print(c.getId()+"("+c.getCharmPoints()+"), ");
                    }
                    int humanPick = Integer.parseInt(s.nextLine()) - 1;
                    transition.getOpponentChoices().add(humanPick);
                    boardState = transition.applyTransition(boardState, me, opponent);

                } else {

                    System.out.println("A gép versengeni akar. Ezek a lehetőségek: (1,2)");
                    System.out.println("Első kettő:");
                    System.out.println("[ "+transition.getCards().get(0).getId()+"("+transition.getCards().get(0).getCharmPoints()+"), "+transition.getCards().get(1).getId()+"("+transition.getCards().get(1).getCharmPoints()+") ]");

                    System.out.println("Második kettő:");
                    System.out.println("[ "+transition.getCards().get(2).getId()+"("+transition.getCards().get(2).getCharmPoints()+"), "+transition.getCards().get(3).getId()+"("+transition.getCards().get(3).getCharmPoints()+") ]");

                    int humanpick = Integer.parseInt(s.nextLine());
                    transition.getOpponentChoices().add(humanpick);

                    boardState = transition.applyTransition(boardState, me, opponent);
                }

                me = boardState.whichPlayer(me);
                opponent = boardState.whichPlayer(opponent);

                boardState.swapNextPlayer();

            }


        }

        boardState.addSecretCards();

        int myCharm = boardState.getTable().charmPoints(me);
        int opponentsCharm = boardState.getTable().charmPoints(opponent);

        int myGeishas = boardState.getTable().numberOfWonGeishas(me);
        int opponentsGeishas = boardState.getTable().numberOfWonGeishas(opponent);

        System.out.println("Az asztal végállapota:");
        printBoardState(boardState, me, opponent);

        if (myCharm >= 11) {
            System.out.println("Győzött a gép! Pontszáma: "+ myCharm + ", a tiéd: "+ opponentsCharm);
        } else if (opponentsCharm >= 11) {
            System.out.println("Győztél! Pontszámod: "+ opponentsCharm + ", a gépé: "+ myCharm);
        } else if (myGeishas >= 4) {
            System.out.println("Győzött a gép! Gésák száma: "+ myGeishas + ", a tiéd: "+ opponentsGeishas);
        } else if (opponentsGeishas >= 4) {
            System.out.println("Győztél! Gésáid száma: "+ opponentsGeishas + ", a gépé: "+ myGeishas);
        } else {
            System.out.println("Döntetlen!");
            System.out.println("Pontszámod: "+ opponentsCharm+", a gép pontszáma: "+myCharm);
            System.out.println("Gésáid száma: "+opponentsGeishas+", a gép gésáinak száma: "+myGeishas);
        }

    }

}
