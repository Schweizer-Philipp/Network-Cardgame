/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameUtils;

import Modelle.Card;
import Util.ResourceLoader;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author Philipp Schweizer
 */
public class Deck 
{
    private static String[] symbols={"herz","karo","pik","kreuz"};
    private static String[] valueOfTheCards={"7","8","9","10","ass","bube","dame","koenig"};
    private static Deck deck;
    
    public Card[] cardDeck;
    
    private Deck()
    {
        filldeck();
    }

    private void filldeck() 
    {
       cardDeck = new Card[32];
    
       int counter = 0;
       
        for (String symbol : symbols) 
        {
           for (String valueOfTheCard : valueOfTheCards) 
           {
               cardDeck[counter] = new Card(ResourceLoader.getImage(symbol + "-" + valueOfTheCard + ".jpg",Card.LABEL_WIDTH,Card.LABEL_HEIGHT), symbol, valueOfTheCard);
               counter++;
           }
        }
    }
    
    public void shuffleDeck()
    {
        Collections.shuffle(Arrays.asList(cardDeck));
    }
    public static synchronized Deck getInstance() 
    {
        if (deck == null) 
        {
          deck = new Deck();
        }
        return deck;
    }
    
    
}

