package Server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(22222);
        IRMIServer server = new RMIServer();
        registry.rebind("server", server);
        System.out.println("Server started!");
    }
}
