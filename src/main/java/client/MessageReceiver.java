package client;
import java.io.*;
import java.net.*;

class MessageReceiver extends Thread {
    private final BufferedReader reader;
    private final Socket socket;

    public MessageReceiver(BufferedReader reader, Socket socket) {
        this.reader = reader;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println(message);
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



