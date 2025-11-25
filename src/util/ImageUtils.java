package util;

import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import model.*;

public class ImageUtils {
	
	private BufferedImage img;
	private BufferedImage removed_img;
	private BufferedImage scaled_img;
	
	private Color textColor;
	
	private JPanel panel;
	
	private Problem[] problems;
	
	public ImageUtils(JPanel panel, Problem[] problems) {
		this.panel = panel;
		this.problems = problems;
	}
	
	public void setPanel(JPanel panel) {
		this.panel = panel;
	}
	
	public Color getTextColor() {
		return textColor;
	}
	
	public BufferedImage getImage(int num) {
		
		try {
			URL path = getClass().getClassLoader().getResource(problems[num].getPath());
			if (path == null) {
			    System.out.println("Can't find image" + problems[num].getPath());
			}
			img = ImageIO.read(path);
		}
		catch(IOException e) {
		    JOptionPane.showMessageDialog(null,"Error" + e.getMessage());
		}
		
		return img;
	}
	
	public BufferedImage scaleImage(int newWidth) {
	    int orgWidth = removed_img.getWidth();
	    int orgHeight = removed_img.getHeight();

	    double ratio = (double)newWidth / orgWidth;
	    int newHeight = (int)(orgHeight * ratio);

	    Image tmp = removed_img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

	    BufferedImage scaled = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = scaled.createGraphics();
	    g2.drawImage(tmp, 0, 0, null);
	    g2.dispose();
	    
	    scaled_img = scaled;
	    
	    return scaled_img;
	}
	
    public BufferedImage removeBackground(int threshold) {
        int w = img.getWidth();
        int h = img.getHeight();

        removed_img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        
        Color panelBg;
        
        if(panel != null) {
        	panelBg = panel.getBackground();
        }
        else {
        	panelBg = new Color(255,255,255);
        }
        
        textColor = getContrastColor(panelBg);
        
        int tr = textColor.getRed();
        int tg = textColor.getGreen();
        int tb = textColor.getBlue();
        int textRGB = (0xFF << 24) | (tr << 16) | (tg << 8) | tb;
        
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                int rgb = img.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                
                int brightness = (r + g + b) / 3;

                if (brightness >= threshold) {
                    removed_img.setRGB(x, y, 0x00000000);
                }
                else {
                    removed_img.setRGB(x, y, textRGB);
                }
            }
        }
        
        
        return removed_img;
    }
    
    private Color getContrastColor(Color bg) {
        double brightness = bg.getRed() * 0.299
                          + bg.getGreen() * 0.587
                          + bg.getBlue() * 0.114;

        return brightness > 128 ? Color.BLACK : Color.WHITE;
    }
}
