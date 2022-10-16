import java.util.Optional;

public class Hive implements Runnable {
    private final Forest forest;
    private final Scheduler scheduler;
    private int idx;
    Hive(Forest forest, Scheduler scheduler, int idx) {
        this.forest = forest;
        this.scheduler = scheduler;
        this.idx = idx;
    }

    @Override
    public void run() {
        while(true) {
            Optional<Point2d> task = scheduler.tryScheduleTask();
            if(task.isEmpty()) {
                System.out.println("No work left to do. Hive " + idx + " stops.");
                break;
            }
            
            Point2d r = task.get();
            
            boolean result = forest.sweep(r.x, r.y);
            if(result) {
                System.out.println("Hive " + idx + " scanned (" + r.x + ", " + r.y + "); found the bear");
            } else {
                System.out.println("Hive " + idx + " scanned (" + r.x + ", " + r.y + "); found nothing");
            }
        }
    }
}