import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

class WorkerThread implements Runnable {
    private final CyclicBarrier barrier;
    private final StringBuilder s;

    private void changeRandomChar() {
        var idx = (int)(Math.random() * s.length());

        switch(s.charAt(idx)) {
            case 'A':
                s.setCharAt(idx, 'C');
                break;
            case 'B':
                s.setCharAt(idx, 'D');
                break;
            case 'C':
                s.setCharAt(idx, 'A');
                break;
            case 'D':
                s.setCharAt(idx, 'B');
                break;
            default:
                System.out.println("Unexpected character " + s.charAt(idx));
        }
    }

    public WorkerThread(CyclicBarrier barrier, StringBuilder s) {
        this.barrier = barrier;
        this.s = s;
    }

    @Override
    public void run() {
        try {
            while(true) {
                barrier.await();
                changeRandomChar();
                barrier.await();
            }
        } 
        catch(InterruptedException | BrokenBarrierException ex) {
            ex.printStackTrace();
        }
    }
}

public class Main {

    static StringBuilder[] s = new StringBuilder[4];

    private static int countMatches(String str, char ch) {
        return str.split(String.valueOf(ch),-1).length - 1;
    }
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        s[0] = new StringBuilder("ACCBBB");
        s[1] = new StringBuilder("ACCDDD");
        s[2] = new StringBuilder("CCCBBD");
        s[3] = new StringBuilder("ACCDDB");

        CyclicBarrier barrier = new CyclicBarrier(5);
        new Thread(new WorkerThread(barrier, s[0])).start();
        new Thread(new WorkerThread(barrier, s[1])).start();
        new Thread(new WorkerThread(barrier, s[2])).start();
        new Thread(new WorkerThread(barrier, s[3])).start();

        while(true) {
            barrier.await();
            int counts = 0;
        
            for(int i = 0; i < 4; ++i) {
                System.out.println(s[i].toString());
                counts += countMatches(s[i].toString(), 'A') == countMatches(s[i].toString(), 'B') ? 1 : 0;
            }

            if(counts == 3) {
                System.out.println("Condition achieved");
                return;
            }
        }
    }
}