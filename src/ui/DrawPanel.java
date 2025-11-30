package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import util.*;

public class DrawPanel extends JPanel {

    private BufferedImage canvas;
    private Graphics2D g2;
    private Color backColor;
    
    private int lastX, lastY;

    DrawPanel() {
        setOpaque(false);
        backColor = Color.WHITE;
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (canvas == null || canvas.getWidth() != getWidth() || canvas.getHeight() != getHeight()) {
            initCanvas(backColor);
        }

        g.drawImage(canvas, 0, 0, null);
    }

    /** center 전체 크기만큼 캔버스 생성 */
    public void initCanvas(Color color) {
    	
    	if (canvas != null) return;
    	
    	backColor = color;
    	
        int W = getWidth();
        int H = getHeight();

        if (W <= 0 || H <= 0) return;

        canvas = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);

        g2 = canvas.createGraphics();
        g2.setStroke(new BasicStroke(3));
        g2.setColor(ColorUtils.getContrastColor(color));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }


    private MouseAdapter mouseAdapter = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            if (!isVisible()) return; 

            lastX = e.getX();
            lastY = e.getY();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!isVisible()) return;

            int x = e.getX();
            int y = e.getY();

            g2.drawLine(lastX, lastY, x, y);

            lastX = x;
            lastY = y;

            repaint();
        }
    };
    
    public void updateColor(Color color) {

        if (canvas == null) initCanvas(backColor);

        if (color.equals(backColor)) {
            return;
        }
        backColor = color;

        Color penColor = ColorUtils.getContrastColor(color);

        canvas = ImageUtils.invertColors(canvas, penColor);

        g2 = canvas.createGraphics();
        g2.setStroke(new BasicStroke(3));
        g2.setColor(penColor);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        repaint();
    }

    public void clear() {
        initCanvas(backColor);
        repaint();
    }
}
