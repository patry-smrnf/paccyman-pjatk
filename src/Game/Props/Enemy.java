package Game.Props;

import Game.PropsLogic.PropsLogic;
import Game.ThreadMenager;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Enemy extends Prop implements Runnable {

    private final Random random;
    private final JPanel[][] cells;
    private final Thread dropThread;

    public Enemy(int startX, int startY, JPanel[][] cells) {
        super(startX, startY, PropsAssets.ICON_enemy_path, PropsAssets.ICON_enemy2_path, "Enemy");
        this.random = new Random();
        this.cells = cells;

        int cellW = cells[startY][startX].getWidth();
        int cellH = cells[startY][startX].getHeight();
        this.setImageForSize(cellW, cellH);
        cells[startY][startX].add(this.getLabel(), BorderLayout.CENTER);
        cells[startY][startX].setComponentZOrder(this.getLabel(), 0);

        dropThread = new Thread(this, "Enemy-PowerUp-Dropper");
        dropThread.setDaemon(true);
        ThreadMenager.register(dropThread);
        dropThread.start();
    }

    public void moveTo(int newX, int newY, JPanel[][] cells) {
        int oldX = this.getX();
        int oldY = this.getY();

        cells[oldY][oldX].remove(this.getLabel());
        cells[oldY][oldX].revalidate();
        cells[oldY][oldX].repaint();

        this.toggleFrame();

        this.setX(newX);
        this.setY(newY);

        int cellW = cells[newY][newX].getWidth();
        int cellH = cells[newY][newX].getHeight();
        this.setImageForSize(cellW, cellH);

        cells[newY][newX].add(this.getLabel(), BorderLayout.CENTER);
        cells[newY][newX].setComponentZOrder(this.getLabel(), 0);

        cells[newY][newX].revalidate();
        cells[newY][newX].repaint();
    }
    public void run() {
        try {
            while (true) {
                Thread.sleep(5000); // czekamy 5 sekund

                // 25% szansy na stworzenie PowerUpa
                if (random.nextFloat() < 0.25f) {
                    final int currentX = this.getX();
                    final int currentY = this.getY();

                    SwingUtilities.invokeLater(() -> leavePowerUpAt(currentX, currentY));
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void leavePowerUpAt(int x, int y) {
        // Sprawdzamy, czy współrzędne są nadal aktualne na planszy (opcjonalnie – w razie śmierci/ usunięcia Enemy)
        if (x < 0 || y < 0 || y >= cells.length || x >= cells[0].length) {
            return;
        }

        PowerUp pu = new PowerUp(x, y);

        int cellW = cells[y][x].getWidth();
        int cellH = cells[y][x].getHeight();
        pu.setImageForSize(cellW, cellH);

        cells[y][x].add(pu.getLabel(), BorderLayout.CENTER);
        cells[y][x].setComponentZOrder(pu.getLabel(), 0);
        cells[y][x].revalidate();
        cells[y][x].repaint();

        PropsLogic.registerNewPowerUp(pu);
    }
}
