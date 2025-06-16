package ogGUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ogTextField extends JTextField {
    public ogTextField() {
        super();
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(ogPaths.font_path)).deriveFont(Font.PLAIN, 12);
            this.setFont(customFont);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
        this.setBorder(new ogBorder(Color.WHITE, 1, 4));
        this.setForeground(Color.WHITE);
        this.setBackground(Color.BLACK);
    }
}
