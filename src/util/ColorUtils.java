package util;

import javax.swing.*;
import java.awt.*;

public class ColorUtils {

	public static Color getContrastColor(Color bg) {
		double brightness = bg.getRed() * 0.299 + bg.getGreen() * 0.587 + bg.getBlue() * 0.114;

		return brightness > 128 ? Color.BLACK : Color.WHITE;
	}
	
	// 선형 보간 알고리즘
	public static Color getAccuracyColor(double rate) {
		// 0~1 사이로 제한
		rate = Math.max(0, Math.min(1, rate));

		if (rate < 0.5) {
			// 0%~50% : Red → Green
			double t = rate / 0.5; // 0~1로 변환
			int r = (int) (255 * (1 - t)); // 255 → 0
			int g = (int) (255 * t); // 0 → 255
			int b = 0;
			return new Color(r, g, b);

		} else {
			// 50%~100% : Green → Blue
			double t = (rate - 0.5) / 0.5; // 0~1로 변환
			int r = 0;
			int g = (int) (255 * (1 - t)); // 255 → 0
			int b = (int) (255 * t); // 0 → 255
			return new Color(r, g, b);
		}
	}

}
