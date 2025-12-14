package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import util.*;
import model.*;

public class LineChartPanel extends JPanel {
	private List<Integer> scores;

	public LineChartPanel() {
		this.scores = List.of(60, 65, 70, 68, 75, 80, 78, 85);
		setBackground(new Color(0, 0, 0, 0));
		setPreferredSize(new Dimension(400, 250));
	}

	private void drawAxes(Graphics2D g2) {
		g2.setColor(Color.LIGHT_GRAY);

		int p = 40;
		int w = getWidth();
		int h = getHeight();

		// Y축
		g2.drawLine(p, p, p, h - p);
		// X축
		g2.drawLine(p, h - p, w - p, h - p);
	}

	private void drawLine(Graphics2D g2) {
		if (scores.size() < 2)
			return;

		int p = 40;
		int w = getWidth();
		int h = getHeight();

		int chartW = w - p * 2;
		int chartH = h - p * 2;

		int maxScore = 100;

		g2.setColor(new Color(90, 160, 220));
		g2.setStroke(new BasicStroke(2.5f));

		for (int i = 0; i < scores.size() - 1; i++) {
			int x1 = p + (chartW * i) / (scores.size() - 1);
			int y1 = h - p - (chartH * scores.get(i)) / maxScore;

			int x2 = p + (chartW * (i + 1)) / (scores.size() - 1);
			int y2 = h - p - (chartH * scores.get(i + 1)) / maxScore;

			g2.drawLine(x1, y1, x2, y2);

			// 점 찍기
			g2.fillOval(x1 - 3, y1 - 3, 6, 6);

			g2.setFont(new Font("Arial", Font.BOLD, 12));
			g2.setColor(new Color(230, 230, 230)); // 완전 흰색 X

			g2.drawString(String.valueOf(scores.get(i)), x1 - 5, y1 - 10);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		// 안티앨리어싱 (중요)
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		drawAxes(g2);
		drawLine(g2);

		g2.dispose();
	}
}
