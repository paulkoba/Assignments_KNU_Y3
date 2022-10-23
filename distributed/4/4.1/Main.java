import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Lock {
    private int readers = 0;
    private int writers = 0;
    private int writeRequests = 0;

    public synchronized void readLock() {
        while(writers > 0 && writeRequests > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
        ++readers;
    }

    public synchronized void readUnlock() {
        --readers;
        notifyAll();
    }

    public synchronized void writeLock() {
        ++writeRequests;
        while(readers > 0 || writers > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Interrupted");
            }
        }
        --writeRequests;
        ++writers;
    }

    public synchronized void writeUnlock() {
        --writers;
        notifyAll();
    }
}

class Record implements Serializable {
    public final String name;
    public final String phone;

    Record(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    @Override
    public String toString() {
        return name + " " + phone;
    }
}

class FileManager {
    private Lock lock;
    private String filename;

    public FileManager(Lock lock, String filename) {
        this.lock = lock;
        this.filename = filename;
    }

    public ArrayList<Record> getRecords() {
        try {
            lock.readLock();
            ArrayList<Record> records = new ArrayList<>();
            FileInputStream fileInputStream = new FileInputStream(filename);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            records = (ArrayList<Record>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();

            lock.readUnlock();

            return records;
        }
        catch(FileNotFoundException ex) {
            System.out.println("File not found");
        }
        catch(IOException ex) {
            System.out.println("IO exception");
        }
        catch(ClassNotFoundException rc) {
            System.out.println("Class not found");
        }
        return new ArrayList<>();
    }

    public void setRecords(ArrayList<Record> records) {
        try {
            lock.writeLock();

            FileOutputStream fileOutputStream = new FileOutputStream(filename, false);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(records);
            objectOutputStream.close();
            fileOutputStream.close();
            lock.writeUnlock();
        }
        catch(FileNotFoundException ex) {
            System.out.println("File not found");
        }
        catch(IOException ex) {
            System.out.println("IO exception");
        }
    }

    public void setRecords(List<Record> records) {
        setRecords(new ArrayList<>(records));
    }
}

class Main {
    public static void main(String[] args) {
        Lock rwLock = new Lock();
        FileManager manager = new FileManager(rwLock, "Database4.1");
        manager.setRecords(Arrays.asList(
            new Record("John Doe", "+380966666666"),
            new Record("Jane Doe", "+380966666777"),
            new Record("James Smith", "+380986666777"),
            new Record("Karl Connan", "+380961234567")
        ));

        while(true) {
            int rand = (int) (Math.random() * 5);
            Thread th;
            switch(rand) {
                case 0:
                    th = new Thread(() -> {
                        ArrayList<Record> records = manager.getRecords();
                        List<Record> filtered = records.stream().filter(el -> el.name.equals("John Doe")).collect(Collectors.toList());

                        System.out.println("People with name John Doe: ");
                        for(Record el : filtered) {
                            System.out.println(el);
                        }
                    });
                    break;
                case 1:
                    th = new Thread(() -> {
                        ArrayList<Record> records = manager.getRecords();
                        List<Record> filtered = records.stream().filter(el -> el.phone.equals("+380986666777")).collect(Collectors.toList());

                        System.out.println("People with phone +380986666777: ");
                        for(Record el : filtered) {
                            System.out.println(el);
                        }
                    });
                    break;
                case 2:
                    th = new Thread(() -> {
                        ArrayList<Record> records = manager.getRecords();

                        System.out.println("All people: ");
                        for(Record el : records) {
                            System.out.println(el);
                        }
                    });
                    break;
                case 3:
                    th = new Thread(() -> {
                        ArrayList<Record> records = manager.getRecords();

                        records.add(new Record("Some other guy", "+380000000000"));
                        System.out.println("Added some other guy.");
                        manager.setRecords(records);
                    });
                    break;
                case 4:
                    th = new Thread(() -> {
                        ArrayList<Record> records = manager.getRecords();
                        List<Record> filtered = records.stream().filter(el -> !el.name.equals("Some other guy")).collect(Collectors.toList());

                        System.out.println("Remove ppl with name \"Some other guy\"");
                        manager.setRecords(filtered);
                    });
                    break;
                default:
                    System.out.println("Unexpected");
                    return;
            }
            th.start();
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException ex) {
                System.out.println("Interrupted");
            }
        }
    }
}