package middleserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import common.SocketThread;
import model.User;

public class MiddleServerThread extends SocketThread implements Runnable {
    MiddleServer middleServer;

    public MiddleServerThread(Socket s, DataInputStream dis, DataOutputStream dos, MiddleServer middleServer) {
        super(s, dis, dos);
    }

    @Override
    public void run() {
        try {
            this.dos.writeUTF("Enter credentials to login username/password: ");
            String[] userResponse = this.dis.readUTF().split("/");
            // check if middle server has the user
            User foundUser = middleServer.getUser(userResponse[0], userResponse[1]);
            if (foundUser != null) {
                this.dos.writeUTF("Login successful, Welcome " + foundUser.role);
                String command = this.dis.readUTF();
                this.dos.writeUTF("Command received: " + command);
                this.dos.writeUTF("Command executed successfully");
            } else {
                this.dos.writeUTF("Login failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
