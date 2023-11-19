/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Windows;

import Modelle.UpdateGameState;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * @author Philipp Schweizer
 * @version v1
 * @since 05.08.2017
 */
public class RegisterWindow {

    private JFrame registerFrame;
    private JTextField jtfInGameName;
    private JTextField jtfPort;
    private JTextField jtfIpAdress;
    private Socket socket;

    public RegisterWindow(int i) {

        init(i);
    }

    private void init(int i) {

        registerFrame = new JFrame("NetzwerkKartenspiel");
        registerFrame.setSize(800, 800);
        registerFrame.setLayout(null);
        registerFrame.setBackground(Color.decode("#B2FFFF"));
        //registerFrame.getRootPane().setCursor(ResourceLoader.getCursor("Maus.png"));
        registerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildAndAddJLabel("Benutzername: ", 50);
        jtfInGameName = buildAndAddJTextfield(String.format("Spieler %d", i), 100);

        buildAndAddJLabel("Port:", 150);
        jtfPort = buildAndAddJTextfield("2221", 200);

        buildAndAddJLabel("IPAdresse:", 250);
        jtfIpAdress = buildAndAddJTextfield("localhost", 300);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(new Font("Monospaced", Font.PLAIN, 25));
        btnLogin.addActionListener(e -> connectToServerAndDisposeWindow());
        btnLogin.setBounds(200, 500, 400, 100);
        registerFrame.add(btnLogin);

        registerFrame.setVisible(true);


        //connectToServerAndDisposeWindow();
    }

    private JTextField buildAndAddJTextfield(String caption, int y) {

        JTextField textfieldToAdd = new JTextField(caption);
        textfieldToAdd.setBounds(200, y, 400, 50);
        textfieldToAdd.setFont(new Font("Monospaced", Font.PLAIN, 25));
        registerFrame.add(textfieldToAdd);

        return textfieldToAdd;
    }

    private void buildAndAddJLabel(String caption, int y) {
        JLabel lblToAdd = new JLabel(caption);
        lblToAdd.setBounds(200, y, 100, 50);
        registerFrame.add(lblToAdd);
    }


    private void connectToServerAndDisposeWindow() {
        if (isConnectionAvailable()) {
            new GameWindow(socket, jtfInGameName.getText(), jtfIpAdress.getText());
            registerFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Es ist gerade ein Spiel im gange versuchen sie es später erneut");
        }
    }

    private boolean isConnectionAvailable() {
        // Test
        boolean isAbleToConnect = false;

        try {
            socket = new Socket("localhost", Integer.valueOf(jtfPort.getText()));//TODO localhost durch textfeld erstezen
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.reset();
            UpdateGameState.Builder builder = new UpdateGameState.Builder(UpdateGameState.CONNECT, this.jtfInGameName.getText());
            oos.writeObject(builder.build());

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            isAbleToConnect = (boolean) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return isAbleToConnect;
    }

    public static void main(String args[]) {

        for (int i = 0; i < 5; i++) {
            new RegisterWindow(i);
        }
        //new RegisterWindow(0);
    }
}
