package Windows;

import Config.AppConfig;
import ogGUI.ogButton;
import ogGUI.ogLabel;
import ogGUI.ogTextField;

import javax.swing.*;
import java.awt.*;

public class CreateGameWindow extends JFrame {
    public CreateGameWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 200);

        // 1) Make the CONTENT PANE black (not just the frame border)
        getContentPane().setBackground(Color.BLACK);

        // 2) Use a BorderLayout with zero hgap/vgap
        setLayout(new BorderLayout());

        JLabel topLabel = new ogLabel("Enter your information", Color.BLUE, Color.BLACK);
        add(topLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(2, 2));
        centerPanel.setBackground(Color.BLACK);

        JLabel label1 = new ogLabel("Columns:", Color.WHITE, 12);
        label1.setBackground(Color.BLACK);
        label1.setOpaque(true);

        JLabel label2 = new ogLabel("Rows:", Color.WHITE, 12);
        label2.setBackground(Color.BLACK);
        label2.setOpaque(true);

        JTextField textField1 = new ogTextField();

        JTextField textField2 = new ogTextField();

        centerPanel.add(label1);
        centerPanel.add(label2);
        centerPanel.add(textField1);
        centerPanel.add(textField2);
        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        bottomPanel.setBackground(Color.BLACK);

        JButton button1 = new ogButton("Start Game");
        JButton button2 = new ogButton("Back");

        button1.setBackground(Color.DARK_GRAY);
        button1.setForeground(Color.WHITE);
        button1.setFocusPainted(false);

        button2.setBackground(Color.DARK_GRAY);
        button2.setForeground(Color.WHITE);
        button2.setFocusPainted(false);

        bottomPanel.add(button1);
        bottomPanel.add(button2);
        add(bottomPanel, BorderLayout.SOUTH);

        button1.addActionListener(e -> {
            try {
                int sizeInt = Integer.parseInt(textField1.getText());
                int sizeInt2 = Integer.parseInt(textField2.getText());
                if(sizeInt >= 10 && sizeInt2 >= 10 && sizeInt <= 100 && sizeInt2 <= 100) {
                    new GameWindow(sizeInt, sizeInt2);
                    CreateGameWindow.this.dispose();
                }
                else {
                    JOptionPane.showMessageDialog(
                            CreateGameWindow.this,
                            "Limit, gra moze byc od 10 do 100",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        CreateGameWindow.this,
                        "Please enter a number",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        button2.addActionListener(e -> {
            new MainMenuWindow();
            CreateGameWindow.this.dispose();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
