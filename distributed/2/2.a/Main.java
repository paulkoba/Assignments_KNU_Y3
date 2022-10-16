public class Main {
    public static void main(String[] args) {
        Forest forest = new Forest(5, 3);
        Scheduler scheduler = new Scheduler(forest);

        Thread[] threads = new Thread[4];

        for(int i = 0; i < 4; ++i) {
            threads[i] = new Thread(new Hive(forest, scheduler, i));
        }

        for(Thread thread : threads) {
            thread.start();
        }
    }
}