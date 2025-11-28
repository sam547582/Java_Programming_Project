package ui;

import javax.swing.*;
import java.awt.*;

class ColorButton extends JButton {
    private Color color;
    
    public ColorButton(Color color) {
        this.color = color;
        setPreferredSize(new Dimension(30, 30));
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}