package Windows;

import Modelle.Card;
import Util.ResourceLoader;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * <strong>Project:</strong> NetzwerkKartenspielClient <br>
 * <strong>File:</strong> JPanelFactory <br>
 *
 * @author Philipp Schweizer
 * @version v1
 * @since 05.08.2017
 */
public class JPanelFactory 
{

    ////////////////////////////////////////////////////////////////////////////
    // Constances
    ////////////////////////////////////////////////////////////////////////////
    private final static String FILE_NAME = "JPanelFactory";

    
    
    public static JPanel getCardPanel(Card[] cards, ActionListener btnBehavior, int requiredCardAmount)
    {
        
        
        JPanel contentPane = new JPanel();
        contentPane.setPreferredSize(new Dimension(1400, 600));
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        
        List<Card> cardList = new ArrayList(requiredCardAmount);
        
        Stream.of(cards).forEach(card -> {
           card.addActionListener(event -> {
               if(cardList.contains(card)) 
               {
                   System.out.println("unklicked");
                   card.setIsPessed(false);
                   cardList.remove(card);
                   contentPane.repaint();
               } 
               else
               {
                   if(cardList.size() < requiredCardAmount)
                   {
                       System.out.println("klicked");
                      card.setIsPessed(true);
                      cardList.add(card);
                      contentPane.repaint();
                   }
                   else
                   {
                       JOptionPane.showMessageDialog(null, "Sie haben bereits die Maximale anzahl an karten ausgewählt");
                   }
               }
           });
           card.setIsUseable(true);
        });
        JLabel lbl = new JLabel("Bitte wählen Sie "+String.valueOf(requiredCardAmount)+" Karten aus die Sie nicht mehr möchten");
        lbl.setFont(getBasicFont());
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        contentPane.add(lbl);
        
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.X_AXIS));
        cardPanel.add(Box.createHorizontalStrut(20));
        Stream.of(cards).forEach(card -> 
        {
            cardPanel.add(card.getButton());
            cardPanel.add(Box.createHorizontalStrut(20));
        });
        
        contentPane.add(cardPanel);
        
        JButton btnsend = new JButton("Karten abschicken");
        btnsend.setFont(getBasicFont());
        btnsend.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnsend.addActionListener(e ->
        {
            if(cardList.size()==requiredCardAmount)
            {
                btnBehavior.actionPerformed(e);
            }
            else
            {
                JOptionPane.showMessageDialog(null,"Sie müssen mindestenz "+String
                        .valueOf(requiredCardAmount)+" Karten auswählen" );
            }
        });
        
        contentPane.add(btnsend);
        
        return contentPane;
    }
    
    public static JPanel getBossChoicePanel(Card[] playerHand,GameWindow gameWindow)
    {
        return getChoicePanel(3, gameWindow,playerHand);
    }
    public static JPanel getViceBossChoicePanel(Card[] playerHand,GameWindow gameWindow)
    {
        return getChoicePanel(2, gameWindow,playerHand);
    }
    private static JPanel getChoicePanel(int size,GameWindow gameWindow,Card[] playerHand)
    {
        JPanel contenPane = new JPanel();
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.X_AXIS));
        northPanel.add(Box.createHorizontalStrut(20));
        Stream.of(playerHand).forEach(card -> 
        {
            northPanel.add(card.getButton());
            northPanel.add(Box.createHorizontalStrut(20));
        });
        contenPane.add(northPanel);
        contenPane.setLayout(new BoxLayout(contenPane, BoxLayout.Y_AXIS));
        JLabel[] labels = getJLabelArray("Option ",size);
        JComboBox[] bcs = getJComboBoxs(size,Card.VALUE_OF_THE_CARDS);
        JButton btnSend = new JButton("Anfrage absenden");
        btnSend.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSend.setFont(getBasicFont());
        btnSend.addActionListener( e -> 
        {
            if(boxSelectionIsValid(bcs))
            {
                gameWindow.sendBossRequest((String[])Stream.of(bcs).map(JComboBox::getSelectedItem).toArray());
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Die Boxen dürfen nicht den selben inhalt haben");
            }
        });
        
        contenPane.setPreferredSize(new Dimension(600, 600));
        
        IntStream.rangeClosed(0, size-1).forEach(i -> 
        {
            contenPane.add(Box.createRigidArea(new Dimension(580, 20)));
            contenPane.add(labels[i]);
            contenPane.add(Box.createRigidArea(new Dimension(580, 20)));
            contenPane.add(bcs[i]);
        });
        contenPane.add(Box.createRigidArea(new Dimension(580, 20)));
        contenPane.add(btnSend);
        
        return contenPane;
    }

    private static JLabel[] getJLabelArray(String txt,int size)
    {
        JLabel[] lbls = new JLabel[size];
        IntStream.rangeClosed(0, size-1).forEach(i -> 
        {
            JLabel lbl = new JLabel(txt+String.valueOf(i+1)+":");
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            lbl.setFont(getBasicFont());
            lbls[i] = lbl;
        });
        
        return lbls;
    }

    private static JComboBox[] getJComboBoxs(int size, String[] VALUE_OF_THE_CARDS)
    {
        JComboBox[] jcbs = new JComboBox[size];
        IntStream.rangeClosed(0, size-1).forEach(i -> 
        {
            JComboBox jcb = new JComboBox<>(VALUE_OF_THE_CARDS);
            jcb.setFont(getBasicFont());
            jcbs[i] = jcb;
        });
        
        return jcbs;
    }

    private static boolean boxSelectionIsValid(JComboBox[] bcs)
    {
        return Stream.of(bcs).map(JComboBox::getSelectedItem).distinct().count() == bcs.length;
    }
    private static Font getBasicFont()
    {
        return new Font("Monospaced", Font.PLAIN, 25);
    }

}
