/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * @author Philipp Schweizer
 */
public class Card implements Serializable {

    public static final int LABEL_HEIGHT = 189;
    public static final int LABEL_WIDTH = 135;
    private static final int LABEL_WIDTH_XL = 203;
    private static final int LABEL_HEIGHT_XL = 276;

    public static final String[] SYMBOLS =
            {
                    "herz" , "karo" , "pik" , "kreuz"
            };
    public static final String[] VALUE_OF_THE_CARDS =
            {
                    "7" , "8" , "9" , "bube" , "dame" , "koenig" , "10" , "ass"
            };

    private transient Image image;
    private JButton button;

    private ImageIcon imageIcon;
    private String symbol;
    private String value;
    private boolean isPressed;
    private boolean isUseable;

    private transient IUpdatable updatableListener;

    public Card(Image image, String symbol, String value) {

        this.isPressed = false;
        this.imageIcon = new ImageIcon(image);
        this.image = image;
        this.symbol = symbol;
        this.value = value;
        buildJlabel();


    }

    public Card(Card card) {

        this(card.getImage(), card.getSymbol(), card.getValue());
        button.repaint();
    }

    public boolean isIsPressed() {

        return isPressed;
    }

    @Override
    public String toString() {

        return symbol + "#" + value;
    }


    public Image getImage() {

        return (image == null) ? imageIcon.getImage() : image;
    }

    public String getSymbol() {

        return symbol;
    }

    public String getValue() {

        return value;
    }

    private void buildJlabel() {

        button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                g2d.drawImage((Card.this.image == null ? imageIcon.getImage() : Card.this.image), 0, 0, null);

                if (isPressed) {
                    g2d.setColor(Color.BLUE);
                    g2d.setStroke(new BasicStroke(5.0f));
                    g2d.drawRect(0, 0, this.getSize().width, this.getSize().height);
                }
            }
        };
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
        button.setMaximumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
        button.setMinimumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("ich wurde gedrückt");
                isPressed = !isPressed;
                updatableListener.update();
                button.repaint();
            }
        });
        setIsUseable(false);
    }

    public void addActionListener(ActionListener e) {

        System.out.println("hier");
        button.removeActionListener(button.getActionListeners()[0]);
        button.addActionListener(e);
    }

    public void makeCardTaler() {

        if (image == null) {
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(LABEL_WIDTH_XL, LABEL_HEIGHT_XL, Image.SCALE_SMOOTH));
        } else {
            image = image.getScaledInstance(LABEL_WIDTH_XL, LABEL_HEIGHT_XL, Image.SCALE_SMOOTH);
        }
        button.setPreferredSize(new Dimension(LABEL_WIDTH_XL, LABEL_HEIGHT_XL));
        button.setMaximumSize(new Dimension(LABEL_WIDTH_XL, LABEL_HEIGHT_XL));
        button.setMinimumSize(new Dimension(LABEL_WIDTH_XL, LABEL_HEIGHT_XL));
    }

    public void makeCardsmaller() {

        if (image == null) {
            imageIcon = new ImageIcon(imageIcon.getImage().getScaledInstance(LABEL_WIDTH, LABEL_HEIGHT, Image.SCALE_SMOOTH));
        } else {
            image = image.getScaledInstance(LABEL_WIDTH, LABEL_HEIGHT, Image.SCALE_SMOOTH);
        }
        button.setPreferredSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
        button.setMaximumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
        button.setMinimumSize(new Dimension(LABEL_WIDTH, LABEL_HEIGHT));
    }

    public JButton getButton() {

        return button;
    }

    public boolean isIsUseable() {

        return isUseable;
    }

    public void setIsUseable(boolean isUseable) {

        this.isUseable = isUseable;
        button.setEnabled(isUseable);
    }

    public void setUpdateListener(IUpdatable updatableListener) {

        this.updatableListener = updatableListener;
    }

    public void setIsPessed(boolean isPressed) {

        this.isPressed = isPressed;
        button.repaint();
    }

    @Override
    public int hashCode() {

        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.symbol);
        hash = 89 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Card other = (Card) obj;
        if (!Objects.equals(this.symbol, other.symbol)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }
}
