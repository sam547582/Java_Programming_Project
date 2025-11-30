package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class BackgroundPanel extends JPanel {

    private BufferedImage bgImage;
    private String bgPath;
    private double angle = 0;

    public BackgroundPanel() {
    	
    	bgPath = "resources/img/background/IU.jpg";
        try {
            bgImage = ImageIO.read(new File(bgPath));
        } catch (Exception e) { e.printStackTrace(); }

        Timer timer = new Timer(30, e -> {
            angle += 0.001;
            repaint();
        });
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();

        int w = getWidth();
        int h = getHeight();
        
        if(!bgPath.equals("resources/img/background/IU.jpg")) g2.rotate(angle, w / 2.0, h / 2.0);
        
        if(bgPath.equals("resources/img/background/grid.png")) {
	        double scale = 1.15;
	        int newW = (int)(w * scale);
	        int newH = (int)(h * scale);
	
	        int x = (w - newW) / 2;
	        int y = (h - newH) / 2;
	
	        g2.drawImage(bgImage, x, y, newW, newH, this);
        }
        else {
        	g2.drawImage(bgImage, 0, 0, w, h, this);
        }

        g2.dispose();
    }
}

