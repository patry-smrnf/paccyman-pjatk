package Windows;

import Config.AppConfig;
import ogGUI.ogButton;
import ogGUI.ogTextField;

import javax.swing.*;
import java.awt.*;

public class MainMenuWindow extends JFrame {
    public MainMenuWindow() {
        this.setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 250);
        getContentPane().setBackground(Color.BLACK);

        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        backgroundPanel.setOpaque(false);
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150));

        JButton newgame_btn = new ogButton("New Game");
        JButton highscores_btn = new ogButton("Highscores");
        JButton exit_btn = new ogButton("Exit");

        newgame_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        highscores_btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit_btn.setAlignmentX(Component.CENTER_ALIGNMENT);

        backgroundPanel.add(newgame_btn);
        backgroundPanel.add(Box.createVerticalStrut(15));
        backgroundPanel.add(highscores_btn);
        backgroundPanel.add(Box.createVerticalStrut(15));
        backgroundPanel.add(exit_btn);

        newgame_btn.addActionListener(e -> {
            new CreateGameWindow();
            MainMenuWindow.this.dispose();
        });

        highscores_btn.addActionListener(e -> {
            new HighScoresWindow();
            MainMenuWindow.this.dispose();
        });

        exit_btn.addActionListener(e -> {
            System.exit(0);
        });

        this.setLayout(new BorderLayout());
        this.add(backgroundPanel, BorderLayout.CENTER);

        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
