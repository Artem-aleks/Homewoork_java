package client2;

import java.io.PrintWriter;
import java.util.Scanner;

class MessageSender implements Runnable {
    private final PrintWriter writer;
    private final String username;

    public MessageSender(PrintWriter writer, String username) {
        this.writer = writer;
        this.username = username;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        String message;

        do {
            message = scanner.nextLine();
            writer.println(username + ": " + message);
        } while (!message.equalsIgnoreCase("exit"));

        scanner.close();
    }
}
