package registry;

import java.rmi.Remote;

public interface PatternFinderRemote extends Remote {
    String findPattern(String pattern) throws java.rmi.RemoteException;
}
