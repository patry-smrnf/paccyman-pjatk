package Game.PropsLogic;

import Game.Props.Player;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EnemyThread extends Thread {
    private final PropsLogic enemyManager;
    private final JPanel[][] cells;
    private final java.util.concurrent.atomic.AtomicBoolean running = new java.util.concurrent.atomic.AtomicBoolean(true);
    private int delayMillis; // np. 400 ms
    private static EnemyThread instance;

    private boolean paused = false;  // flaga, czy wrogowie mają być zatrzymani


    public EnemyThread(PropsLogic enemyManager, JPanel[][] cells, int delayMillis) {
        this.enemyManager = enemyManager;
        this.cells = cells;
        this.delayMillis = delayMillis;
        setName("EnemyThread");
        setDaemon(true);
        instance = this;
    }
    public static EnemyThread getInstance() {
        return instance;
    }

    public void pauseMovement() {
        paused = true;
    }

    public void resumeMovement() {
        paused = false;
    }

    public void setDelay(int newDelay) {
        this.delayMillis = newDelay;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            long start = System.currentTimeMillis();

            if (!paused) {
                SwingUtilities.invokeLater(() -> enemyManager.moveEnemiesTowardPlayer());
            }

            long elapsed = System.currentTimeMillis() - start;
            long sleepTime = delayMillis - elapsed;
            if (sleepTime < 0) sleepTime = 5;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
    public void terminate() {
        running.set(false);
        this.interrupt();
    }
}
