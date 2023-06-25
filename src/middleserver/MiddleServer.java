package middleserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.MyConst;
import model.User;

public class MiddleServer extends common.Parent {
    List<User> userList;
    List<String> senderList;
    ServerSocket serverSocket;

    public MiddleServer(String startingPort) {
        super(startingPort);
        this.userList = new ArrayList<>();
        this.senderList = new ArrayList<>();
        readUsers();
        startServerSocket();
    }

    private void readUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(MyConst.USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    String role = parts[2];
                    User user = new User(username, password, role);
                    userList.add(user);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Print the users
    private void printUsers() {
        System.out.println("Username\tPassword\tRole");
        System.out.println("----------------------------------------");
        for (User user : userList) {
            System.out.println(user.username + "\t\t" + user.password + "\t\t" + user.role);
            System.out.println("----------------------------------------");
        }
    }

    // Print receivers
    private void printSenders() {
        System.out.println("Receiver IP\tReceiver Port");
        System.out.println("----------------------------------------");
        for (String receiver : senderList) {
            System.out.println(receiver.split(":")[0] + "\t\t" + receiver.split(":")[1]);
            System.out.println("----------------------------------------");
        }
    }

    private void startServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.startingPort);
            System.out.println("Server socket created on " + java.net.InetAddress.getLocalHost().getHostAddress() + ":"
                    + this.startingPort);
        } catch (Exception e) {
            System.out.println("Error in creating server socket.");
        }
    }

    public void startExecution() {
        // Middle Server logic here
        System.out.println("Running in Middle Server mode");
        ExecutorService es = Executors.newCachedThreadPool();
        while (true) {
            Socket s = null;

            try {
                System.out.println("Press 0 Exit\n1 Accept more connections\n2 Print Users\n3 Print Senders");
                int choice = scan.nextInt();
                if (choice == 0) {
                    break;
                } else if (choice == 1) {
                    System.out.println("Accepting more connections now...");
                } else if (choice == 2) {
                    printUsers();
                    continue;
                } else if (choice == 3) {
                    printSenders();
                    continue;
                } else {
                    System.out.println("Invalid choice");
                    continue;
                }

                // socket object to receive incoming client requests
                s = serverSocket.accept();

                System.out.println("A new client is connected : " + s);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                System.out.println("Assigning new thread for this client");

                // create a new thread object
                Runnable runnable = new MiddleServerThread(s, dis, dos, this);

                // Invoking the start() method
                es.execute(runnable);
            } catch (Exception e) {
                // s.close();
                e.printStackTrace();
            } finally {
                // ss.close();
            }
        }
        try {
            es.shutdown();
            this.serverSocket.close();
            scan.close();
        } catch (Exception e) {
            System.out.println("Error in closing server socket.");
        }
    }

    // check if username password exists in userList
    public User getUser(String username, String password) {
        for (User user : userList) {
            if (user.username.equals(username) && user.password.equals(password)) {
                return user;
            }
        }
        return null;
    }
}