package server2;

import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final ChatLog chatLog;
    private BufferedReader in;
    private BufferedWriter out;
    private final ExecutorService executorService;

    public ClientHandler(Socket clientSocket, ChatLog chatLog) {
        this.clientSocket = clientSocket;
        this.chatLog = chatLog;
        this.executorService = Executors.newFixedThreadPool(2);
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String nickName = in.readLine();

            executorService.submit(() -> {
                try {
                    chatLog.put(nickName + " подключился к серверу", this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            while (!Thread.currentThread().isInterrupted()) {
                String message = in.readLine();

                if (Objects.isNull(message)) {
                    break;
                }

                System.out.println(nickName + ":" + message);

                executorService.submit(() -> {
                    try {
                        chatLog.put(nickName + ":" + message, this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }

            executorService.submit(() -> {
                try {
                    chatLog.put(nickName + " отключился от сервера", this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ServerListener.removeClient(this);
            executorService.shutdown();
        }
    }

    public void sendMessageToClient(String msg) throws IOException {
        if (!clientSocket.isOutputShutdown()) {
            executorService.submit(() -> {
                try {
                    out.write(msg);
                    out.newLine();
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
