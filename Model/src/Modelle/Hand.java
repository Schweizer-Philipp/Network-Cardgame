/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Philipp Schweizer
 */
public class Hand implements Serializable
{

    private Card[] currentCards;
    private boolean firstMove;

    public boolean isFirstMove()
    {
        return firstMove;
    }

    @Override
    public String toString()
    {
        return Arrays.toString(currentCards);
    }

    public Hand(Card[] currentHand)
    {
        this.currentCards = currentHand;
    }

    public void setFirstMove(
            boolean firstMove)
    {
        this.firstMove = firstMove;
    }

    public void deleteCardsFromHand(Card[] cardsToDelete)
    {
        ArrayList<Card> placeHolder = new ArrayList<>();
        for (Card card : currentCards)
        {
            boolean isCardToDelete = false;
            for (Card DeleteCard : cardsToDelete)
            {
                if (card.getValue()
                        .equals(DeleteCard.getValue()) && card.getSymbol()
                        .equals(DeleteCard.getSymbol()))
                {
                    isCardToDelete = true;
                }
            }
            if (!isCardToDelete)
            {
                placeHolder.add(new Card(card));
            }
        }
        this.currentCards = new Card[placeHolder.size()];
        placeHolder.toArray(currentCards);
    }

    public boolean getValidCards(Card[] playedCards, Player player)
    {
        ArrayList<Card> selectedCards = new ArrayList<>();
        ArrayList<Card> usableCards = new ArrayList<>();
        for (Card card : player.getCurrenthand()
                .getCurrentCards())
        {
            if (card.isIsPressed())
            {
                selectedCards.add(card);
            }
            else
            {
                usableCards.add(card);
            }
        }
        if (player.getCurrenthand()
                .isFirstMove())
        {
            for (Card card : player.getCurrenthand()
                    .getCurrentCards())
            {
                if (!card.getValue()
                        .equals(Card.VALUE_OF_THE_CARDS[0]))
                {
                    card.setIsUseable(false);
                }
                else
                {
                    card.setIsUseable(true);
                }
            }
        }
        else
        {
            if (playedCards == null && selectedCards.size() == 0)
            {
                for (Card card : player.getCurrenthand()
                        .getCurrentCards())
                {
                    card.setIsUseable(true);
                }
            }
            else
            {
                if (playedCards == null && selectedCards.size() != 0)
                {
                    String value = selectedCards.get(0)
                            .getValue();
                    for (Card card : player.getCurrenthand()
                            .getCurrentCards())
                    {
                        if (card.getValue()
                                .equals(value))
                        {
                            card.setIsUseable(true);
                        }
                        else
                        {
                            card.setIsUseable(false);
                        }
                    }
                }
                else
                {
                    if (playedCards != null && selectedCards.size() == 0)
                    {
                        int amountOfCardsPlayed = playedCards.length;
                        int powerPlace = getPowerPlace(playedCards[0]); 
                        for (int i = 0; i < 8; i++)
                        {
                            for (Card card : player.getCurrenthand() .getCurrentCards())
                            {
                                if (card.getValue().equals(Card.VALUE_OF_THE_CARDS[i]))
                                {
                                    if (i <= powerPlace)
                                    {
                                        card.setIsUseable(false);
                                    }
                                    else
                                    {
                                        int needAmountOfCards = amountOfCardsPlayed;
                                        for(Card card1 : player.getCurrenthand().getCurrentCards())
                                        {
                                            if(card1.getValue().equals(Card.VALUE_OF_THE_CARDS[i]))
                                            {
                                                needAmountOfCards--;
                                            }
                                        }
                                        if(needAmountOfCards<=0)
                                        {
                                            card.setIsUseable(true);
                                        }
                                        else
                                        {
                                            card.setIsUseable(false);
                                        }
                                        
                                    }
                                }

                            }
                        }
                        String value = CheckBombe(selectedCards);
                        if (!value.equals("false"))
                        {
                            for (Card card : player.getCurrenthand()
                                    .getCurrentCards())
                            {
                                if (card.getValue()
                                        .equals(value))
                                {
                                    card.setIsUseable(true);
                                }
                            }
                        }
                    }
                    else
                    {
                        if (playedCards != null && selectedCards.size() != 0)
                        {
                            String value = selectedCards.get(0).getValue();
                            int amountOfCardsPlayed = playedCards.length;
                            int amountofcardsWhoCanBeStillPlayed = amountOfCardsPlayed - selectedCards.size();
                            if (amountofcardsWhoCanBeStillPlayed > 0)
                            {
                                for (Card card : player.getCurrenthand().getCurrentCards())
                                {
                                    if (card.getValue().equals(value))
                                    {
                                        card.setIsUseable(true);
                                    }
                                    else
                                    {
                                        card.setIsUseable(false);
                                    }
                                }
                            }
                            else
                            {
                                for (Card card : player.getCurrenthand()
                                        .getCurrentCards())
                                {
                                    card.setIsUseable(false);
                                }
                                for (Card card : selectedCards)
                                {
                                    card.setIsUseable(true);
                                }
                            }
                            value = CheckBombe(selectedCards);
                            if (!value.equals("false"))
                            {
                                for (Card card : player.getCurrenthand()
                                        .getCurrentCards())
                                {
                                    if (card.getValue()
                                            .equals(value))
                                    {
                                        card.setIsUseable(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (playedCards != null && playedCards.length == 4)
            {
                String value = CheckBombe(selectedCards);
                if (!value.equals("false"))
                {
                    if (getPowerPlace(playedCards[0]) < getPowerPlace(value))
                    {
                        for (Card card : player.getCurrenthand()
                                .getCurrentCards())
                        {
                            if (card.getValue()
                                    .equals(value))
                            {
                                card.setIsUseable(true);
                            }
                            else
                            {
                                card.setIsUseable(false);
                            }
                        }
                    }
                }
            }
        }

        return (checkSendButton(playedCards, selectedCards));

    }

    private int getPowerPlace(Card playedCard)
    {
        for (int i = 0; i < Card.VALUE_OF_THE_CARDS.length; i++)
        {
            if (Card.VALUE_OF_THE_CARDS[i].equals(playedCard.getValue()))
            {
                return i;
            }
        }
        return -1;
    }

    private int getPowerPlace(String value)
    {
        for (int i = 0; i < Card.VALUE_OF_THE_CARDS.length; i++)
        {
            if (Card.VALUE_OF_THE_CARDS[i].equals(value))
            {
                return i;
            }
        }
        return -1;
    }

    public Card[] getCurrentCards()
    {
        return currentCards;
    }

    private String CheckBombe(ArrayList<Card> selectedCards)
    {

        for (int i = 0; i < 8; i++)
        {
            int amountofCardsNeedForABomb = 4;
            for (Card card : selectedCards)
            {
                if (card.getValue()
                        .equals(Card.VALUE_OF_THE_CARDS[i]))
                {
                    amountofCardsNeedForABomb--;
                }
            }
            if (amountofCardsNeedForABomb == 0)
            {
                return Card.VALUE_OF_THE_CARDS[i];
            }
        }
        return "false";
    }

    private boolean checkSendButton(Card[] playedCards, ArrayList<Card> selectedCards)
    {
        if (playedCards == null && !selectedCards.isEmpty())
        {
            return true;
        }
        else
        {
            if (playedCards == null && selectedCards.isEmpty())
            {
                return false;
            }
        }
        return playedCards.length <= selectedCards.size();
    }
}
