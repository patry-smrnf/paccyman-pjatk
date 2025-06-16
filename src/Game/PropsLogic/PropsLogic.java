package Game.PropsLogic;

import Config.AppConfig;
import Game.GameBoard;
import Game.Props.Enemy;
import Game.Props.Player;
import Game.Props.PowerUp;
import Game.ThreadMenager;
import Windows.GameWindow;
import Windows.MainMenuWindow;
import Windows.SaveGameWindow;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.*;

public class PropsLogic {
    private List<Enemy> enemies;
    private JPanel[][] cells;
    private final Player player;

    private final List<PowerUp> powerUps = new ArrayList<>();
    private static PropsLogic instance;

    private final JLabel actualPowerLabel;

    // Replace Swing Timer with a dedicated Thread:
    private Thread powerUpThread;
    private int remainingSeconds;

    private final GameBoard board;
    private final GameWindow parentWindow;

    public PropsLogic(Enemy[] enemies, JPanel[][] cells, Player player, JLabel actualPowerLabel, GameBoard board, GameWindow parentWindow) {
        this.enemies = new ArrayList<>(Arrays.asList(enemies));
        this.cells = cells;
        this.player = player;
        this.actualPowerLabel = actualPowerLabel;

        this.board = board;
        this.parentWindow = parentWindow;

        instance = this;
    }

    public static PropsLogic getInstance() {
        return instance;
    }

    public static void registerNewPowerUp(PowerUp pu) {
        if (instance != null) {
            instance.powerUps.add(pu);
        }
    }

