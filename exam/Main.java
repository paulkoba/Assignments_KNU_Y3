import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Account {
    int balance = 0; // Can be negative

    public Account(int balance) {
        this.balance = balance;
    }
}

class CashRegister {
    public int idx = 0;
    private int cashAmount = 50;
    private int capacity = 100;
    private int maxAllowedTakeout = 50;

    private int notifyThresholdHigh = 85;
    private int notifyThresholdLow = 15;

    private Overseer overseer;

    public CashRegister(Overseer overseer, int idx) {
        this.overseer = overseer;
        this.idx = idx;
    }

    public int getCash() {
        return cashAmount;
    }

    public boolean takeCash(int amount) {
        if(maxAllowedTakeout < amount) {
            return false;
        }

        if(amount > cashAmount || cashAmount - amount < notifyThresholdLow) {
            overseer.notifyNeedsService(this);
        }

        if(cashAmount >= amount) {
            cashAmount -= amount;
            return true;
        } else {
            return false;
        }
    }

    public boolean addCash(int amount) {
        if(maxAllowedTakeout < amount) {
            return false;
        }

        if(amount > cashAmount || cashAmount + amount > notifyThresholdHigh) {
            overseer.notifyNeedsService(this);
        }

        if(cashAmount + amount > capacity) {
            return false;
        }

        cashAmount += amount;

        return true;
    }

    public boolean setCash(int amount) {
        if(amount < 0 || amount > maxAllowedTakeout) {
            return false;
        }

        cashAmount = amount;

        return true;
    }
}

class Customer {
    public Account account;

    public Customer(Account account) {
        this.account = account;
    }
}

class Cashier implements Runnable {
    private CashRegister register;
    private static BlockingQueue<Customer> customerQueue = new ArrayBlockingQueue<Customer>(10);

    public Cashier(CashRegister register) {
        this.register = register;
    }

    public CashRegister getRegistry() {
        return register;
    }

    public void service(Customer customer) {
        try {
            customerQueue.put(customer);
        }
        catch(InterruptedException ex) {
            System.out.println("Interrupted");
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                Customer customer = customerQueue.take();

                Account acc = customer.account;

                // This emulates customers doing different things
                int action = (int) (Math.random() * 2);
                int amount = (int) (Math.random() * 10);
                switch(action) {
                case 0: {
                    boolean result = register.takeCash(amount);
                    if(!result) {
                        System.out.println("Not enough cash in the registry. Transaction cancelled.");
                    } else {
                        acc.balance -= amount;
                    }
                    
                    break;
                }
                case 1: {
                    boolean result = register.addCash(amount);
                    if(!result) {
                        System.out.println("Not enough space in the registry. Transaction cancelled.");
                    } else {
                        acc.balance += amount;
                    }
                
                    break;
                }
                default:
                    System.out.println("Unreachable");
                    break;
                }
            }
            catch(InterruptedException ex) {
                System.out.println("Interrupted");
            }
        }
    }
}

class Overseer implements Runnable {
    private static BlockingQueue<CashRegister> needsService = new ArrayBlockingQueue<CashRegister>(10);

    public void notifyNeedsService(CashRegister register) {        
        try {
            needsService.put(register);
        }
        catch(InterruptedException ex) {
            System.out.println("Interrupted");
        }
    }

    @Override
    public void run() {
        while(true) {
            try {
                CashRegister register = needsService.take();
                int amount = register.getCash();
                System.out.println("Cash registry " + register.idx + " needed service. It had " + amount + " dollars");
                
                register.setCash(50);
            }
            catch(InterruptedException ex) {
                System.out.println("Interrupted");
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Overseer overseer = new Overseer();
        Thread overseerThread = new Thread(overseer);
        overseerThread.start();

        CashRegister[] registers = new CashRegister[10];
        Cashier[] cashiers = new Cashier[10];
        Thread[] cashierThreads = new Thread[10];

        for(int i = 0; i < 10; ++i) {
            registers[i] = new CashRegister(overseer, i);
            cashiers[i] = new Cashier(registers[i]);
            cashierThreads[i] = new Thread(cashiers[i]);
            cashierThreads[i].start();
        }

        while(true) {
            Cashier chosen = cashiers[(int)(Math.random() * 10)];
            chosen.service(new Customer(new Account((int)(Math.random() * 100))));

            try {
                Thread.sleep(10);
            }
            catch(InterruptedException ex) {
                System.out.println("Interrupted");
            }
        }
    }
}