package sender;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Sender extends common.Parent {
    public Sender(String startingPort) {
        super(startingPort);
    }

    public void startExecution() {
        // Sender logic here
        System.out.println("Running in Sender mode");

        // getting localhost ip
        InetAddress ip;
        try {
            ip = InetAddress.getByName("localhost");

            // establish the connection with argument port
            try {
                Socket s = new Socket(ip, startingPort);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