    public void moveEnemiesTowardPlayer() {
        for (var enemy : enemies) {
            int ex = enemy.getX();
            int ey = enemy.getY();
            int px = player.getX();
            int py = player.getY();

            int bestX = ex;
            int bestY = ey;
            var bestDistance = Double.MAX_VALUE;

            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) {
                        continue;
                    }

                    int newX = ex + dx;
                    int newY = ey + dy;

                    if (canMoveTo(newX, newY) && !isCellOccupiedByEnemy(newX, newY)) {
                        double distance = distance(newX, newY, px, py);
                        if (distance < bestDistance) {
                            bestDistance = distance;
                            bestX = newX;
                            bestY = newY;
                        }
                    }
                }
            }

            if (bestX != ex || bestY != ey) {
                enemy.moveTo(bestX, bestY, cells);
            }

            // Check collision with player
            if (enemy.getX() == player.getX() && enemy.getY() == player.getY()) {
                System.out.println("Game Over!");
                SwingUtilities.invokeLater(() -> {
                    int actualScore = board.get_score();
                    String actualTime = board.get_time();
                    ThreadMenager.killAll();

                    if (parentWindow != null) {
                        parentWindow.dispose();
                    }
                    new SaveGameWindow(actualScore, actualTime);
                });
            }
        }
        checkPlayerPowerUpCollision();
    }

    public void checkPlayerPowerUpCollision() {
        int px = player.getX();
        int py = player.getY();

        Component[] comps = cells[py][px].getComponents();
        PowerUp collided = null;
        for (var comp : comps) {
            if (comp instanceof JLabel && "PowerUP".equals(comp.getName())) {
                for (PowerUp pu : powerUps) {
                    if (pu.getX() == px && pu.getY() == py) {
                        collided = pu;
                        break;
                    }
                }
                break;
            }
        }
        if (collided != null) {
            // Remove icon from board
            cells[py][px].remove(collided.getLabel());
            cells[py][px].revalidate();
            cells[py][px].repaint();

            // Apply effect
            applyPowerUpEffect(collided);

            // Remove from active list
            powerUps.remove(collided);
        }
    }

    private void applyPowerUpEffect(PowerUp pu) {
        if (powerUpThread != null && powerUpThread.isAlive()) {
            powerUpThread.interrupt();
        }
        this.actualPowerLabel.setForeground(Color.GREEN);
        switch (pu.type) {
            case STOP_ENEMIES -> {
                remainingSeconds = PowerUp.STOP_time;
                actualPowerLabel.setText("Actual Power: STOP_ENEMIES (" + remainingSeconds + "s)");
                System.out.println("[+] Player wziął STOP_ENEMIES");

                pauseAllEnemies();
                powerUpThread = new Thread(() -> {
                    try {
                        while (remainingSeconds > 0) {
                            Thread.sleep(1000);
                            remainingSeconds--;

                            final int secs = remainingSeconds;
                            SwingUtilities.invokeLater(() -> {
                                if (secs > 0) {
                                    actualPowerLabel.setText("Actual Power: STOP_ENEMIES (" + secs + "s)");
                                }
                            });
                        }
                        SwingUtilities.invokeLater(() -> {
                            resumeAllEnemies();
                            actualPowerLabel.setForeground(Color.RED);
                            actualPowerLabel.setText("Actual Power: none");
                        });
                    } catch (InterruptedException ex) {
                        // If interrupted (e.g. a new power-up picked up), make sure to clear
                        // any visual state if needed
                        SwingUtilities.invokeLater(() -> {
                            resumeAllEnemies();
                            actualPowerLabel.setForeground(Color.RED);
                            actualPowerLabel.setText("Actual Power: none");
                        });
                    }
                }, "PowerUp-STOP-Thread");
                powerUpThread.setDaemon(true);
                powerUpThread.start();
            }

            case ENEMIES_SLOWER -> {
                remainingSeconds = PowerUp.ENEMIES_SLOWER;
                actualPowerLabel.setText("Actual Power: ENEMIES_SLOWER (" + remainingSeconds + "s)");
                System.out.println("[+] Player wziął ENEMIES_SLOWER");

                resumeAllEnemies(); // make sure enemies are moving before we slow them
                slowDownAllEnemies();

                powerUpThread = new Thread(() -> {
                    try {
                        while (remainingSeconds > 0) {
                            Thread.sleep(1000);
                            remainingSeconds--;

                            final int secs = remainingSeconds;
                            SwingUtilities.invokeLater(() -> {
                                if (secs > 0) {
                                    actualPowerLabel.setText("Actual Power: ENEMIES_SLOWER (" + secs + "s)");
                                }
                            });
                        }
                        SwingUtilities.invokeLater(() -> {
                            restoreEnemySpeed();
                            actualPowerLabel.setForeground(Color.RED);
                            actualPowerLabel.setText("Actual Power: none");
                        });
                    } catch (InterruptedException ex) {
                        SwingUtilities.invokeLater(() -> {
                            restoreEnemySpeed();
                            actualPowerLabel.setForeground(Color.RED);
                            actualPowerLabel.setText("Actual Power: none");
                        });
                    }
                }, "PowerUp-SLOWER-Thread");
                powerUpThread.setDaemon(true);
                powerUpThread.start();
            }

            case ENEMIES_FASTER -> {
                remainingSeconds = PowerUp.ENEMIES_FASTER;
                actualPowerLabel.setText("Actual Power: ENEMIES_FASTER (" + remainingSeconds + "s)");
                System.out.println("[+] Player wziął ENEMIES_FASTER");

                resumeAllEnemies(); // ensure they are moving, then speed them up
                speedUpAllEnemies();

                powerUpThread = new Thread(() -> {
                    try {
                        while (remainingSeconds > 0) {
                            Thread.sleep(1000);
                            remainingSeconds--;

                            final int secs = remainingSeconds;
                            SwingUtilities.invokeLater(() -> {
                                if (secs > 0) {
                                    actualPowerLabel.setText("Actual Power: ENEMIES_FASTER (" + secs + "s)");
                                }
                            });
                        }
                        SwingUtilities.invokeLater(() -> {
                            restoreEnemySpeed();
                            actualPowerLabel.setForeground(Color.RED);
                            actualPowerLabel.setText("Actual Power: none");
                        });
                    } catch (InterruptedException ex) {
                        SwingUtilities.invokeLater(() -> {
                            restoreEnemySpeed();
                            actualPowerLabel.setForeground(Color.RED);
                            actualPowerLabel.setText("Actual Power: none");
                        });
                    }
                }, "PowerUp-FASTER-Thread");
                powerUpThread.setDaemon(true);
                powerUpThread.start();
            }

            case KILL_ENEMY -> {
                actualPowerLabel.setText("Actual Power: KILL_ENEMY");
                System.out.println("[+] Player wziął KILL_ENEMY");

                killRandomEnemy();
                resumeAllEnemies();

                SwingUtilities.invokeLater(() -> actualPowerLabel.setText("Actual Power: KILL_ENEMY"));
            }

            case PLAYER_DESTROY_ONE_WALL -> {
                actualPowerLabel.setText("Actual Power: PLAYER_DESTROY_ONE_WALL");
                System.out.println("[+] Player wziął PLAYER_DESTROY_ONE_WALL");

                playerDestroyOneWall();
                resumeAllEnemies();

                SwingUtilities.invokeLater(() -> actualPowerLabel.setText("Actual Power: PLAYER_DESTROY_ONE_WALL"));
            }
        }
    }

    private void pauseAllEnemies() {
        EnemyThread.getInstance().pauseMovement();
    }

    private void resumeAllEnemies() {
        EnemyThread.getInstance().resumeMovement();
    }

    private void slowDownAllEnemies() {
        EnemyThread.getInstance().setDelay(PowerUp.ENEMIES_SLOWER_SPEED);
    }

    private void speedUpAllEnemies() {
        EnemyThread.getInstance().setDelay(PowerUp.ENEMIES_FASTER_SPEED);
    }

    private void restoreEnemySpeed() {
        EnemyThread.getInstance().setDelay(PowerUp.ENEMIES_DEFAUL_SPEED);
    }

    private void killRandomEnemy() {
        if (enemies.isEmpty() || enemies.size() < 2) return;

        int idx = new java.util.Random().nextInt(enemies.size());
        Enemy toKill = enemies.get(idx);

        int ex = toKill.getX();
        int ey = toKill.getY();
        cells[ey][ex].remove(toKill.getLabel());
        cells[ey][ex].revalidate();
        cells[ey][ex].repaint();

        enemies.remove(idx);
    }

    private void playerDestroyOneWall() {
        player.can_destroy_wall = true;
    }

    private boolean canMoveTo(int x, int y) {
        if (x < 0 || y < 0 || y >= cells.length || x >= cells[0].length) {
            return false;
        }
        return !cells[y][x].getBackground().equals(AppConfig.walls_color);
    }

    private boolean isCellOccupiedByEnemy(int x, int y) {
        for (Enemy enemy : enemies) {
            if (enemy.getX() == x && enemy.getY() == y) return true;
        }
        return false;
    }

    private double distance(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
}
