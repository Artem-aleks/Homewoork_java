package client;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 8081;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Введите 1 для подключения к серверу, 0 для выхода: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
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

                    sender.start();
                    receiver.start();
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

