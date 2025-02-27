package server;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;

    private final ChatLog chatLog;

    private BufferedReader in;

    private BufferedWriter out;

    public ClientHandler(Socket clientSocket, ChatLog chatLog) {
        this.clientSocket = clientSocket;
        this.chatLog = chatLog;
    }

    @Override
    public void run() {
        try {

            in =  new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String nickName = in.readLine();

            chatLog.put(nickName + " подключился к серверу", this);

            while (!Thread.currentThread().isInterrupted()) {
                String message = in.readLine();

                if(Objects.isNull(message)){
                    break;
                }

                System.out.println(nickName + ":" + message);

                chatLog.put(nickName + ":" + message, this);
            }
            chatLog.put(nickName + " отключился от сервера", this);
        } catch (IOException e){
            e.printStackTrace();
        }
        ServerListener.removeClient(this);
    }


    public void sendMessageToClient(String msg) throws IOException {
        if (!clientSocket.isOutputShutdown()) {
            out.write(msg);
            out.newLine();
            out.flush();
        }
    }
}