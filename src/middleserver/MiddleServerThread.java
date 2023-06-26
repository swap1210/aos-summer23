package middleserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
        try {
            this.dos.writeUTF("Enter credentials to login username/password: ");
            String textReceived = this.dis.readUTF();
            String[] userResponse = textReceived.split("/");
            // check if middle server has the user
            User foundUser = middleServer.getUser(userResponse[0], userResponse[1]);
            if (foundUser != null) {
                this.dos.writeUTF("Login successful, Welcome " + foundUser.role + "\nSend your connection details: ");
                if (foundUser.role.equals("sender")) {
                    // Sender logic here
                    // sender will send his connection details as next response
                    String senderConnectionDetails = this.dis.readUTF();
                    this.middleServer.senderList.add(senderConnectionDetails);
                } else if (foundUser.role.equals("receiver")) {
                    // Receiver logic here
                    // send receiver list of senders to connect to
                    this.receiverStr = String.join("\n", this.middleServer.senderList);
                    // System.out.println("waiting for receiver to send 1");
                    textReceived = this.dis.readUTF();
                    // receiver wants list of senders
                    if (Integer.parseInt(textReceived) == 1) {
                        // send list of senders
                        dos.writeUTF(receiverStr);
                    }
                }

                // String command = this.dis.readUTF();
                // this.dos.writeUTF("Command received: " + command);
            } else {
                this.dos.writeUTF("Login failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
