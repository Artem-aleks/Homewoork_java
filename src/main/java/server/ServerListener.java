package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerListener {
    private ServerSocket serverSocket;
    private static ArrayList<ClientHandler> clients;
    public void start() throws IOException{
        serverSocket = new ServerSocket(8081);
        ChatLog log = new ChatLog();

        while (true){
            Socket incomingConnection = serverSocket.accept();
            ClientHandler client = new ClientHandler(incomingConnection, log);
            clients.add(client);
            Thread clietnThread = new Thread(client);
            clietnThread.start();
        }
    }
    public ServerListener(){
        clients = new ArrayList<>();
    }
    public static List<ClientHandler> getClients(){
        return clients;
    }
    public static void removeClient(ClientHandler clientHandler){
        System.out.println("Client "+clientHandler+" is deleted");
        clients.remove(clientHandler);
    }
}