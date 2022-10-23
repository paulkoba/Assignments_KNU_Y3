import java.io.FileOutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Garden implements Runnable {
    public int[][] state; // 0 - normal crop, 1 - crop needs service
    public int n;
    public final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public Garden(int n) {
        this.n = n;
        state = new int[n][n];
    }    

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            Lock lock = rwLock.writeLock();
            lock.lock();

            int i = (int)(Math.random() * n);
            int j = (int)(Math.random() * n);

            System.out.println("(" + i + ", " + j + ") faded.");
            state[i][j] = 1;

            lock.unlock();
        }   
    }
}

class Gardener implements Runnable {
    Garden garden;

    public Gardener(Garden garden) {
        this.garden = garden;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Lock lock = garden.rwLock.writeLock();
            lock.lock();

            for(int i = 0; i < garden.n; ++i) {
                for(int j = 0; j < garden.n; ++j) {
                    if(garden.state[i][j] == 1) {
                        System.out.println("Gardener serviced (" + i + ", " + j + ").");
                        garden.state[i][j] = 0;
                    }
                }
            }

            lock.unlock();
        }   
    }
}

class ConsoleWriter implements Runnable {
    Garden garden;

    public ConsoleWriter(Garden garden) {
        this.garden = garden;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(3333);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Lock lock = garden.rwLock.readLock();
            lock.lock();

            for(int i = 0; i < garden.n; ++i) {
                for(int j = 0; j < garden.n; ++j) {
                    System.out.print(garden.state[i][j] + " ");
                }
                System.out.println();
            }

            lock.unlock();
        }
    }
}

class FileWriter implements Runnable {
    Garden garden;
    String filename;

    public FileWriter(Garden garden, String filename) {
        this.garden = garden;
        this.filename = filename;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(6666);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                Lock lock = garden.rwLock.readLock();
                lock.lock();

                FileOutputStream fileOutputStream = new FileOutputStream(filename, true);
                StringBuilder toWrite = new StringBuilder();
                for(int i = 0; i < garden.n; ++i) {
                    for(int j = 0; j < garden.n; ++j) {
                        toWrite.append(garden.state[i][j] + " ");
                    }
                    toWrite.append("\n");
                }
                fileOutputStream.write(toWrite.toString().getBytes());
                fileOutputStream.close();
                lock.unlock();
            }
            catch(Exception ex) {
                System.out.println("Exception");
            }
        }
    }
}

class Main {
    public static void main(String[] args) {
        Garden garden = new Garden(10);
        Thread gardenThread = new Thread(garden);
        Thread gardener = new Thread(new Gardener(garden));
        Thread consoleWriter = new Thread(new ConsoleWriter(garden));
        Thread fileWriter = new Thread(new FileWriter(garden, "Database4.2"));

        gardenThread.start();
        gardener.start();
        consoleWriter.start();
        fileWriter.start();

        while(true) {}
    }
}