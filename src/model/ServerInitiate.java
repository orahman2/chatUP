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

    public ServerInitiate(){
        messages = new ArrayList();
    }

    /**
     * launches server
     */
    public void go() {

        try(ServerSocket serverSocket = new ServerSocket(4242)){
            while (true){
                Socket socket = serverSocket.accept();
                retrieveMessages(socket);
                sendMessages(socket);
            }
        }
        catch (IOException e){}
    }

    private void retrieveMessages(Socket socket) throws IOException{
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String message;
        while ((message=bufferedReader.readLine())!=null){
            messages.add(message);
        }
        inputStreamReader.close();
    }

    private void sendMessages(Socket socket) throws IOException{
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        for (String s: (ArrayList<String>) messages){
            printWriter.println(s+" - "+System.currentTimeMillis());
        }
        printWriter.close();
        messages.clear();
    }

    public static void main(String[] args) {
        new ServerInitiate().go();
    }
}
