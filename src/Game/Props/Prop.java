package Game.Props;

import javax.swing.*;
import java.awt.*;

public class Prop {
    private int x;
    private int y;

    protected JLabel playerLabel;
    protected ImageIcon[] frames = new ImageIcon[2];
    private boolean useAlternateFrame = false;

    public Prop(int x, int y, String path_icon, String path_icon2, String name) {
        this.x = x;
        this.y = y;
        this.playerLabel = new JLabel();
        this.playerLabel.setName(name);
        frames[0] = new ImageIcon(path_icon);
        frames[1] = new ImageIcon(path_icon2);
        playerLabel.setIcon(frames[0]);
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    public JLabel getLabel() {
        return playerLabel;
    }

    public void setImageForSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            System.err.println("Invalid size.");
            return;
        }
        // Pick the correct ImageIcon based on useAlternateFrame
        ImageIcon baseIcon = useAlternateFrame ? frames[1] : frames[0];
        if (baseIcon == null || baseIcon.getImage() == null) {
            System.err.println("Missing image in frame.");
            return;
        }

        // Scale the image
        Image scaled = baseIcon.getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaled);

        playerLabel.setIcon(scaledIcon);
        Dimension size = new Dimension(width, height);
        playerLabel.setPreferredSize(size);
        playerLabel.setMinimumSize(size);
        playerLabel.setMaximumSize(size);
        playerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerLabel.setVerticalAlignment(SwingConstants.CENTER);

        playerLabel.revalidate();
        playerLabel.repaint();
    }

    public void toggleFrame() {
        useAlternateFrame = !useAlternateFrame;
    }
}
