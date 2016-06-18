package model;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Omar on 6/12/2016.
 */
public class ServerInitiate {

    private ArrayList messages;
    private Socket socket;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    public ServerInitiate(){
        messages = new ArrayList();
    }

    /**
     * launches server
     */
    public synchronized void go() {

        try(ServerSocket serverSocket = new ServerSocket(4242)){
            while (true){
                socket = serverSocket.accept();
                inputStreamReader = new InputStreamReader(socket.getInputStream());
                bufferedReader = new BufferedReader(inputStreamReader);
                printWriter = new PrintWriter(socket.getOutputStream());
                while (true){
                    System.out.println("check 1 complete");
                    retrieveMessages();
                    System.out.println("check 2 complete");
                    sendMessages();
                    System.out.println("check 3 complete");
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void retrieveMessages() throws IOException{
        System.out.println("check b1 complete");
        String message;
        if ((message=bufferedReader.readLine())!=null){
            messages.add(message);
        }
        System.out.println("check b2 complete");
    }

    private void sendMessages() throws IOException{
        for (String s: (ArrayList<String>) messages){
            printWriter.println(s+" - "+System.currentTimeMillis());
        }
        printWriter.flush();
        messages.clear();
    }

    public static void main(String[] args) {
        new ServerInitiate().go();
    }
}
