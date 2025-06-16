package ogGUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class ogButton extends JButton {
    public ogButton(String text) {
        super(text);
        setBackground(Color.BLACK);
        setForeground(Color.WHITE);

        try {
            Font customFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File(ogPaths.font_path)
            ).deriveFont(Font.PLAIN, 16);
            setFont(customFont);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }

        Border dotted = new ogBorder(Color.BLUE, 3, 2);

        Border padding = new EmptyBorder(10, 20, 10, 20);

        setBorder(new CompoundBorder(dotted, padding));

        setContentAreaFilled(false);
        setOpaque(true);
        setFocusPainted(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Color.DARK_GRAY);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(Color.BLACK);
            }
        });
    }
}
