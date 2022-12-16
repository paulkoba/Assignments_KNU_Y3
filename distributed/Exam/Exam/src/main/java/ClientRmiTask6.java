import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;


public class ClientRmiTask6 {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        int choice = 1000;
        int x, y, z;
        Scanner in = new Scanner(System.in);
        try {
            RMIServer st = (RMIServer) Naming.lookup("//localhost:4444/exam");
            System.out.println("Choose option:\n"
                    + "(1) Look for houses that have a given room count\n"
                    + "(2) Look for houses that have a given room count and floor satisfying conditions\n"
                    + "(3) Look for houses that have a larger area then provided\n");
            choice = in.nextInt();
            switch (choice) {
                case 1: {
                    System.out.println("Enter target room count:");
                    x = in.nextInt();
                    System.out.println(st.displayWithRoomsCount(x));
                    break;
                }
                case 2: {
                    System.out.print("Enter room count: ");
                    x = in.nextInt();
                    System.out.print("Enter lowest floor suitable: ");
                    y = in.nextInt();
                    System.out.print("Enter highest floor suitable: ");
                    z = in.nextInt();
                    System.out.println(st.displayWithRoomsCountInIntervalOfFloor(x, y, z));
                    break;
                }
                case 3: {
                    System.out.println("Enter minimal area:");
                    x = in.nextInt();
                    System.out.println(st.displayWithAreaMoreThan(x));
                    break;
                }
            }
        } catch (RemoteException | NotBoundException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}