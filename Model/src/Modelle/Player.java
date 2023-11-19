/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelle;

import java.io.Serializable;
import java.net.Socket;

/**
 * @author Philipp Schweizer
 * @version v1
 * @since 05.08.2017
 */
public class Player implements Serializable
{

    private Hand currenthand;
    private transient Socket socket;
    private Role role;
    private boolean hasTurn;
    private String name;
    private boolean isFinished;

    public Player(Socket socket, String name)
    {
        this.socket = socket;
        this.name = name;
        this.hasTurn = false;
    }

    public Hand getCurrenthand()
    {
        return currenthand;
    }

    public void setCurrenthand(Hand currenthand)
    {
        this.currenthand = currenthand;
    }

    public Role getRole()
    {
        return role;
    }

    public void setRole(Role role)
    {
        this.role = role;
    }

    public boolean isHasTurn()
    {
        return hasTurn;
    }

    public void setHasTurn(boolean hasTurn)
    {
        this.hasTurn = hasTurn;
    }

    public Socket getSocket()
    {
        return socket;
    }

    public String getName()
    {
        return name;
    }

    public boolean isIsFinished()
    {
        return isFinished;
    }

    public void setIsFinished(boolean isFinished)
    {
        this.isFinished = isFinished;
    }
}
