import java.util.concurrent.atomic.AtomicInteger;

class Pot {
    private final int capacity;
    private int beeLevel = 0;
    private final Object synchronizer = new Object();

    public Pot(int capacity) {
        this.capacity = capacity;
    }

    public void addHoney(int volume) {
        assert(volume > 0);

        beeLevel += volume;
        beeLevel = Math.min(beeLevel, capacity);

        synchronized(synchronizer) {
            synchronizer.notifyAll();
        }
    }

    public void waitUntilFull() {
        synchronized(synchronizer) {
            try {
                while(beeLevel < capacity) {
                    synchronizer.wait();
                }
            }
            catch(InterruptedException ex) {
                System.out.println("Interrupted");
            }
        }
    }

    public boolean isFull() {
        return beeLevel == capacity;
    }

    public void clear() {
        beeLevel = 0;
    }
}

class Bee implements Runnable {
    private final Pot pot;
    private final int idx;

    public Bee(Pot pot, int idx) {
        this.pot = pot;
        this.idx = idx;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep((int)(Math.random() * 10000));
                if(!pot.isFull()) {
                    pot.addHoney(1);
                    System.out.println("Bee " + idx + " performed work on bucket.");
                }
            }
            catch (InterruptedException ex) {
                System.out.println("Interrupted");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Pot pot = new Pot(100);
        Thread[] bees = new Thread[100];
        
        Thread bear = new Thread(() -> {
            try {
                while(true) {
                    pot.waitUntilFull();
                    System.out.println("Bear started eating honey");
                    Thread.sleep((int)(Math.random() * 5000));
                    pot.clear();
                    System.out.println("Bear ate all the honey");
                }
            }
            catch(InterruptedException ex) {
                System.out.println("Interrupted");
            }
        });

        for(int i = 0; i < 100; ++i) {
            bees[i] = new Thread(new Bee(pot, i));
        }

        bear.start();

        for(Thread tt : bees) {
            tt.start();
        }
    }
}