package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Omar on 6/12/2016.
 */
public class ClientInitiate extends JFrame {

    private JTextArea messageToSend;
    private JButton sendMessage;
    private Socket socket;
    private PrintWriter printWriter;

    public ClientInitiate() {
        setUpNetworking();
        setLayout(new BorderLayout());
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.X_AXIS));
        sendPanel.add(messageToSend = new JTextArea());
        sendPanel.add(sendMessage = new JButton("Send"));
        add(sendPanel, BorderLayout.SOUTH);
        setSize(new Dimension(800,600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        sendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                printWriter.println(messageToSend.getText());
                printWriter.flush();
                messageToSend.setText("");
                messageToSend.requestFocus();
            }
        });
        messageToSend.requestFocus();
        setVisible(true);
    }

    private void setUpNetworking() {
        try {
            socket = new Socket("127.0.0.1", 4242);
            printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ClientInitiate();
    }
}