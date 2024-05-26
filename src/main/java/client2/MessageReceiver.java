package client2;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

class MessageReceiver implements Runnable {
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


