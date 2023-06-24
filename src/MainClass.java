import java.util.Scanner;

import middleserver.MiddleServer;
import receiver.Receiver;
import sender.Sender;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the mode: ");
        System.out.println("1. Sender");
        System.out.println("2. Receiver");
        System.out.println("3. Middle Server");

        int mode = scanner.nextInt();
        scanner.nextLine(); // Consume newline character

        switch (mode) {
            case 1:
                Sender sender = new Sender(args[0]);
                sender.startExecution();
                break;
            case 2:
                Receiver receiver = new Receiver();
                receiver.startExecution();
                break;
            case 3:
                MiddleServer middleServer = new MiddleServer(args[0]);
                middleServer.startExecution();
                break;
            default:
                System.out.println("Invalid mode selected.");
                break;
        }

        scanner.close();
    }
}
