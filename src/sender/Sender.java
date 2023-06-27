package sender;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import common.MyConst;
import registry.PatternFinderRemote;
import service.MatchAPattern;

public class Sender extends common.Parent {
    Registry registry;

    public Sender(String startingPort) {
        super(startingPort);
    }

    public void startExecution() {
        // Sender logic here
        System.out.println("Running in Sender mode");
        registerRMI();
        connectToSocket();
    }

    private void registerRMI() {
        try {
            registry = LocateRegistry.createRegistry(startingPort);
            PatternFinderRemote pfr = (PatternFinderRemote) UnicastRemoteObject.exportObject(new PatternFinderRemote() {
                @Override
                public String findPattern(String pattern) throws RemoteException {
                    return MatchAPattern.perform(pattern);
                }
            }, 0);
            registry.rebind(MyConst.REGISTRY_NAME, pfr);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
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

                // the following loop performs the exchange of
                // information between client and client handler
                int twice = 2;
                while (twice-- > 0) {
                    System.out.print(dis.readUTF());
                    String tosend = scan.nextLine();
                    dos.writeUTF(tosend);
                }

                // show connection registration details from middle server
                System.out.println(dis.readUTF());

                while (true) {
                    System.out.print("Press Exit anytime to close this sender: ");
                    String tosend = scan.nextLine();
                    // If client sends exit,close this connection
                    // and then break from the while loop
                    if (tosend.equals("Exit")) {
                        System.out.println(
                                "Closing this connection " + s.getLocalAddress().toString() + ":" + s.getPort() + ".");
                        s.close();
                        System.out.println("Connection closed");
                        break;
                    }

                }

                // closing resources
                scan.close();
                dis.close();
                dos.close();
                registry.unbind(MyConst.REGISTRY_NAME);
            } catch (IOException | NotBoundException e) {
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
