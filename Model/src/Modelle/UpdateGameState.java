/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelle;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Philipp Schweizer
 */
public class UpdateGameState implements Serializable
{

    // Constanten
    public static final String CONNECT = "connect";
    public static final String ALL_PLAYER = "allPlayer";
    public static final String DISCONNECT = "disconnect";
    public static final String NOCHANGE = "noChange";
    public static final String UPDATE_SPIELSTAND = "updateSpielstand";
    public static final String GAME_START = "gameStart";
    public static final String NEW_ROUND = "newRound";
    public static final String NEW_GAME = "newGame";
    public static final String SEND_OPTIONS = "sendOptions";

    private final String[] options;
    private final boolean hasTurn;
    private final int transmitterID;
    private final Hand currentHand;
    private final Card[] currentField;
    private final String command;
    private final String transmitter;
    private final List<Player> allCurrentPlayers;
    private final boolean firstMove;
    private final Role role;

    private UpdateGameState(String command, String transmitter, List<Player> allCurrentPlayers, Hand currentHand, Card[] currentField, int transmitterID, boolean hasTurn, boolean firstMove, Role role, String[] options)
    {
        this.hasTurn = hasTurn;
        this.transmitterID = transmitterID;
        this.command = command;
        this.transmitter = transmitter;
        this.allCurrentPlayers = allCurrentPlayers;
        this.currentHand = currentHand;
        this.currentField = currentField;
        this.firstMove = firstMove;
        this.role = role;
        this.options = options;
    }

    public String[] getOptions()
    {
        return options;
    }

    public boolean isFirstMove()
    {
        return firstMove;
    }

    public boolean isHasTurn()
    {
        return hasTurn;
    }

    public List<Player> getAllCurrentPlayers()
    {
        return allCurrentPlayers;
    }

    public String getTransmitter()
    {
        return transmitter;
    }

    public String getCommand()
    {
        return command;
    }

    public Hand getCurrentHand()
    {
        return currentHand;
    }

    public Card[] getCurrentField()
    {
        return currentField;
    }

    public int getTransmitterID()
    {
        return transmitterID;
    }

    public Role getRole()
    {
        return role;
    }

    // -------------------------------------------
    public static class Builder
    {

        private boolean hasTurn;
        private int transmitterID;
        private Hand currentHand;
        private Card[] currentField;
        private final String command;
        private final String transmitter;
        private List<Player> allCurrentPlayers;
        private boolean firstMove;
        private Role role;
        private String[] options;

        public Builder(String command, String transmitter)
        {
            this.command = command;
            this.transmitter = transmitter;
        }

        public Builder setHasTurn(boolean hasTurn)
        {
            this.hasTurn = hasTurn;
            return this;
        }

        public Builder setOptions(String[] options)
        {
            this.options = options;
            return this;
        }

        public Builder setTransmitterID(int transmitterID)
        {
            this.transmitterID = transmitterID;
            return this;
        }

        public Builder setCurrentHand(Hand currentHand)
        {
            this.currentHand = currentHand;
            return this;
        }

        public Builder setCurrentField(Card[] currentField)
        {
            this.currentField = currentField;
            return this;
        }

        public Builder setAllCurrentPlayers(List<Player> allCurrentPlayers)
        {
            this.allCurrentPlayers = allCurrentPlayers;
            return this;
        }

        public Builder setFirstMove(boolean firstMove)
        {
            this.firstMove = firstMove;
            return this;
        }

        public Builder setRole(Role role)
        {
            this.role = role;
            return this;
        }

        public UpdateGameState build()
        {
            return new UpdateGameState(command, transmitter, allCurrentPlayers, currentHand, currentField, transmitterID, hasTurn, firstMove, role, options);
        }

    }
}
