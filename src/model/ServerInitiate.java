package model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Omar on 6/12/2016.
 * code based on Head First Java, 2nd Edition. Head First Java, 2nd Edition, ISBN: 0596009208
 */
public class ServerInitiate {

    private ArrayList<PrintWriter> clientOutputStreams;

    /**
     * initializes client printstream arraylist
     */
    public ServerInitiate(){
        clientOutputStreams = new ArrayList();
    }

    /**
     * sets readers for each client
     */
    public void go() {

        try(ServerSocket serverSocket = new ServerSocket(4242)){
            while (true){
                Socket socket = serverSocket.accept();
                clientOutputStreams.add(new PrintWriter(socket.getOutputStream()));
                Thread thread = new Thread(new UserMessages(socket));
                thread.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        new ServerInitiate().go();
    }

    /**
     * Starts waiting for messages sent by user
     */
    private class UserMessages implements Runnable {

        BufferedReader bufferedReader;
        String message;

        private UserMessages(Socket socket) throws IOException {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                while ((message = bufferedReader.readLine())!=null){
                    for (PrintWriter writer: clientOutputStreams){
                        writer.println(message);
                        writer.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
