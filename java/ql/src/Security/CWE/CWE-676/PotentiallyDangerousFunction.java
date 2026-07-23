public class PotentiallyDangerousFunction implements Runnable {

    private void repaint() {
    }

    private volatile Thread blinker;

    public void stop() {
        blinker = null;
    }

    public void run() {
        Thread thisThread = Thread.currentThread();
        while (blinker == thisThread) {
            try {
                int interval = 1000;
                Thread.sleep(interval);
            } catch (InterruptedException e) {
            }
            repaint();
        }
    }
}
