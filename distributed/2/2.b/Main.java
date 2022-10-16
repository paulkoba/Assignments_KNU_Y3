import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Item {
    public int price;
    
    public Item() {
        price = (int)(Math.random() * 1000);
    }
}

class Main {
    private static BlockingQueue<Item> storage = new ArrayBlockingQueue<Item>(10);
    private static BlockingQueue<Item> stealing = new ArrayBlockingQueue<Item>(10);
    private static BlockingQueue<Item> carrying = new ArrayBlockingQueue<Item>(10);
    private static ArrayList<Item> result = new ArrayList<Item>(10);

    public static void main(String[] args) {
        for(int i = 0; i < 10; ++i) {
            storage.add(new Item());
        }

        Thread a = new Thread(() -> {
            try {
                while(!storage.isEmpty()) {
                    Item item = storage.take();
                    System.out.println("Yoinking an item");
                    Thread.sleep((int)(Math.random() * 100));
                    System.out.println("Yoinked an item");
                    stealing.put(item);
                }
            }
            catch(InterruptedException ex) {
                System.out.println("Interrupted");
            }
        });

        Thread b = new Thread(() -> {
            try {
                while(true) {
                    Item item = stealing.take();
                    System.out.println("Carrying an item");
                    Thread.sleep((int)(Math.random() * 100));
                    System.out.println("Brought an item");
                    carrying.put(item);
                }
            }
            catch(InterruptedException ex) {
                System.out.println("Interrupted");
            }
        });

        Thread c = new Thread(() -> {
            try {
                while(true) {
                    Item item = carrying.take();
                    System.out.println("Inventorizing an item");
                    Thread.sleep((int)(Math.random() * 100));
                    System.out.println("Inventorized an item. Worth " + item.price);
                    result.add(item);
                }
            }
            catch(InterruptedException ex) {
                System.out.println("Interrupted");
            }
        });

        c.start();
        b.start();
        a.start();

        try {
            Thread.sleep(10000);
        }
        catch(InterruptedException ex) {
            System.out.println("Interrupted");
        }
    }
}