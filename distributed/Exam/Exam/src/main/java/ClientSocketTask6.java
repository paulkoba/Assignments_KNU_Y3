import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;


public class ClientSocketTask6 {
    private static int port = 9876;

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException{
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        BufferedWriter writer = null;

        Scanner scan = new Scanner(System.in);
        while (true)
        {
            System.out.println("Choose option:\n"
                    + "(1) Look for houses that have a given room count\n"
                    + "(2) Look for houses that have a given room count and floor satisfying conditions\n"
                    + "(3) Look for houses that have a larger area then provided\n");
            socket = new Socket(host.getHostName(), port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
            System.out.println("Sending request to Socket Server");
            int commandIndex = scan.nextInt();
            if (commandIndex == 4)
            {
                socket = new Socket(host.getHostName(), port);
                oos = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Sending close Request");
                oos.writeInt(commandIndex);
                oos.flush();
                break;
            }
            switch (commandIndex) {
                case 1: case 3: {
                    System.out.println("Enter target room count:");
                    int x = scan.nextInt();
                    String message = "" + commandIndex + "#" + x + "\n";
                    writer.write(message);
                    writer.newLine();
                    writer.flush();
                    break;
                }
                case 2: {
                    System.out.print("Enter room count: ");
                    int x = scan.nextInt();
                    System.out.print("Enter lowest floor suitable: ");
                    int y = scan.nextInt();
                    System.out.print("Enter highest floor suitable: ");
                    int z = scan.nextInt();
                    String message = "" + commandIndex + "#" + x + "#" + y + "#" + z + "\n";
                    writer.write(message);
                    writer.newLine();
                    writer.flush();
                    break;
                }
            }
            System.out.println("Results: ");
            ois = new ObjectInputStream(socket.getInputStream());
            ArrayList<House> results = (ArrayList<House>) ois.readObject();
            for (House house: results)
            {
                System.out.println(house.toString());
            }
            ois.close();
            writer.close();
            Thread.sleep(100);
        }
        oos.writeInt(3);
        System.out.println("Shutting down client!!");
    }
}
