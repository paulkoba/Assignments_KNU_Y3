import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

interface RMIServer extends Remote {
    List<House> displayWithRoomsCount(Integer x) throws RemoteException;
    List<House> displayWithRoomsCountInIntervalOfFloor(Integer x, Integer y, Integer z) throws RemoteException;
    List<House> displayWithAreaMoreThan(Integer x) throws RemoteException;
}


public class ServerRmiTask6 {
    public static void main(String[] args) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(4444);
        RMIServer service = new Service();
        registry.rebind("exam", service);
        System.out.println("Server started!");
    }

    static class Service extends UnicastRemoteObject implements RMIServer {
        List<House> houses;

        Service() throws RemoteException {
            super();
            houses = new ArrayList<House>() {
                {
                    add(new House("1", 543, 300, 2, 1, "Wide st.", "High-rise", 20));
                    add(new House("2", 465, 250, 3, 2, "Tall st.", "High-rise", 35));
                    add(new House("3", 123, 200, 4, 3, "Weird st.", "Low density", 40));
                    add (new House("4", 321, 150, 5, 4, "Suspicious avenue", "Low density", 40));
                    add(new House("5", 121, 100, 6, 5, "Deep st.", "Low density", 10));
                }
            };
        }

        @Override
        public List<House> displayWithRoomsCount(Integer x) {
            List<House> results = new ArrayList<>();
            for (House house : houses) {
                if (house.getRoomsCount().equals(x)) {
                    results.add(house);
                }
            }
            return results;
        }

        @Override
        public List<House> displayWithRoomsCountInIntervalOfFloor(Integer x, Integer y, Integer z) {
            List<House> results = new ArrayList<>();
            for(House house: houses) {
                if((house.getRoomsCount().equals(x)) &&
                        (house.getFloor() > y && house.getFloor() < z)) {
                    results.add(house);
                }
            }
            return results;
        }

        @Override
        public List<House> displayWithAreaMoreThan(Integer x){
            List<House> results = new ArrayList<>();
            for (House house : houses) {
                if (house.getArea() > x){
                    results.add(house);
                }
            }
            return results;
        }
    }
}