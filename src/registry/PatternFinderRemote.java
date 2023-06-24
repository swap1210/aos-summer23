package registry;

import java.rmi.Remote;
import java.util.List;

public interface PatternFinderRemote extends Remote {
    List<List<Integer>> findPattern(String pattern) throws java.rmi.RemoteException;
}
