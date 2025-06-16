package ogGUI;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class ogBorder extends AbstractBorder {
    private final Color color;
    private final int thickness;
    private final int dotSpacing;

    public ogBorder(Color color, int thickness, int dotSpacing) {
        this.color = color;
        this.thickness = thickness;
        this.dotSpacing = dotSpacing;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(color);
        float[] dashPattern = {1, dotSpacing};
        g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
        g2.drawRect(x + thickness / 2, y + thickness / 2, width - thickness, height - thickness);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(thickness, thickness, thickness, thickness);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(thickness, thickness, thickness, thickness);
        return insets;
    }
}