package model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Omar on 6/12/2016.
 */
/**
 * lets client launch their own chat app
 * and send messages to server (localhost)
 */

public class ClientInitiate extends JFrame {

    private JTextArea messageToSend;
    private JButton sendMessage;
    private Socket socket;
    private PrintWriter printWriter;
    private JPanel conversation;

    public void go() {
        setUpNetworking();
        setUpUI();
    }

    /**
     * sets UI for application
     * adds networking functionality to send message
     */
    private void setUpUI() {
        setLayout(new BorderLayout());
        setSendFrame();
        setConversation();
        setSize(new Dimension(800,600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        sendMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessageNow();
                System.out.println("message sent");
                try {
                    receiveNewMessages();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                System.out.println("message received");

            }
            //receive messages from server
            private void receiveNewMessages() throws IOException {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())
                );
                String message;
                while ((message=bufferedReader.readLine())!=null){
                    conversation.add(new JLabel(message));
                    repaint();
                    revalidate();
                }
            }
            //send message to server
            private void sendMessageNow() {
                printWriter.println(messageToSend.getText());
                printWriter.flush();
                messageToSend.setText("");
                messageToSend.requestFocus();
            }
        });

        messageToSend.requestFocus();
    }

    /**
     * sets up UI for conversation pane
     */
    private void setConversation() {
        (conversation = new JPanel()).setLayout(new BoxLayout(conversation, BoxLayout.Y_AXIS));
        add(new JScrollPane(conversation), BorderLayout.CENTER);
    }

    /**
     * configures bottom part of window to send messages
     */
    private void setSendFrame() {
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.X_AXIS));
        sendPanel.add(messageToSend = new JTextArea());
        sendPanel.add(sendMessage = new JButton("Send"));
        add(sendPanel, BorderLayout.SOUTH);
    }

    /**
     * creates socket
     */
    private void setUpNetworking() {
        try {
            socket = new Socket("127.0.0.1", 4242);
            printWriter = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientInitiate clientInitiate = new ClientInitiate();
        clientInitiate.go();
        clientInitiate.setVisible(true);
    }
}