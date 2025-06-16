package ogGUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ogLabel extends JLabel {
    private Color glowColor;
    private int glowSize = 3;
    private int fontSize;

    public ogLabel(String text, Color color) {
        super(text);
        this.fontSize = 21;
        this.glowColor = color;

        // Padding on the left so text isn’t glued to the edge
        this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        this.setForeground(color);

        // By default, we leave this label non‐opaque (transparent).
        // If you want a black background, do:
        //   setBackground(Color.BLACK);
        //   setOpaque(true);

        try {
            Font customFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File(ogPaths.font_path)
            ).deriveFont(Font.PLAIN, this.fontSize);
            this.setFont(customFont);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public ogLabel(String text, Color colorFr, Color colorBg) {
        super(text);
        this.fontSize = 21;
        this.glowColor = colorFr;

        this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        this.setForeground(colorFr);

        this.setBackground(colorBg);
        this.setOpaque(true);

        try {
            Font customFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File(ogPaths.font_path)
            ).deriveFont(Font.PLAIN, this.fontSize);
            this.setFont(customFont);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor: custom font‐size label with default black background.
     * Always opaque.
     */
    public ogLabel(String text, Color color, int size) {
        super(text);
        this.fontSize = size;
        this.glowColor = color;

        this.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        this.setForeground(color);

        // Default background is black here:
        this.setBackground(Color.BLACK);
        this.setOpaque(true);

        try {
            Font customFont = Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File(ogPaths.font_path)
            ).deriveFont(Font.PLAIN, this.fontSize);
            this.setFont(customFont);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        if (isOpaque()) {
            g2.setColor(getBackground());
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        g2.setFont(getFont());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        String text = getText();
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth()  - fm.stringWidth(text)) / 2;
        int y = (getHeight() + fm.getAscent())   / 2 - 4;
        for (int i = glowSize; i > 0; i--) {
            float alpha = 0.1f * (i / (float) glowSize);
            Color glowWithAlpha = new Color(
                    glowColor.getRed(),
                    glowColor.getGreen(),
                    glowColor.getBlue(),
                    (int) (alpha * 255)
            );
            g2.setColor(glowWithAlpha);
            g2.drawString(text, x - i, y);
            g2.drawString(text, x + i, y);
            g2.drawString(text, x, y - i);
            g2.drawString(text, x, y + i);
        }
        g2.setColor(getForeground());
        g2.drawString(text, x, y);

        g2.dispose();
    }
}
