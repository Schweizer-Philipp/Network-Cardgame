/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windows;

import Logic.ButtonLogic;
import Modelle.Card;
import Modelle.Hand;
import Modelle.IUpdatable;
import Modelle.Player;
import Modelle.Role;
import Modelle.UpdateGameState;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import network.UpdateListener;
import network.UpdateTransmitter;

/**
 * @author Philipp Schweizer
 * @version v1
 * @since 05.08.2017
 */
public class GameWindow implements IUpdatable
{

    private static final int SCREEN_WIDTH = 900;
    private static final int SCREEN_HEIGHT = 900;

    private String ipAdress;
    private Socket s;
    private String inGameName;
    private WaitingJPanel waitingJPanel;
    private JFrame frame;
    private JPanel contentPane;
    private JPanel upperPane;
    private JPanel lowerPane;
    private JPanel enemyPanel;
    private JPanel gameFieldPanel;
    private JPanel buttonPanel;
    private JPanel playerHandPanel;
    private JButton btnPlayCards;
    private JButton btnSkipTurn;
    private ArrayList<Player> players;
    private Player player;
    private ButtonLogic btnLogic;
    private Card[] playFieldCards;

    public GameWindow(Socket s, String inGameName, String ipAdress)
    {
        this.ipAdress = ipAdress;
        player = new Player(s, inGameName);
        btnLogic = new ButtonLogic(this);
        buildAndShowWaitingScreen();
        new UpdateListener(s, this);
    }

    public void updateSpieler(boolean hasTurn)
    {
        player.getCurrenthand()
                .setFirstMove(false);
        player.setHasTurn(hasTurn);
    }

    private void buildAndShowWaitingScreen()
    {
        this.waitingJPanel = new WaitingJPanel(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(waitingJPanel, BorderLayout.CENTER);

        frame.setVisible(true);

    }

    public void startGame(Hand hand, boolean hasTurn, boolean firstMove)
    {
        player.setCurrenthand(hand);
        player.getCurrenthand()
                .setFirstMove(firstMove);
        player.setHasTurn(hasTurn);
        buildComponents(hand);
    }

    private void buildComponents(Hand hand)
    {
        frame.remove(waitingJPanel);
        contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

        upperPane = new JPanel();
        upperPane.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        upperPane.setLayout(new BoxLayout(upperPane, BoxLayout.PAGE_AXIS));
        upperPane.setBackground(Color.yellow);
        upperPane.setPreferredSize(new Dimension(SCREEN_WIDTH, (int) (SCREEN_HEIGHT * (2 / 3d))));

        enemyPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                g2d.setStroke(new BasicStroke(2.0f));
                g2d.setFont(new Font("Monospaced", Font.BOLD, 16));
                FontMetrics fM = g2d.getFontMetrics(new Font("Monospaced", Font.BOLD, 16));

                for (int i = 0; i < players.size(); i++)
                {
                    int widthName = fM.stringWidth(players.get(i)
                            .getName());
                    int widthNummber = fM.stringWidth(String.valueOf(players.get(i)
                            .getCurrenthand()
                            .getCurrentCards().length));
                    g2d.setColor((players.get(i)
                            .isHasTurn()) ? Color.YELLOW : Color.BLACK);
                    g2d.drawRect(44 + (i * 200), 10, 180, 80);
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(players.get(i)
                            .getName(), 44 + (i * 200) + ((180 - widthName) / 2), 35);
                    g2d.drawString(String.valueOf(players.get(i)
                            .getCurrenthand()
                            .getCurrentCards().length), 44 + (i * 200) + (180 - widthNummber) / 2, 80);

                }
            }
        };
        enemyPanel.setLayout(new BoxLayout(enemyPanel, BoxLayout.LINE_AXIS));
        enemyPanel.setBackground(Color.GRAY);
        enemyPanel.setPreferredSize(new Dimension(SCREEN_WIDTH, (int) (upperPane.getPreferredSize().height *
                (1 / 4d))));
        enemyPanel.setMaximumSize(new Dimension(SCREEN_WIDTH, (int) (upperPane.getPreferredSize().height *
                (1 / 4d))));
        upperPane.add(enemyPanel);

        upperPane.add(Box.createRigidArea(new Dimension(SCREEN_WIDTH, 8)));

