package Modelle;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Philipp Schweizer
 * @version v1
 * @since 05.08.2017
 */
public class ServerClient extends Player
{

    ////////////////////////////////////////////////////////////////////////////
    // Constances
    ////////////////////////////////////////////////////////////////////////////
    private final static String FILE_NAME = "ServerClient";

    ////////////////////////////////////////////////////////////////////////////
    // Fields
    ////////////////////////////////////////////////////////////////////////////
    private transient final ObjectOutputStream objectOutputStream;

    private final transient int id;

    private boolean noChange;

    public boolean isNoChange()
    {
        return noChange;
    }

    public void setNoChange(
            boolean noChange)
    {
        this.noChange = noChange;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////////////////////////////////
    /**
     * Default-Constructor
     */
    public ServerClient(Socket s, int id, String name) throws IOException
    {
        super(s, name);
        this.id = id;
        objectOutputStream = new ObjectOutputStream(this.getSocket()
                .getOutputStream());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods
    ////////////////////////////////////////////////////////////////////////////
    public int getId()
    {
        return id;
    }

    public void send(UpdateGameState updateSpielstandMessage) throws IOException
    {
        objectOutputStream.reset();
        objectOutputStream.writeObject(updateSpielstandMessage);
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 97 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }
      
        final ServerClient other = (ServerClient) obj;
        
        return getId() == other.getId() && getName().equals(other.getName());
    }
}
