package util;

import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import model.*;

public class ImageUtils {
	
	public ImageUtils() {}
	
	public static BufferedImage getImage(Problem[] problems, int num) {
		BufferedImage img = null;
		try {
			URL path = ImageUtils.class.getClassLoader().getResource(problems[num].getPath());
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
	
	public static BufferedImage scaleImage(BufferedImage img, int newWidth) {
	    int orgWidth = img.getWidth();
	    int orgHeight = img.getHeight();

	    double ratio = (double)newWidth / orgWidth;
	    int newHeight = (int)(orgHeight * ratio);

	    Image tmp = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

	    BufferedImage scaled = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = scaled.createGraphics();
	    g2.drawImage(tmp, 0, 0, null);
	    g2.dispose();
	    
	    return scaled;
	}
	
    public static BufferedImage removeBackground(BufferedImage img, Color panelColor, int threshold) {
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage inputImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        
        Color panelBackgroundColor = panelColor;
        
        Color textColor = ColorUtils.getContrastColor(panelBackgroundColor);
        
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
                
                int alpha = (rgb >> 24) & 0xFF;
                if (alpha == 0) {
                    inputImg.setRGB(x, y, 0x00000000);
                    continue;
                }
                
                int brightness = (r + g + b) / 3;

                if (brightness >= threshold) {
                    inputImg.setRGB(x, y, 0x00000000);
                }
                else {
                    inputImg.setRGB(x, y, textRGB);
                }
            }
        }
        
        
        return inputImg;
    }
    
    public static BufferedImage invertColors(BufferedImage img, Color newColor) {
        int w = img.getWidth(), h = img.getHeight();
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        int nr = newColor.getRed();
        int ng = newColor.getGreen();
        int nb = newColor.getBlue();
        int newRGB = (0xFF << 24) | (nr << 16) | (ng << 8) | nb;

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int rgb = img.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;

                if (alpha == 0) {
                    out.setRGB(x, y, 0x00000000);
                } else { // 그림
                    out.setRGB(x, y, newRGB);
                }
            }
        }
        return out;
    }

    
}
