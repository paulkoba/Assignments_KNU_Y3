import java.util.Optional;

public class Scheduler {
    private Forest forest;
    private int rowToSchedule, columnToSchedule;

    public Scheduler(Forest forest) {
        this.forest = forest;

        rowToSchedule = 0;
        columnToSchedule = -1;
    }

    public synchronized Optional<Point2d> tryScheduleTask() {
        if(rowToSchedule == forest.rows) return Optional.empty();

        ++columnToSchedule;
        if(columnToSchedule == forest.columns) {
            columnToSchedule = 0;
            ++rowToSchedule;
        }

        return rowToSchedule < forest.rows ? Optional.of(new Point2d(rowToSchedule, columnToSchedule)) : Optional.empty();
    }
}