package Game;

import Config.AppConfig;
import Game.Props.*;
import Game.PropsLogic.EnemyThread;
import Game.PropsLogic.PropsLogic;
import Game.Render.Chunk;
import Windows.GameWindow;
import Windows.SaveGameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyEvent;

public class GameBoard extends JPanel {

    private final int szerokosc_x, szerokosc_y;
    protected final JPanel[][] cells;
    private final Chunk[] chunks;

    private PropsLogic enemyManager;

    private Chunk spawn_pacman_chunk;
    private Chunk spawn_duchy_chunk;

    private Food[] foods;
    private final Player player;

    private int ammount_of_enemies;
    private Enemy[] enemies;
    private EnemyThread enemyThread;

    private GameWindow parentWindow;

    private final JLabel scoreLabel;
    private final JLabel actualPowerLabel;
    private final JLabel actualtimeLabel;


    public GameBoard(int szerokosc_x_od_user, int szerokosc_y_od_user,
                     JLabel scoreLabel, JLabel actualPowerLabel, JLabel actualtimeLabel,
                     GameWindow parentWindow)  {
        this.szerokosc_x = szerokosc_x_od_user+2;
        this.szerokosc_y = szerokosc_y_od_user+2;

        if(szerokosc_x_od_user >= 10 && szerokosc_y_od_user >= 10 && szerokosc_x_od_user <= 30 && szerokosc_y_od_user <= 30)  {
            System.out.println(2);
            this.ammount_of_enemies = 2;
        }
        if(szerokosc_x_od_user > 30 && szerokosc_y_od_user > 30 && szerokosc_x_od_user <= 80 && szerokosc_y_od_user <= 80)  {
            System.out.println(3);
            this.ammount_of_enemies = 3;
        }
        if(szerokosc_x_od_user > 80 && szerokosc_y_od_user > 80)  {
            System.out.println(4);
            this.ammount_of_enemies = 4;
        }

        this.scoreLabel = scoreLabel;
        this.actualPowerLabel = actualPowerLabel;
        this.actualtimeLabel = actualtimeLabel;
        this.parentWindow = parentWindow;

        //[!-- 1. Ustawianie grid
        this.setLayout(new GridLayout(this.szerokosc_x, this.szerokosc_y, 0, 0));
        cells = new JPanel[this.szerokosc_x][this.szerokosc_y];

        //[!-- 2. Tworzenie tla
        for (int r = 0; r < szerokosc_x; r++) {
            for (int c = 0; c < szerokosc_y; c++) {
                JPanel cell = new JPanel(new BorderLayout());
                cell.setBackground(AppConfig.background_color);
                cells[r][c] = cell;
            }
        }

        //[!-- 3. Rysowanie scian pobocznych
        BoardFeatures.draw_walls(cells);

        //[!-- 4. Generowanie chunkow
        this.chunks = BoardFeatures.generate_chunks(cells);
        System.out.printf(AppConfig.dev_mode ? "[!!!] Created %d chunks\n" : "", Chunk.counter_chunkow);

        //[!-- 5. Generowanie spawn pointow, oraz przypisanie ich
        Chunk[] spawns = Chunk.random_spawners(this.chunks);
        this.spawn_pacman_chunk = spawns[0];
        this.spawn_duchy_chunk = spawns[1];

        //[!-- 6. Rysowanie figur
        for(var i : this.chunks) {  DrawingStuff.DrawRandomFigure(cells, i); }

        //[!-- 7. Tworzenie obiektu jedzenia, ale bez ikon
        this.foods = BoardFeatures.spawn_food(cells);

        //[!-- 8. Dodawania jedzenia
        for(var i : this.foods) {
            cells[i.getY()][i.getX()].add(i.getLabel(), BorderLayout.CENTER);
            cells[i.getY()][i.getX()].setComponentZOrder(i.getLabel(), 0);
        }

        //[!-- 9. Tworzenie obiektu duchow
        this.enemies = BoardFeatures.spawn_enemies(this.spawn_duchy_chunk, this.ammount_of_enemies, cells);

        //[!-- 10. Dodawanie enemie na mape
        for(var i : this.enemies) {
            cells[i.getY()][i.getX()].add(i.getLabel(), BorderLayout.CENTER);
            cells[i.getY()][i.getX()].setComponentZOrder(i.getLabel(), 0);
            System.out.printf("Dodanie ducha na: %d %d\n",i.getY(),i.getX());  }

        //[!-- 11. Tworzenie obiektu gracza, ale bez ikony bez niczego
        this.player = new Player(spawn_pacman_chunk.x+2, spawn_pacman_chunk.y+2);

        //[!-- 12. Wpisywanie go do kolumny
        cells[ spawn_pacman_chunk.y +2][ spawn_pacman_chunk.x +2]
                .add(player.getLabel(), BorderLayout.CENTER);

        //[!-- 13. Ostateczne rysowanie mapy --!]
        for(var row = 0; row < this.szerokosc_x; row++) {
            for(var column = 0; column < this.szerokosc_y; column++) {
                this.add(cells[row][column]);
            }
        }
        this.enemyManager = new PropsLogic(this.enemies, this.cells, this.player, this.actualPowerLabel, this, this.parentWindow);
        SwingUtilities.invokeLater(() -> {
            int row = spawn_pacman_chunk.y+2; // <-- +2 bo sciany i zaczynanie od 0
            int col = spawn_pacman_chunk.x+2;

            int w = cells[row][col].getWidth();
            int h = cells[row][col].getHeight();
            for (Food f : this.foods) {
                f.setImageForSize(w-10, h-10);
                cells[f.getY()][f.getX()].revalidate();
                cells[f.getY()][f.getX()].repaint();
            }
            for (var f : this.enemies) {
                f.setImageForSize(w, h);
                cells[f.getY()][f.getX()].revalidate();
                cells[f.getY()][f.getX()].repaint();
            }
            player.setImageForSize(w, h);

            cells[row][col].revalidate();
            cells[row][col].repaint();

            this.revalidate();

            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.repaint();
            }

            if(AppConfig.dev_mode) {
                System.out.println("Label size: " +
                        player.getLabel().getWidth() + " x " +
                        player.getLabel().getHeight());
                System.out.println("Cell size: " +
                        cells[row][col].getWidth() + " x " +
                        cells[row][col].getHeight());
            }

            enemyThread = new EnemyThread(enemyManager, cells, 400);
            ThreadMenager.register(enemyThread);
            enemyThread.start();
            Thread timerThread = new Thread(() -> {
                try {
                    int seconds = 0;
                    while (true) {
                        Thread.sleep(1000);
                        seconds++;
                        int finalSeconds = seconds;
                        SwingUtilities.invokeLater(() ->
                                actualtimeLabel.setText(String.valueOf(finalSeconds))
                        );
                    }
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }, "Timer-Thread");
            timerThread.setDaemon(true);
            ThreadMenager.register(timerThread);
            timerThread.start();
        });

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int cellW = getWidth() / szerokosc_y;
                int cellH = getHeight() / szerokosc_x;

                for (Food f : foods) {
                    f.setImageForSize(cellW-10, cellH-10);
                    cells[f.getY()][f.getX()].revalidate();
                    cells[f.getY()][f.getX()].repaint();
                }
                for (var f : enemies) {
                    f.setImageForSize(cellW, cellH);
                    cells[f.getY()][f.getX()].revalidate();
                    cells[f.getY()][f.getX()].repaint();
                }
                player.setImageForSize(cellW, cellH);

                cells[player.getY()][player.getX()].revalidate();
                cells[player.getY()][player.getX()].repaint();
            }
        });

        addHierarchyListener(e -> {
            if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0 &&
                    this.getParent() == null) {
                if (enemyThread != null && enemyThread.isAlive()) {
                    enemyThread.terminate();
                }
            }
        });
        System.out.printf(AppConfig.dev_mode ? "[+] Player dodany na koordy %d | %d \n" : "", spawn_pacman_chunk.x, spawn_pacman_chunk.y);
    }

    public int get_score() {
        return this.player.player_score;
    }

    public String get_time() {
        return this.actualtimeLabel.getText();
    }

    public Dimension getPreferredSize() {
        int cellSize = 10;
        return new Dimension(this.szerokosc_y * cellSize,
                this.szerokosc_x * cellSize);
    }

    public boolean tryMovePlayer(int dCol, int dRow) {

        if(this.foods.length == player.player_score) {
            SwingUtilities.invokeLater(() -> {
                int actualScore = get_score();
                String actualTime = get_time();
                ThreadMenager.killAll();

                if (this.parentWindow != null) {
                    this.parentWindow.dispose();
                }
                new SaveGameWindow(actualScore, actualTime);
            });
        }

        int oldCol = player.getX();
        int oldRow = player.getY();
        int newCol = oldCol + dCol;
        int newRow = oldRow + dRow;

        if (newRow < 0 || newRow >= szerokosc_x ||
                newCol < 0 || newCol >= szerokosc_y) {
            return false;
        }

        if (cells[newRow][newCol].getBackground().equals(AppConfig.walls_color)) {
            if(player.can_destroy_wall && newRow != 0 && newRow != szerokosc_y - 1 && newCol != szerokosc_x - 1 && newCol != 0) {
                System.out.printf("[+] Player destroyed wall at %d %d\n", newRow, newCol);
                cells[newRow][newCol].setBackground(AppConfig.background_color);
                cells[newRow][newCol].revalidate();
                cells[newRow][newCol].repaint();
                actualPowerLabel.setText("Actual power: none");
                player.can_destroy_wall = false;
            }
            return false;
        }

        Component[] components = cells[newRow][newCol].getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel && "Food".equals(comp.getName())) {
                cells[newRow][newCol].remove(comp);
                cells[newRow][newCol].revalidate();
                cells[newRow][newCol].repaint();
                player.eat();
                scoreLabel.setText("Ilosc itn-ow: " + player.player_score);
                break;
            }
        }

        player.moveTo(newCol, newRow, cells);
        return true;
    }
}

