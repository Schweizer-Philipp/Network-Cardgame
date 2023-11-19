/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import Modelle.UpdateGameState;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Philipp Schweizer
 * @version v1
 * @since 05.08.2017
 */
public class UpdateTransmitter implements Runnable
{

    
    private UpdateGameState uGS;
    private final int port;
    private String ipAdresse;
    
    private final Thread thread;
    
    public UpdateTransmitter(int port, UpdateGameState uGS,String ipAdresse) 
    {   
        this.ipAdresse = ipAdresse;
        this.port = port;
        this.uGS = uGS;
        
        thread = new Thread(this);
        thread.start();
    }    
    
    
    @Override
    public void run() 
    {
         try
        {
            Socket socket = new Socket(ipAdresse, port);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.reset();
            objectOutputStream.writeObject(uGS);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
}