        gameFieldPanel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);

                Graphics2D g2d = (Graphics2D) g;

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                g2d.setColor(Color.BLACK);

                for (int i = 0; i < 5; i++)
                {
                    g2d.fillRect(i * 213, 0, 10, gameFieldPanel.getPreferredSize().height - 40);
                }
                g2d.fillRect(0, 0, gameFieldPanel.getPreferredSize().width, 87);
                g2d.fillRect(0, gameFieldPanel.getPreferredSize().height - 87, gameFieldPanel
                        .getPreferredSize().width, 87);

                g2d
                        .setStroke(new BasicStroke(4.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[]
                        {
                            10.0f
                }, 0.0f));
                g2d.setFont(new Font("Monospaced", Font.BOLD, 18));
                for (int i = 0; i < 4; i++)
                {
                    g2d.draw(new RoundRectangle2D.Double(13 + (i * 213), 90, 190, 265, 25, 25));

                    g2d.drawString("Keine Karte", 40 + (i * 213), 225);
                }

            }
        };
        gameFieldPanel.setLayout(new BoxLayout(gameFieldPanel, BoxLayout.LINE_AXIS));
        gameFieldPanel.setBackground(Color.red);
        gameFieldPanel
                .setPreferredSize(new Dimension(SCREEN_WIDTH, (int) (upperPane.getPreferredSize().height *
                        (3 / 4d))));
        gameFieldPanel.setMaximumSize(new Dimension(SCREEN_WIDTH, (int) (upperPane.getPreferredSize().height *
                (3 / 4d))));
        gameFieldPanel.setMinimumSize(new Dimension(SCREEN_WIDTH, (int) (upperPane.getPreferredSize().height *
                (3 / 4d))));
        upperPane.add(gameFieldPanel);

        lowerPane = new JPanel();
        lowerPane.setLayout(new BoxLayout(lowerPane, BoxLayout.PAGE_AXIS));
        lowerPane.setBackground(Color.GREEN);
        lowerPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lowerPane.setPreferredSize(new Dimension(SCREEN_WIDTH, (int) (SCREEN_HEIGHT * (1 / 3d))));
        lowerPane.setMaximumSize(new Dimension(SCREEN_WIDTH, (int) (SCREEN_HEIGHT * (1 / 3d))));

        playerHandPanel = new JPanel();
        playerHandPanel.setLayout(new BoxLayout(playerHandPanel, BoxLayout.LINE_AXIS));
        playerHandPanel.setPreferredSize(new Dimension(lowerPane.getPreferredSize().width, 221));
        playerHandPanel.setMaximumSize(new Dimension(lowerPane.getPreferredSize().width, 221));
        playerHandPanel.setBackground(Color.black);
        lowerPane.add(playerHandPanel);

        for (Card card : hand.getCurrentCards())
        {
            card.setUpdateListener(this);
            playerHandPanel.add(Box.createRigidArea(new Dimension(10, card.getButton()
                    .getMinimumSize().height)));
            playerHandPanel.add(card.getButton());
        }
        lowerPane.add(Box.createRigidArea(new Dimension(lowerPane.getPreferredSize().width, 10)));

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
        buttonPanel.setPreferredSize(new Dimension(lowerPane.getPreferredSize().width, 45));
        buttonPanel.setMaximumSize(new Dimension(lowerPane.getPreferredSize().width, 45));
        buttonPanel.setBackground(Color.pink);

        btnSkipTurn = addButton("Zug Überspringen");
        btnPlayCards = addButton("Spielen");

        buttonPanel.add(btnSkipTurn);
        buttonPanel.add(btnPlayCards);

        lowerPane.add(buttonPanel);

        contentPane.add(upperPane);
        contentPane.add(lowerPane);
        frame.add(contentPane, BorderLayout.CENTER);

        player.getCurrenthand()
                .getValidCards(null, player);
        if (!player.getCurrenthand()
                .isFirstMove())
        {
            lockPlayField();
        }

        enemyPanel.repaint();
        contentPane.repaint();
        contentPane.revalidate();
        

    }

    private JButton addButton(String txt)
    {
        JButton btn = new JButton(txt);
        btn.setPreferredSize(new Dimension(150, 25));
        btn.setBorderPainted(false);
        btn.setActionCommand(txt);
        btn.addActionListener(btnLogic);
        btn.setFocusPainted(false);
        btn.setEnabled(false);
        return btn;
    }

    private void changePlayerHand(Card[] playerHand)
    {
        playerHandPanel.removeAll();
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
        for (Card card : playerHand)
        {
            card.setUpdateListener(this);
            playerHandPanel.add(Box.createRigidArea(new Dimension(10, card.getButton()
                    .getMinimumSize().height)));
            playerHandPanel.add(card.getButton());
        }

        playerHandPanel.revalidate();
    }

    public void changePlayField(Card[] playField)
    {
        playFieldCards = playField;
        gameFieldPanel.removeAll();
        gameFieldPanel.revalidate();
        gameFieldPanel.repaint();
        if (playFieldCards != null)
        {
            for (Card card : playField)
            {
                gameFieldPanel.add(Box.createRigidArea(new Dimension(10, card.getButton()
                        .getMinimumSize().height)));
                card.setIsUseable(false);
                card.setIsPessed(false);
                card.makeCardTaler();
                gameFieldPanel.add(card.getButton());
                gameFieldPanel.revalidate();
                gameFieldPanel.repaint();
            }
        }

        gameFieldPanel.revalidate();
        gameFieldPanel.repaint();
    }

    @Override
    public void update()
    {
        btnPlayCards.setEnabled(player.getCurrenthand()
                .getValidCards(playFieldCards, player));
    }

    public void updatePlayers(List<Player> allCurrentPlayers)
    {
        this.players = new ArrayList<>(allCurrentPlayers);
        waitingJPanel.updatePlayerCount(players.size());
        if (enemyPanel != null)
        {
            enemyPanel.repaint();
        }
    }

    private void lockPlayField()
    {
        for (Card card : player.getCurrenthand()
                .getCurrentCards())
        {
            card.setIsUseable(false);
        }
        btnPlayCards.setEnabled(false);
        btnSkipTurn.setEnabled(false);
    }

    public void sendGameFieldStatus(boolean change)
    {
        if (!change)
        {
            UpdateGameState.Builder builder = new UpdateGameState.Builder(UpdateGameState.NOCHANGE, player
                    .getName());
            new UpdateTransmitter(player.getSocket()
                    .getPort(), builder.build(), ipAdress);
        }
        else
        {
            List<Card> playedCard = new ArrayList<>();
            for (Card card : player.getCurrenthand()
                    .getCurrentCards())
            {
                card.setIsUseable(false);
                if (card.isIsPressed())
                {
                    playedCard.add(card);
                }
            }

            player.getCurrenthand()
                    .deleteCardsFromHand(Arrays.copyOf(playedCard.toArray(), playedCard.size(), Card[].class));
            changePlayerHand(player.getCurrenthand()
                    .getCurrentCards());

            playFieldCards = Arrays.copyOf(playedCard.toArray(), playedCard.size(), Card[].class);
            changePlayField(playFieldCards);

            UpdateGameState.Builder builder = new UpdateGameState.Builder(UpdateGameState.UPDATE_SPIELSTAND, player
                    .getName());
            builder.setCurrentHand(player.getCurrenthand())
                    .setCurrentField(playFieldCards);
            new UpdateTransmitter(player.getSocket()
                    .getPort(), builder.build(), ipAdress);

        }
    }

    public void prepareTurn()
    {
        if (!player.isHasTurn())
        {
            lockPlayField();
        }
        else
        {
            btnSkipTurn.setEnabled(true);
            player.getCurrenthand()
                    .getValidCards(playFieldCards, player);
        }
    }

    public void newRound()
    {
        btnSkipTurn.setEnabled(false);
    }

    public static JDialog createDialog(JPanel panel, int width, int height)
    {
        JDialog dialog = new JDialog();
        dialog.setSize(width, height);
        dialog.setModal(true);
        dialog.add(panel);
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.getRootPane()
                .setWindowDecorationStyle(JRootPane.NONE);
        dialog.setResizable(false);
        dialog.setVisible(true);

        return dialog;
    }

    public void prepareNewGame(Hand currentHand, Role role)
    {
        player.setCurrenthand(currentHand);
        player.setRole(role);
        
        switch(player.getRole()) 
        {
            case CHEF:
                createDialog(JPanelFactory.getBossChoicePanel(player.getCurrenthand().getCurrentCards(),this), 600 , 600);
        }
    }
    public void sendBossRequest(String... options)
    {
        
    }
}
