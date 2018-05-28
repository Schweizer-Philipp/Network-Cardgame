
package Server;

import Modelle.Player;
import Modelle.ServerClient;
import Modelle.UpdateGameState;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Philipp Schweizer
 */
public class Server 
{
    private static final int MAX_PLAYERAMOUNT=5;
    
    private ServerSocket serverSocket;
    
    private List<ServerClient> players;
    
    private static int id = 1;
    
    private GameManager gameManager;
    
    public Server(int port) 
    {           
        try
        {
            players = new ArrayList();
            serverSocket = new ServerSocket(port);
            
            Socket socket = null;
            System.out.println("Server gestartet");
            while( (socket = serverSocket.accept()) != null)
            {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                UpdateGameState updateSpielstand = (UpdateGameState)objectInputStream.readObject();
                if(updateSpielstand.getCommand().equals(UpdateGameState.CONNECT))
                {
                    if(id<=MAX_PLAYERAMOUNT)
                    {
                        informUserAboutConnection(socket, true);
                        players.add(new ServerClient(socket, id, updateSpielstand.getTransmitter()));
                        id++;
                        
                        for(ServerClient serverClient :players)
                        {
                            UpdateGameState.Builder builder = new UpdateGameState.Builder(UpdateGameState.ALL_PLAYER, "Server");
                            builder.setAllCurrentPlayers(ServerClientToPlayerList(getServerClientList(), serverClient));
                            serverClient.send(builder.build());
                        }
                        if(id>MAX_PLAYERAMOUNT)
                        {
                            gameManager = new GameManager(getServerClientList());
                        }
                        
                    }
                    else
                    {
                        informUserAboutConnection(socket, false);
                    }
                }
                else if(updateSpielstand.getCommand().equals(UpdateGameState.UPDATE_SPIELSTAND))
                {
                    gameManager.updateSpielstand(updateSpielstand);
                }
                else if(updateSpielstand.getCommand().equals(UpdateGameState.NOCHANGE))
                {
                    gameManager.noChange(updateSpielstand);
                }
            }
        }
        catch(IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
    private List<Player> ServerClientToPlayerList(List<ServerClient> serverClients,ServerClient serverClient)
    {
        List<Player> playerList = new ArrayList<>();
        
        for(ServerClient sC : serverClients)
            if(sC.getId()!=serverClient.getId())
                playerList.add(new Player(sC.getSocket(), sC.getName()));
        
        return playerList;
    }
    private List<ServerClient> getServerClientList()
    {
        return this.players;
    }
    public static void main(String args[])
    {
        new Server(2221);
    }
    private void informUserAboutConnection(Socket socket,boolean isAbleToConnect)
    {
        ObjectOutputStream oos = null;
        try
        {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.reset();
            oos.writeObject(isAbleToConnect);
        }
        catch (IOException ex)
        {
           ex.printStackTrace();
        }
       
    }
}
