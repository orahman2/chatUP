package model;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Created by Omar on 6/12/2016.
 * code based on Head First Java, 2nd Edition. Head First Java, 2nd Edition, ISBN: 0596009208
 * lets client launch their own chat app
 * and send messages to server (localhost)
 */

public class ClientInitiate extends JFrame {

    private JTextArea messageToSend;
    private JButton sendMessage;
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private JTextArea conversation;
    private String name;

    public void go() {
        name = JOptionPane.showInputDialog(null,"What's your name?", "Welcome welcome!", 3);
        if (name == null||name.equals(""))
            name = "anonymous";
        setUpNetworking();
        setUpUI();
        Thread thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            receiveNewMessages();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
        );
        thread.start();
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
            }

            //send message to server
            private void sendMessageNow() {
                LocalDateTime timeStamp = LocalDateTime.now();
                printWriter.println(messageToSend.getText() + "\n - " + name + ", " +
                        timeStamp.getHour()+ ":" + timeStamp.getMinute());
                printWriter.flush();
                messageToSend.setText("");
                messageToSend.requestFocus();
            }

        });

        messageToSend.requestFocus();
        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    /**
     * sets up UI for conversation pane
     */
    private void setConversation() {
        conversation = new JTextArea();
        conversation.setEditable(false);
        conversation.setLineWrap(true);
        conversation.setFont(conversation.getFont().deriveFont(20f));
        JScrollPane jScrollPane = new JScrollPane(conversation);
        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(jScrollPane, BorderLayout.CENTER);
    }

    //receive messages from server
    private void receiveNewMessages() throws IOException {
        String message;
        while ((message=bufferedReader.readLine())!=null){
            conversation.append(message + "\n");
        }
    }

    /**
     * configures bottom part of window to send messages
     */
    private void setSendFrame() {
        JPanel sendPanel = new JPanel();
        sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.X_AXIS));
        JScrollPane jScrollPane = new JScrollPane(messageToSend = new JTextArea());
        messageToSend.setFont(messageToSend.getFont().deriveFont(20f));
        sendPanel.add(jScrollPane);
        jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
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
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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