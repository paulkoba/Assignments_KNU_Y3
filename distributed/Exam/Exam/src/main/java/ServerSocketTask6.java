import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

class Callback
{
    public boolean shouldEnd = false;
}

class HouseHandler implements Runnable
{
    private final Socket socket;
    private final Callback callback;
    private final List<House> houses;

    public HouseHandler(Socket socket, Callback callback, List<House> houses)
    {
        this.callback = callback;
        this.socket = socket;
        this.houses = houses;
    }

    @Override
    public void run() {
        try {
            InputStreamReader ois = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
            System.out.println("Receiving input");
            BufferedReader reader = new BufferedReader(ois);
            String message = reader.readLine().substring(4);
            System.out.println(message);
            String[] splitMessage = message.split("#");
            String commandIndex = splitMessage[0];
            System.out.println("Command " + splitMessage[0]);

            List<House> result = new ArrayList<>();

            switch (commandIndex) {
                case "1": {
                    Integer x = Integer.parseInt(splitMessage[1]);
                    for (House house : houses) {
                        if (house.getRoomsCount().equals(x)) {
                            result.add(house);
                        }
                    }
                    break;
                }
                case "2": {
                    Integer x = Integer.parseInt(splitMessage[1]);
                    Integer y = Integer.parseInt(splitMessage[2]);
                    Integer z = Integer.parseInt(splitMessage[3]);
                    for (House house : houses) {
                        if ((house.getRoomsCount().equals(x)) &&
                                (house.getFloor() > y && house.getFloor() < z)) {
                            result.add(house);
                        }
                    }
                    break;
                }
                case "3": {
                    Integer x = Integer.parseInt(splitMessage[1]);
                    for (House house : houses) {
                        if (house.getArea() > x){
                            result.add(house);
                        }
                    }
                    break;
                }
                default: {
                    throw new IllegalStateException("Unexpected value: " + commandIndex);
                }
            }
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(result);
            ois.close();
            oos.close();
            socket.close();
        }
        catch (IOException e) { }
    }
}


public class ServerSocketTask6 {
    private static ServerSocket server;

    private static int port = 9876;

    public static Callback c = new Callback();

    public static void main(String args[]) throws IOException, ClassNotFoundException{
        server = new ServerSocket(port);
        List<House> houses =  new ArrayList<House>() {
            {
                add(new House("1", 543, 300, 2, 1, "Wide st.", "High-rise", 20));
                add(new House("2", 465, 250, 3, 2, "Tall st.", "High-rise", 35));
                add(new House("3", 123, 200, 4, 3, "Weird st.", "Low density", 40));
                add (new House("4", 321, 150, 5, 4, "Suspicious avenue", "Low density", 40));
                add(new House("5", 121, 100, 6, 5, "Deep st.", "Low density", 10));
            }
        };

        while(!c.shouldEnd){
            System.out.println("Waiting for the client request");
            Socket socket = server.accept();
            HouseHandler handler = new HouseHandler(socket, c, houses);
            handler.run();
        }
        System.out.println("Shutting down Socket server!!");
        server.close();
    }
}