class BoardFeatures {
    public static void draw_walls(JPanel[][] cells) {
        //Ustawienie scian
        for(int r = 0; r < cells.length; r++) {
            JPanel wall = new JPanel(new BorderLayout());
            wall.setBackground(AppConfig.walls_color);
            wall.setBorder(BorderFactory.createLineBorder(AppConfig.wall_border_color, 1));
            cells[r][0] = wall;
        }
        for(int r = 0; r < cells.length; r++) {
            JPanel wall = new JPanel(new BorderLayout());
            wall.setBackground(AppConfig.walls_color);
            wall.setBorder(BorderFactory.createLineBorder(AppConfig.wall_border_color, 1));
            cells[r][cells[0].length-1] = wall;
        }
        for(int r = 0; r < cells[0].length; r++) {
            JPanel wall = new JPanel(new BorderLayout());
            wall.setBackground(AppConfig.walls_color);
            wall.setBorder(BorderFactory.createLineBorder(AppConfig.wall_border_color, 1));
            cells[0][r] = wall;
        }
        for(int r = 0; r < cells[0].length; r++) {
            JPanel wall = new JPanel(new BorderLayout());
            wall.setBackground(AppConfig.walls_color);
            wall.setBorder(BorderFactory.createLineBorder(AppConfig.wall_border_color, 1));
            cells[cells.length-1][r] = wall;
        }
    }
    public static Chunk[] generate_chunks(JPanel[][] cells) {

        var szerokosc_x = cells[0].length;
        var szerokosc_y = cells.length;

        //Chunki maja miec w zalozeniu rozmiar 5x5, lecz moze sie to zmienic zaleznie od podanych rozmiarow
        int ilosc_chunkow_x = 0;
        if((szerokosc_x-2) % 5 == 0) {
            ilosc_chunkow_x = ((szerokosc_x-2)/5);
        }
        else
        {
            ilosc_chunkow_x = (((szerokosc_x-2)/5)+1);
        }

        int ilosc_chunkow_y = 0;
        if((szerokosc_y-2) % 5 == 0) {
            ilosc_chunkow_y = ((szerokosc_y-2)/5);
        }
        else
        {
            ilosc_chunkow_y = (((szerokosc_y-2)/5)+1);
        }


        var ilosc_chunkow = ilosc_chunkow_y * ilosc_chunkow_x; //-2 bo nie licze scian


        System.out.printf("X:Y (%d, %d) ilosc chunkow Chunk %d jest srodkowy %d\n", szerokosc_x, szerokosc_y, ilosc_chunkow, ilosc_chunkow/2);

        Chunk[] output_chunks = new Chunk[ilosc_chunkow];

        int counter_chunkow = 0;

        //      y  x
        // cells[] []
        for(int y = 1; y < szerokosc_y-1; y+=Chunk.distance_prefered) {
            for(int x = 1; x < szerokosc_x-1; x+=Chunk.distance_prefered) {
                int dlugosc_chunka_x = 0;
                int dlugosc_chunka_y = 0;

                if(x+Chunk.distance_prefered > szerokosc_x-1) {
                    dlugosc_chunka_x = (szerokosc_x-1) - x;
                }
                else {
                    dlugosc_chunka_x = Chunk.distance_prefered;
                }
                if(y+Chunk.distance_prefered > szerokosc_y-1) {
                    dlugosc_chunka_y = (szerokosc_y-1) - y;
                }
                else {
                    dlugosc_chunka_y = Chunk.distance_prefered;
                }
                if(AppConfig.dev_mode) {
                    JPanel wall = new JPanel();
                    wall.setBackground(Color.GREEN);
                    wall.setBorder(BorderFactory.createLineBorder(AppConfig.wall_border_color, 1));
                    cells[y][x] = wall;

                    System.out.printf("[+] %d Stworzono Chunk na (%d, %d), ma dlugosc = [%d, %d] \n",counter_chunkow,  x, y, dlugosc_chunka_x, dlugosc_chunka_y);
                }

                try {
                    output_chunks[counter_chunkow] = new Chunk(x,y, dlugosc_chunka_x, dlugosc_chunka_y, Chunk.type_of_chunk.NORMAL);
                }
                catch(Exception e) {
                    System.out.printf("Stopped at: (%d, %d) chunk nmbr: %d \n", x, y, counter_chunkow);
                }

                counter_chunkow++;
            }
        }
        return output_chunks;
    }
    public static Food[] spawn_food(JPanel[][] cells) {
        java.util.List<Food> list = new java.util.ArrayList<>();

        for (int y = 0; y < cells.length; y++) {
            for (int x = 0; x < cells[y].length; x++) {
                if (!cells[y][x].getBackground().equals(AppConfig.walls_color)) {
                    list.add(new Food(x, y));
                }
            }
        }
        return list.toArray(new Food[0]);
    }
    public static Enemy[] spawn_enemies(Chunk point_spawn, int count, JPanel[][] cells) {
        java.util.List<Enemy> list = new java.util.ArrayList<>();

        for(int x = point_spawn.x; x < point_spawn.x + count; x++) {
            list.add(new Enemy(x, point_spawn.y+(x-point_spawn.x), cells));
        }
        return  list.toArray(new Enemy[0]);
    }
}



