package Windows;

import Game.Score.Score;
import ogGUI.ogButton;
import ogGUI.ogLabel;
import ogGUI.ogTextField;

import javax.swing.*;
import java.awt.*;

public class SaveGameWindow extends JFrame {
    public SaveGameWindow(int score, String time) {
        this.setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 250);
        getContentPane().setBackground(Color.BLACK);

        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        backgroundPanel.setOpaque(false);
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(40, 150, 40, 150));

        JLabel score_label = new ogLabel("Your score: " + score, Color.BLUE);
        JLabel time_label = new ogLabel("Your time: " + time, Color.BLUE);
        JLabel name_label = new ogLabel("Whats your name?: ", Color.WHITE);
        JTextField textField1 = new ogTextField();
        JButton ok_button = new ogButton("OK");

        backgroundPanel.add(score_label);
        backgroundPanel.add(time_label);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(name_label);
        backgroundPanel.add(textField1);
        backgroundPanel.add(Box.createVerticalStrut(20));
        backgroundPanel.add(ok_button);

        ok_button.addActionListener(e -> {
            if(textField1.getText().equals("")) {
                JOptionPane.showMessageDialog(
                        SaveGameWindow.this,
                        "Nazwa gracza nie moze byc pusta",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
            else {
                Score.SaveGame(score, time, textField1.getText());
                JOptionPane.showMessageDialog(
                        SaveGameWindow.this,
                        "Zapisano elegancko wynik",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                new MainMenuWindow();
                SaveGameWindow.this.dispose();
            }
        });


        this.setLayout(new BorderLayout());
        this.add(backgroundPanel, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
