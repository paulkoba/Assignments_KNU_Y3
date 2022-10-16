import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Customer {
    public int idx;

    public Customer(int idx) {
        this.idx = idx;
    }
}

class Barber implements Runnable {
    private static BlockingQueue<Customer> customers = new ArrayBlockingQueue<Customer>(10);

    public Barber() {}

    public void provideService(Customer customer) {
        try {
            customers.put(customer);
        }
        catch(InterruptedException ex) {
            System.out.println("Interrupted");
        }
    }

    @Override
    public void run() {
        try {
            while(true) {
                Customer customer = customers.take();
                System.out.println("Servicing customer #" + customer.idx);
                Thread.sleep((int)(Math.random() * 2500 + 2500));
                System.out.println("Done servicing customer #" + customer.idx);
            }
        }
        catch(InterruptedException ex) {
            System.out.println("Interrupted");
        }

    }
}

class Main {
    public static void main(String[] args) {
        Barber barber = new Barber();
        int idx = 0;

        Thread th = new Thread(barber);
        th.start();

        try {
            while(true) {
                Customer customer = new Customer(idx++);
                System.out.println("Customer #" + customer.idx + " came");
                barber.provideService(customer);
                Thread.sleep((int)(Math.random() * 5000 + 2500));
            }
        }
        catch(InterruptedException ex) {
            System.out.println("Interrupted");
        }
    }
}