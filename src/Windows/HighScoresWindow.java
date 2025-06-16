package Windows;

import Config.AppConfig;
import ogGUI.ogButton;
import ogGUI.ogLabel;
import ogGUI.ogPaths;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class HighScoresWindow extends JFrame {
    public HighScoresWindow() {
        this.setTitle("High Scores");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 350);

        File file = new File(AppConfig.saves_file_path);
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        backgroundPanel.setBackground(Color.BLACK);
        JButton back_button = new ogButton("Back");

        if (!file.exists()) {
            JLabel back_label = new ogLabel("Theres no saved games :( ", Color.WHITE);
            backgroundPanel.add(back_label, BorderLayout.CENTER);
            backgroundPanel.add(back_button, BorderLayout.EAST);

        } else {
            DefaultListModel<String> listModel = loadRecordsFromFile(AppConfig.saves_file_path);

            JList<String> recordList = new JList<>(listModel);
            recordList.setBackground(Color.BLACK);
            recordList.setForeground(Color.WHITE);

            try {
                Font customFont = Font.createFont(
                        Font.TRUETYPE_FONT,
                        new File(ogPaths.font_path)
                ).deriveFont(Font.PLAIN, 16);
                recordList.setFont(customFont);
            } catch (IOException | FontFormatException e) {
                throw new RuntimeException(e);
            }

            recordList.setSelectionBackground(Color.BLACK);       // Selection background
            recordList.setSelectionForeground(Color.BLUE);            // Selection text

            JScrollPane scrollPane = new JScrollPane(recordList);
            scrollPane.setBorder(null);
            scrollPane.getViewport().setBackground(Color.BLACK);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = Color.BLUE; // Scrollbar thumb
                    this.trackColor = Color.BLACK;
                }
            });

            JLabel info = new ogLabel("Score | Time | Name", Color.GREEN, 15);

            info.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add top padding
            backgroundPanel.add(info);
            backgroundPanel.add(scrollPane);
            backgroundPanel.add(Box.createVerticalStrut(10));
            backgroundPanel.add(back_button);

        }
        this.add(backgroundPanel);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        back_button.addActionListener(e -> {
            new MainMenuWindow();
            HighScoresWindow.this.dispose();
        });
    }


    private DefaultListModel<String> loadRecordsFromFile(String filename) {
        DefaultListModel<String> listModel = new DefaultListModel<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                listModel.addElement(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage());
        }

        return listModel;
    }
}
