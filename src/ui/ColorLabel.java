package ui;

import javax.swing.*;
import java.awt.*;

class ColorLabel extends JLabel {
    private Color bg;

    public void setBg(Color bg) {
        this.bg = bg;
        repaint();
    }
    
    public Color getBg() {
        return bg;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (bg != null) {
            g.setColor(bg);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }
}