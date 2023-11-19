package Server;

import Modelle.Card;
import Modelle.Hand;
import Modelle.Player;
import Modelle.Role;
import Modelle.ServerClient;
import Modelle.UpdateGameState;
import Modelle.exception.NoPlayerFoundException;
import Util.ResourceLoader;
import gameUtils.Deck;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Philipp Schweizer
 * @version v1
 * @since 05.08.2017
 */
public class GameManager
{

    ////////////////////////////////////////////////////////////////////////////
    // Constances
    ////////////////////////////////////////////////////////////////////////////
    private final static String FILE_NAME = "GameManager";

    ////////////////////////////////////////////////////////////////////////////
    // Fields
    ////////////////////////////////////////////////////////////////////////////
    private final List<ServerClient> serverClientList;
    private Deck deck;
    private int currentPlayer;
    private int position = 1;
    private ServerClient lastServerClientWhoPlayedACard;
    ////////////////////////////////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Default-Constructor
     */
    public GameManager(List<ServerClient> serverClientList) throws IOException
    {
        this.serverClientList = new ArrayList<>(serverClientList);
        prepareDeck();
        spreadOutDeckToPlayers();
        startGame();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    private void startGame() throws IOException
    {
        final String value = "7";
        String symbol = "herz";
        if ((deck.cardDeck[30].getValue()
                .equals(value) && deck.cardDeck[30].getSymbol()
                .equals(symbol)) ||
                (deck.cardDeck[31].getValue()
                        .equals(value) && deck.cardDeck[31].getSymbol()
                .equals(symbol)))
        {
            symbol = "karo";
        }
        if ((deck.cardDeck[30].getValue()
                .equals(value) && deck.cardDeck[30].getSymbol()
                .equals(symbol)) ||
                (deck.cardDeck[31].getValue()
                        .equals(value) && deck.cardDeck[31].getSymbol()
                .equals(symbol)))
        {
            symbol = "pik";
        }

        final String finalSymbol = symbol;

        serverClientList.forEach(c -> c.setHasTurn(checkCards(c.getCurrenthand()
                .getCurrentCards(), finalSymbol, value)));

        serverClientList.forEach(s ->
        {
            UpdateGameState.Builder builder = new UpdateGameState.Builder(UpdateGameState.GAME_START, "Server");
            builder.setAllCurrentPlayers(ServerClientToPlayerList(serverClientList, s))
                    .setCurrentHand(s.getCurrenthand())
                    .setHasTurn(s.isHasTurn());

            try
            {
                s.send(builder.build());
            }
            catch (IOException ex)
            {
                Logger.getLogger(GameManager.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        });
    }

    private ServerClient findPlayerInServerList(Predicate<ServerClient> predicate, String errorMsg)
    {
        return serverClientList.stream()
                .filter(predicate)
                .findFirst()
                .orElseThrow(() -> new NoPlayerFoundException(errorMsg));
    }

    public void updateSpielstand(UpdateGameState updateSpielstand)
    {
        updateCurrentPlayer(updateSpielstand, false);

        if (isPlayerFinished(updateSpielstand))
        {
            checkInPlayer(serverClientList.get(currentPlayer));
        }
        if (gameIsFinished())
        {

        }
        else
        {
            ServerClient nextPlayer = getNextPlayer();
            serverClientList.forEach(e -> e.setHasTurn(e.equals(nextPlayer)));
            serverClientList.forEach(serverClient ->
            {
                try
                {
                    serverClient.setNoChange(false);
                    UpdateGameState.Builder builder = new UpdateGameState.Builder(UpdateGameState.UPDATE_SPIELSTAND, "Server");
                    builder.setAllCurrentPlayers(ServerClientToPlayerList(serverClientList, serverClient))
                            .setCurrentField(updateSpielstand.getCurrentField())
                            .setHasTurn(serverClient.isHasTurn());

                    serverClient.send(builder.build());
                }
                catch (IOException ex)
                {
                    Logger.getLogger(GameManager.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            });
        }
    }

    public void noChange(UpdateGameState updateSpielstand) throws IOException
    {
        updateCurrentPlayer(updateSpielstand, true);

        if (checkRoundDone())
        {
            serverClientList.forEach(e -> e.setNoChange(false));

            ServerClient nextPlayerARD = getNextPlayerAfterRoundDone();

            System.out.println(nextPlayerARD.getId());

            serverClientList.forEach(e -> e.setHasTurn(e.equals(nextPlayerARD)));

            serverClientList.forEach(s ->
            {
                UpdateGameState.Builder builder = new UpdateGameState.Builder(UpdateGameState.NEW_ROUND, "Server");
                builder.setAllCurrentPlayers(ServerClientToPlayerList(serverClientList, s))
                        .setHasTurn(s.equals(nextPlayerARD));
                try
                {
                    s.send(builder.build());
                }
                catch (IOException ex)
                {
                    Logger.getLogger(GameManager.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            });
        }
        else
        {
            ServerClient nextPlayerANC = getNextPlayerAfterNoChange(currentPlayer + 1);

            serverClientList.forEach(e -> e.setHasTurn(e.equals(nextPlayerANC)));

            serverClientList.forEach(s ->
            {
                UpdateGameState.Builder builder = new UpdateGameState.Builder(UpdateGameState.NOCHANGE, "Server");
                builder.setAllCurrentPlayers(ServerClientToPlayerList(serverClientList, s))
                        .setHasTurn(s.equals(nextPlayerANC));
                try
                {
                    s.send(builder.build());
                }
                catch (IOException ex)
                {
                    Logger.getLogger(GameManager.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            });
        }

    }

    private ServerClient getNextPlayerAfterRoundDone()
    {
        if (!lastServerClientWhoPlayedACard.isIsFinished())
        {
            return lastServerClientWhoPlayedACard;
        }
        else
        {
            return getNextPlayerAfterNoChange(serverClientList.indexOf(lastServerClientWhoPlayedACard) + 1);
        }
    }

    private boolean checkRoundDone()
    {
        return serverClientList.stream()
                .filter(s -> isPlayerNotFinishedForThisRound(s))
                .count() == 0;
    }

    private boolean isPlayerNotFinishedForThisRound(ServerClient s)
    {
        return !(s.isIsFinished() || s.isNoChange() || s.equals(lastServerClientWhoPlayedACard));
    }

    private void prepareDeck()
    {
        deck = Deck.getInstance();
        deck.shuffleDeck();
    }

    private void spreadOutDeckToPlayers()
    {
        for (int i = 0; i < 5; i++)
        {
            Card[] startHand = new Card[6];
            for (int j = 0; j < 6; j++)
            {
                startHand[j] = deck.cardDeck[i + (5 * j)];
            }
            serverClientList.get(i)
                    .setCurrenthand(new Hand(startHand));
        }

    }

    private List<Player> ServerClientToPlayerList(List<ServerClient> serverClients, ServerClient serverClient)
    {
        List<Player> playerList = new ArrayList<>();

        playerList = serverClients.stream()
                .filter(e -> e.getId() != serverClient.getId())
                .map(e -> getPlayerOfServerClient(e))
                .collect(Collectors.toList());

        return playerList;
    }

    public Player getPlayerOfServerClient(ServerClient s)
    {
        Player player = new Player(s.getSocket(), s.getName());
        player.setCurrenthand(s.getCurrenthand());
        player.setHasTurn(s.isHasTurn());
        return player;
    }

    private void updateCurrentPlayer(UpdateGameState updateSpielstand, boolean change)
    {
        currentPlayer = getPlayerByName(updateSpielstand.getTransmitter());
        if (!change)
        {
            lastServerClientWhoPlayedACard = serverClientList.get(currentPlayer);
            serverClientList.get(currentPlayer)
                    .setCurrenthand(updateSpielstand.getCurrentHand());
        }
        serverClientList.get(currentPlayer)
                .setNoChange(change);

    }

    private boolean isPlayerFinished(UpdateGameState updateSpielstand)
    {
        return updateSpielstand.getCurrentHand()
                .getCurrentCards().length == 0;
    }

    private int getPlayerByName(String name)
    {
        for (int i = 0; i < serverClientList.size(); i++)
        {
            if (serverClientList.get(i)
                    .getName()
                    .equals(name))
            {
                return i;
            }
        }

        return -1;
    }

    private void checkInPlayer(ServerClient s)
    {
        givePlayerRole(s);
        s.setIsFinished(true);
    }

    private void givePlayerRole(ServerClient s)
    {
        s.setRole(Role.getRoleByValue(position));
        position++;
    }

    private ServerClient getNextPlayer()
    {
        ServerClient s = null;
        boolean foundPlayer = false;
        int temporärPlayer = currentPlayer + 1;
        while (!foundPlayer)
        {
            if (!serverClientList.get(temporärPlayer % serverClientList.size())
                    .isIsFinished())
            {
                s = serverClientList.get(temporärPlayer % serverClientList.size());
                foundPlayer = true;
            }
            temporärPlayer++;
        }
        return s;
    }

    private boolean gameIsFinished()
    {
        return serverClientList.size() - 1 == serverClientList.stream()
                .filter(ServerClient::isIsFinished)
                .count();
    }

    private ServerClient getNextPlayerAfterNoChange(int temp)
    {
        ServerClient s = null;
        boolean foundPlayer = false;
        int temporärPlayer = temp;
        while (!foundPlayer)
        {
            if (!serverClientList.get(temporärPlayer % serverClientList.size())
                    .isIsFinished() && !serverClientList.get(temporärPlayer % serverClientList.size())
                            .isNoChange())
            {
                s = serverClientList.get(temporärPlayer % serverClientList.size());
                foundPlayer = true;
            }
            temporärPlayer++;
        }
        return s;
    }

    private boolean checkCards(Card[] cards, String symbol, String value)
    {
        return Stream.of(cards)
                .anyMatch(c -> c.equals(new Card(ResourceLoader.getImage("herz-7.jpg",100,100),symbol, value)));
    }

    private Hand getMiddlemanHand(ServerClient middleman)
    {
        Card[] cards = middleman.getCurrenthand()
                .getCurrentCards();
        Card[] last = Arrays.copyOfRange(deck.cardDeck, deck.cardDeck.length - 2, deck.cardDeck.length);
        Card[] combined = new Card[cards.length + last.length];
        System.arraycopy(cards, 0, combined, 0, cards.length);
        System.arraycopy(last, 0, combined, cards.length, combined.length);

        return new Hand(combined);
    }
}
/*ServerClient lastNotFinishedPlayer = findPlayerInServerList(s -> !s.isIsFinished(),
                    "Es wurde kein Spieler gefunden");

            checkInPlayer(lastNotFinishedPlayer);

            prepareDeck();
            spreadOutDeckToPlayers();
            ServerClient middleman = findPlayerInServerList(s -> s.getRole() == Role.MITTELMANN, "Es wurde kein Mittelmann gefunden");
            middleman.setCurrenthand(getMiddlemanHand(middleman));

            serverClientList.forEach(s ->
            {

                UpdateGameState.Builder builder = new UpdateGameState.Builder(UpdateGameState.NEW_GAME, "Server");
                builder.setAllCurrentPlayers(ServerClientToPlayerList(serverClientList, s))
                        .setCurrentHand(s.getCurrenthand())
                        .setRole(s.getRole())
                        .setHasTurn(s.getRole() == Role.ARSCH);
                try
                {
                    s.send(builder.build());
                }
                catch (IOException ex)
                {
                    Logger.getLogger(GameManager.class.getName())
                            .log(Level.SEVERE, null, ex);
                }

            });
*/
