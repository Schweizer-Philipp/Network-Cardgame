package network;

import Modelle.Card;
import Modelle.Hand;
import Modelle.UpdateGameState;
import Windows.GameWindow;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Arrays;

/**
 * @author Philipp Schweizer
 * @version v1
 * @since 05.08.2017
 */
public class UpdateListener implements Runnable
{

    ////////////////////////////////////////////////////////////////////////////
    // Constances
    ////////////////////////////////////////////////////////////////////////////
    private final static String FILE_NAME = "UpdateListener";

    ////////////////////////////////////////////////////////////////////////////
    // Fields
    ////////////////////////////////////////////////////////////////////////////
    private Socket socket;
    private GameWindow gameWindow;
    ////////////////////////////////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Default-Constructor
     */
    public UpdateListener(Socket socket, GameWindow gameWindow)
    {
        this.socket = socket;
        this.gameWindow = gameWindow;
        new Thread(this).start();
    }

    @Override
    public void run()
    {
        try
        {

            InputStream inputStream = socket.getInputStream();

            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Object line = null;

            try
            {
                while ((line = objectInputStream.readObject()) != null)
                {
                    UpdateGameState uGS = (UpdateGameState) line;
                    switch (uGS.getCommand())
                    {
                        case UpdateGameState.ALL_PLAYER:
                            gameWindow.updatePlayers(uGS.getAllCurrentPlayers());
                            break;

                        case UpdateGameState.GAME_START:
                            gameWindow.updatePlayers(uGS.getAllCurrentPlayers());
                            Card[] cards = Arrays.stream(uGS.getCurrentHand().getCurrentCards()).map(c-> new Card(c)).toArray(Card[]::new);
                            gameWindow.startGame(new Hand(cards), uGS.isHasTurn(), uGS.isHasTurn());
                            break;

                        case UpdateGameState.UPDATE_SPIELSTAND:
                            gameWindow.updatePlayers(uGS.getAllCurrentPlayers());
                            gameWindow.updateSpieler(uGS.isHasTurn());
                            gameWindow.changePlayField(uGS.getCurrentField());
                            gameWindow.prepareTurn();
                            break;

                        case UpdateGameState.NOCHANGE:
                            gameWindow.updatePlayers(uGS.getAllCurrentPlayers());
                            gameWindow.updateSpieler(uGS.isHasTurn());
                            gameWindow.prepareTurn();
                            break;

                        case UpdateGameState.NEW_ROUND:
                            gameWindow.changePlayField(null);
                            gameWindow.updatePlayers(uGS.getAllCurrentPlayers());
                            gameWindow.updateSpieler(uGS.isHasTurn());
                            gameWindow.prepareTurn();
                            break;
                            
                        case UpdateGameState.NEW_GAME:
                            System.out.println(uGS.getRole());

                        default:
                            break;
                    }
                }
            }
            catch (ClassNotFoundException ex)
            {
                ex.printStackTrace();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
}
