package server2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerListener extends Thread {
    private static final int SERVER_PORT = 8081;
    private static final List<ClientHandler> clients = new ArrayList<>();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(4);
    private final ChatLog chatLog;

    public ServerListener() {
        this.chatLog = new ChatLog();
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Сервер запущен. Ожидание подключений...");

            while (!isInterrupted()) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Новое подключение: " + clientSocket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket, chatLog);
                addClient(clientHandler);

                executorService.submit(clientHandler);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            chatLog.shutdown();
            executorService.shutdown();
        }
    }

    private static synchronized void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    public static synchronized void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public static synchronized List<ClientHandler> getClients() {
        return new ArrayList<>(clients);
    }
}

