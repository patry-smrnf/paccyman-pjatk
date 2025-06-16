package Windows;

import Config.AppConfig;
import Game.GameBoard;
import Game.ThreadMenager;
import ogGUI.ogLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class GameWindow extends JFrame implements Runnable {
    private static final int bottom_menu_height = 50;
    private final Set<Integer> pressedKeys = new HashSet<>();

    public static int final_time = 0;

    private GameBoard gameBoard;

    public GameWindow(int ilosc_kolumn, int ilosc_wierszy) {

        //[!-- Ustawianie danych dotyczacych okna --!]
        this.setTitle("Pacman retarded edition");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(AppConfig.WindowWidth, AppConfig.WindowHeight);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setLayout(new BorderLayout());
        this.setBackground(Color.black);

        //[!-- Wyliczenie minimalnego osiagalnego rozmiaru okna --!]
        SwingUtilities.invokeLater(() -> {
            int min_window_width = ilosc_kolumn * 10;
            int min_window_height = ilosc_wierszy * 10 + bottom_menu_height;
            this.setMinimumSize(new Dimension(min_window_width, min_window_height));
            System.out.println(min_window_width + " " + min_window_height);
        });

        //[!-- Gorny Panel --!]
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel actualtime = new ogLabel("Actual time: 0000", Color.YELLOW);
        actualtime.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        JLabel actualPowerLabel = new ogLabel("Actual power: none", Color.red);
        actualPowerLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        topPanel.add(actualPowerLabel, BorderLayout.WEST);
        topPanel.add(actualtime, BorderLayout.EAST);
        topPanel.setPreferredSize(new Dimension(this.getContentPane().getWidth(), 30));
        topPanel.setBackground(Color.black);
        this.add(topPanel, BorderLayout.NORTH);

        //[!-- Dolny panel --!]
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel infoLabel = new ogLabel("Pacman PJATK edition premium export", Color.BLUE);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        JLabel scoreLabel = new ogLabel("Score: 0", Color.RED);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        bottomPanel.add(infoLabel, BorderLayout.WEST);
        bottomPanel.add(scoreLabel, BorderLayout.EAST);
        bottomPanel.setPreferredSize(new Dimension(this.getContentPane().getWidth(), bottom_menu_height));
        bottomPanel.setBackground(Color.black);
        this.add(bottomPanel, BorderLayout.SOUTH);

        //[!-- Panel Z gra --!]
        this.gameBoard = new GameBoard(ilosc_kolumn, ilosc_wierszy, scoreLabel, actualPowerLabel, actualtime, this);
        this.gameBoard.setBackground(Color.black);
        this.add(gameBoard, BorderLayout.CENTER);

        setFocusable(true);
        requestFocusInWindow();
        SwingUtilities.invokeLater(this::requestFocusInWindow);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int kc = e.getKeyCode();
                pressedKeys.add(kc); // Mark key as down

                // Check for CTRL + SHIFT + Q
                boolean ctrl = (e.isControlDown());
                boolean shift = (e.isShiftDown());
                boolean q = (kc == KeyEvent.VK_Q);

                if (ctrl && shift && q) {
                    System.out.println("CTRL + SHIFT + Q detected");
                    ThreadMenager.killAll();
                    new MainMenuWindow();
                    GameWindow.this.dispose();
                    return;
                }

                // Prevent key repeat
                if (pressedKeys.size() > 1 && pressedKeys.contains(kc)) {
                    return;
                }

                int dcol = 0, drow = 0;
                switch (kc) {
                    case KeyEvent.VK_W -> drow = -1;
                    case KeyEvent.VK_S -> drow = +1;
                    case KeyEvent.VK_A -> dcol = -1;
                    case KeyEvent.VK_D -> dcol = +1;
                    default -> { return; }
                }

                gameBoard.tryMovePlayer(dcol, drow);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // When the key is released, remove it from the set:
                pressedKeys.remove(e.getKeyCode());
            }
        });

        this.setVisible(true);
    }

    public void run(){

    }

}
