package client2;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8081;
    private static final int THREAD_POOL_SIZE = 2;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        while (true) {
            System.out.print("Введите 1 для подключения к серверу, 0 для выхода: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
                executorService.shutdown();
                break;
            } else if (choice == 1) {
                try {
                    Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                    System.out.println("Подключение к серверу установлено");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

                    System.out.print("Введите ваше имя: ");
                    String username = scanner.nextLine();
                    writer.println(username);

                    System.out.print("Введите ваше сообщение: ");
                    String message = scanner.nextLine();
                    writer.println(message);

                    MessageSender sender = new MessageSender(writer, username);
                    MessageReceiver receiver = new MessageReceiver(reader, socket);

                    executorService.submit(sender);
                    executorService.submit(receiver);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

        scanner.close();
    }
}

