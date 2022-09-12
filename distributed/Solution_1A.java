import javax.swing.*;

public class Solution_1A {
    private static TThread thread1;
    private static TThread thread2;

    public static void main(String[] args) {
        JFrame window = new JFrame("Task 1A");
        window.setLayout(null);
        window.setSize(800, 600);

        JSlider slider = new JSlider(0,100,50);

        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(10);
        slider.setPaintLabels(true);

        JButton btn = new JButton("Start");

        btn.addActionListener(e -> {
            thread1.start();
            thread2.start();
            btn.setEnabled(false);
        });

        SpinnerModel spinnerModel1 = new SpinnerNumberModel(5, 1, 10, 1);
        SpinnerModel spinnerModel2 = new SpinnerNumberModel(5, 1, 10, 1);

        JSpinner spinner1 = new JSpinner(spinnerModel1);
        JSpinner spinner2 = new JSpinner(spinnerModel2);

        thread1 = new TThread(slider, 10);
        thread2 = new TThread(slider, 90);

        spinner1.addChangeListener(e -> {
            thread1.setPriority((int)spinner1.getValue());
        });

        spinner2.addChangeListener(e -> {
            thread2.setPriority((int)spinner2.getValue());
        });

        slider.setBounds(150, 0, 400, 100);
        spinner1.setBounds(150, 150, 150, 50);
        spinner2.setBounds(400, 150, 150, 50);
        btn.setBounds(300, 300, 100, 50);
        window.add(slider);
        window.add(spinner1);
        window.add(spinner2);
        window.add(btn);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
    }
}

class TThread extends Thread {
    private boolean shouldTerminate = false;
    private JSlider slider;
    private int target;

    TThread(JSlider sl, int targetPos) {
        target = targetPos;
        slider = sl;
    }

    @Override
    public void run() {
        while(!shouldTerminate) {
            yield();
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
}