package server2;


import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ChatLog {
    private final ArrayList<String> chatHistory;
    private int pointer = 0;
    private final ExecutorService executorService;

    public ChatLog() {
        chatHistory = new ArrayList<>();
        executorService = Executors.newFixedThreadPool(2);
    }

    public void put(String message, ClientHandler clientSender) throws IOException {
        executorService.submit(() -> {
            try {
                chatHistory.add(message);
                System.out.println(message);
                update(clientSender);
                pointer++;
            } catch (IOException e) {
                try {
                    throw e;
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void update(ClientHandler clientSender) throws IOException {
        for (ClientHandler client : ServerListener.getClients()) {
            if (client != clientSender) {
                executorService.submit(() -> {
                    try {
                        client.sendMessageToClient(chatHistory.get(pointer));
                    } catch (IOException e) {
                        try {
                            throw e;
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
        }
    }

    public void shutdown() {
        executorService.shutdown();
    }
}

