package util;

import javax.swing.*;
import java.awt.*;

public class ColorUtils {
	
    public static Color getContrastColor(Color bg) {
        double brightness = bg.getRed() * 0.299
                          + bg.getGreen() * 0.587
                          + bg.getBlue() * 0.114;

        return brightness > 128 ? Color.BLACK : Color.WHITE;
    }
    
}
