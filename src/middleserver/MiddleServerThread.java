package middleserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import common.SocketThread;
import model.User;

public class MiddleServerThread extends SocketThread implements Runnable {
    MiddleServer middleServer;
    String receiverStr = "";

    public MiddleServerThread(Socket s, DataInputStream dis, DataOutputStream dos, MiddleServer middleServer) {
        super(s, dis, dos);
        this.middleServer = middleServer;
    }

    @Override
    public void run() {
        User foundUser;
        String textReceived;
        try {
            this.dos.writeUTF("Enter credentials to login username/password: ");
            while (true) {
                String errorMsg = "Login failed! try again: ";
                try {
                    textReceived = this.dis.readUTF();
                    String[] userResponse = textReceived.split("/");
                    // check if middle server has the user
                    foundUser = middleServer.getUser(userResponse[0], userResponse[1]);
                    if (foundUser != null) {
                        break;
                    } else {
                        this.dos.writeUTF(errorMsg);
                    }
                } catch (Exception e) {
                    System.out.println("Login attempt failure by " + s.getRemoteSocketAddress().toString()
                            .replace("/", ""));
                    this.dos.writeUTF(errorMsg);
                }
            }

            // login sucessful
            this.dos.writeUTF("Login successful, Welcome " + foundUser.role + "!");
            if (foundUser.role.equals("sender")) {
                // Sender logic here
                // sender will send his connection details as next response
                String senderConnectionDetails = this.dis.readUTF();
                // receiver wants list of senders
                if (senderConnectionDetails.contains(":")
                        && this.middleServer.senderList.add(senderConnectionDetails)) {
                    // send list of senders
                    dos.writeUTF("You are registered in server.");
                } else {
                    dos.writeUTF("Invalid self registration details.");
                }
            } else if (foundUser.role.equals("receiver")) {
                // Receiver logic here
                // send receiver list of senders to connect to
                this.receiverStr = String.join("\n", this.middleServer.senderList);
                while (true) {
                    try {
                        textReceived = this.dis.readUTF();
                    } catch (Exception e) {
                        System.out.println("Connection closed abruptly by "
                                + (((InetSocketAddress) s.getRemoteSocketAddress()).getAddress()).toString()
                                        .replace("/", ""));
                    }
                    try {
                        // receiver wants list of senders
                        if (Integer.parseInt(textReceived) == 1) {
                            // send list of senders
                            dos.writeUTF(receiverStr);
                            continue;
                        }
                    } catch (Exception e) {
                        dos.writeUTF("Invalid Input");
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
