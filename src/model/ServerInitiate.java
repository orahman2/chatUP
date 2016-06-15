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
                System.out.println("check 1 complete");
                retrieveMessages(socket);
                System.out.println("check 2 complete");
                sendMessages(socket);
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void retrieveMessages(Socket socket) throws IOException{
        InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        System.out.println("check b1 complete");
        String message;
        if ((message=bufferedReader.readLine())!=null){
            messages.add(message);
        }
        System.out.println("check b2 complete");
//        inputStreamReader.close();
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
