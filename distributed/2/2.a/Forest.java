public class Forest {
    public final int rows, columns;
    public final int bearRow, bearColumn;

    public Forest(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;

        bearRow = (int)(Math.random() * (rows - 1));
        bearColumn = (int)(Math.random() * (columns - 1));
    }

    boolean sweep(int row, int column) {
        try {
            Thread.sleep((int)(Math.random() * (100)));
        }
        catch(InterruptedException ex) {
            System.out.println("Interrupted");
        }

        return row == bearRow && column == bearColumn;
    }
}
