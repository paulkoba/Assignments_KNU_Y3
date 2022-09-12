import javax.swing.*;
import java.util.concurrent.Semaphore;

public class Solution_1B {
    private static TThread thread1;
    private static TThread thread2;
    public static void main(String[] args) {
        JFrame window = new JFrame("Task 1B");
        window.setLayout(null);
        window.setSize(800, 600);

        JSlider slider = new JSlider(0,100,50);
        slider.setBounds(100, 50, 200, 100);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(10);
        slider.setPaintLabels(true);

        JButton start1 = new JButton("Start #1");
        JButton start2 = new JButton("Start #2");
        JButton stop1 = new JButton("Stop #1");
        JButton stop2 = new JButton("Stop #2");

        start1.setBounds(100, 150, 100, 100);
        start2.setBounds(200, 150, 100, 100);
        stop1.setBounds(100, 250, 100, 100);
        stop2.setBounds(200, 250, 100, 100);

        Semaphore semaphore = new Semaphore(1);

        start1.addActionListener(e -> {
            boolean acquired = semaphore.tryAcquire();

            if(acquired) {
                thread1 = new TThread(slider, 10);
                thread1.setPriority(Thread.MIN_PRIORITY);
                thread1.start();
            } else {
                JOptionPane.showMessageDialog(null, "Can't acquire semaphore.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        start2.addActionListener(e -> {
            boolean acquired = semaphore.tryAcquire();

            if(acquired) {
                thread2 = new TThread(slider, 90);
                thread1.setPriority(Thread.MAX_PRIORITY);
                thread2.start();
            } else {
                JOptionPane.showMessageDialog(null, "Can't acquire semaphore.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        stop1.addActionListener(e -> {
            if(thread1 != null && thread1.isAlive()) {
                thread1.terminate();
                semaphore.release();
            }
        });

        stop2.addActionListener(e -> {
            if(thread2 != null && thread2.isAlive()) {
                thread2.terminate();
                semaphore.release();
            }
        });

        window.add(slider);
        window.add(start1);
        window.add(start2);
        window.add(stop1);
        window.add(stop2);

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
} 

class TThread extends Thread {
    private volatile boolean shouldTerminate = false;
    private JSlider slider;
    private int target;

    TThread(JSlider sl, int targetPos) {
        target = targetPos;
        slider = sl;
    }

    @Override
    public void run() {
        while(!shouldTerminate) {
            synchronized(slider) {
                int val = slider.getValue();
                if(val == target) continue;
                
                if(val < target) {
                    slider.setValue(val + 1);
                } else {
                    slider.setValue(val - 1);
                }
            }
        }
    }

    public void terminate() {
        shouldTerminate = true;
    }
}