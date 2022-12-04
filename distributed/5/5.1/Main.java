class CyclicBarrier  {
    private int waitingParties = 0;
    private final int trippingParties;

    public CyclicBarrier(int trippingParties) {
        this.trippingParties = trippingParties;
    }

    public synchronized void await() throws InterruptedException {
        ++waitingParties;

        if(waitingParties != trippingParties) {
            wait();
            return;
        }

        waitingParties = 0;

        notifyAll();
    }
}

class WorkerThread implements Runnable {
    private final CyclicBarrier barrier;
    private final StringBuilder builder;
    private int leftIdx;
    private int rightIdx;

    public WorkerThread(CyclicBarrier barrier, StringBuilder builder, int leftIdx, int rightIdx) {
        this.barrier = barrier;
        this.builder = builder;
        this.leftIdx = leftIdx;
        this.rightIdx = rightIdx;
    }

    @Override
    public void run() {
        try {
            while(true) {
                barrier.await();
    
                for(int i = leftIdx; i < rightIdx; ++i) {
                    if(builder.charAt(i) == 'L') {
                        if(i == 0) {
                            continue;
                        }

                        if(builder.charAt(i - 1) != 'L') {
                            builder.setCharAt(i, 'R');
                            builder.setCharAt(i - 1, 'L');
                            ++i;
                        }
                    } else {
                        if(i == builder.length() - 1) {
                            continue;
                        }

                        if(builder.charAt(i + 1) != 'R') {
                            builder.setCharAt(i, 'L');
                            builder.setCharAt(i + 1, 'R');
                            ++i;
                        }
                    }
                }

                barrier.await();
            }
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Main{

    public static void main(String[] args) throws InterruptedException {
        CyclicBarrier barrier = new CyclicBarrier(4);
        StringBuilder builder = new StringBuilder(150);

        for(int i = 0; i < 150; ++i) builder.append(Math.random() > 0.5 ? 'R' : 'L');

        for(int i = 0; i < 3; ++i) {
            new Thread(new WorkerThread(barrier, builder, i * 50, (i + 1) * 50)).start();
        }

        String cp = builder.toString();

        while(true) {
            barrier.await();

            //if(builder.toString().equals(cp)) return;
            System.out.println(cp);

            cp = builder.toString();
            Thread.sleep(100);
            barrier.await();
        }
    }
}