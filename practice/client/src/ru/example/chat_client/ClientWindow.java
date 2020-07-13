package ru.example.chat_client;

import ru.example.network.TCPConnection;
import ru.example.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow extends JFrame  implements ActionListener, TCPConnectionListener
{

    private static final String IP_ADDRESS = "127.0.0.1";//"81.1.241.22"; //"89.222.249.131";
    private static final int PORT = 22;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;


    public static void main(String[] args){
         SwingUtilities.invokeLater(new Runnable() {
             @Override
             public void run() {
                 new ClientWindow();
             }
         });
    }
    private final JTextArea log = new JTextArea();
    private final JTextField fieldNickname = new JTextField("Введите ваш никнейм");
    private final JTextField fieldInput = new JTextField();

    private TCPConnection connection;

    private ClientWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        log.setEditable(false);
        log.setLineWrap(true);

        add(log, BorderLayout.CENTER);
        fieldInput.addActionListener(this);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNickname, BorderLayout.NORTH);

        setVisible(true);
        try {
            connection = new TCPConnection(this, IP_ADDRESS, PORT);
        } catch (IOException e) {
            printMessage("Connection exception" + e);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if(msg.equals("")) return;
        fieldInput.setText(null);
        connection.sendString(fieldNickname.getText() + ": " + msg);
    }


    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMessage("Connection ready");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMessage(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMessage("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMessage("Connection exception" + e);
    }
    private synchronized void printMessage(String msg){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg + "\r\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
