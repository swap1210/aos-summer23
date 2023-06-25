package receiver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.registry.Registry;

public class Receiver extends common.Parent {
    Registry registry;

    public Receiver(String startingPort) {
        super(startingPort);

    }

    public void startExecution() {
        // Receiver logic here
        System.out.println("Running in Receiver mode");
        connectToSocket();
    }

    private void connectToSocket() {
        // getting localhost ip
        InetAddress ip;
        try {
            System.out.print("Enter the ip address:port of the middle server: ");
            String middleServerIPPort = scan.nextLine();
            ip = InetAddress.getByName(middleServerIPPort.split(":")[0]);
            int middleServerPort = Integer.parseInt(middleServerIPPort.split(":")[1]);

            // establish the connection with argument port
            try {
                Socket s = new Socket(ip, middleServerPort);
                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println(dis.readUTF());
                scan.nextLine();
                // the following loop performs the exchange of
                // information between client and client handler
                while (true) {
                    System.out.print("1. Get list of senders\n2. Exit\nSend to server: ");
                    String tosend = scan.nextLine();

                    // If client sends exit,close this connection
                    // and then break from the while loop
                    int choice = -1;
                    try {
                        choice = Integer.parseInt(tosend);
                    } catch (Exception e) {
                        choice = -1;
                    }
                    if (choice == 0) {
                        System.out.println("Closing this connection : " + s);
                        s.close();
                        System.out.println("Connection closed");
                        break;
                    } else if (choice == 1) {
                        String senderList = dis.readUTF();
                        System.out.print(senderList);
                        System.out.print("Choose the sender you wanna connect to: ");
                        int select = scan.nextInt();
                        String[] senderArray = senderList.split("\n");
                        this.connectToRMI(senderArray[select]);
                    }
                    dos.writeUTF(tosend);
                }

                // closing resources
                scan.close();
                dis.close();
                dos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (

        UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void connectToRMI(String connectionURL) {
        System.out.println("Preparing to connect to " + connectionURL);
    }
}
