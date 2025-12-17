package ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import util.*;
import model.*;

public class LineChartPanel extends JPanel {
	private List<Integer> scores;

	public LineChartPanel() {
		this.scores = StatsManager.getScore();
		setBackground(new Color(0, 0, 0, 0));
		setPreferredSize(new Dimension(500, 350));
	}

	private void drawYAxisAndGrid(Graphics2D g2) {
		int p = 30;
		int w = getWidth();
		int h = getHeight();

		int chartH = h - p * 2;
		int bottom = h - p;

		FontMetrics fm = g2.getFontMetrics();

		int labelCenterX = p - 20;
		// Y축
		g2.setColor(Color.GRAY);
		// g2.drawLine(p, p, p, bottom);

		g2.setStroke(new BasicStroke(0.5f));

		g2.setColor(new Color(180, 180, 180, 100));

		// 10점 단위 가로 점선
		for (int score = 0; score <= 100; score += 10) {
			int y = bottom - (chartH * score) / 100;
			g2.drawLine(p, y, w - p, y);

			// 점수 라벨
			g2.setColor(new Color(180, 180, 180, 150));
			g2.setFont(new Font("Arial", Font.BOLD, 15));

			int textWidth = fm.stringWidth(String.valueOf(score));
			int x = labelCenterX - textWidth / 2;
			int yText = y + fm.getAscent() / 2 - 2;
			g2.drawString(String.valueOf(score), x, yText);

			g2.setColor(new Color(180, 180, 180, 100));
		}
	}

	private void drawLine(Graphics2D g2) {
		if (scores.isEmpty())
			return;

		int p = 30;
		int w = getWidth();
		int h = getHeight();

		int chartW = w - p * 2;
		int chartH = h - p * 2;

		int maxScore = 100;

		g2.setColor(new Color(90, 160, 220));
		g2.setStroke(new BasicStroke(2.5f));

		if (scores.size() == 1) {
			int x = p;
			int y = h - p - (chartH * scores.get(0)) / maxScore;

			g2.setColor(new Color(90, 160, 220));
			g2.fillOval(x - 3, y - 3, 6, 6);

			g2.setColor(new Color(230, 230, 230));
			if(scores.get(0) == 0) 
				g2.drawString(String.valueOf(scores.get(0)), x - 5, y - 10);
			else
				g2.drawString(String.valueOf(scores.get(0)), x - 10, y - 10);
			
			return;
		}
		
		for (int i = 0; i < scores.size() - 1; i++) {
			int x1 = p + (chartW * i) / (scores.size() - 1);
			int y1 = h - p - (chartH * scores.get(i)) / maxScore;

			int x2 = p + (chartW * (i + 1)) / (scores.size() - 1);
			int y2 = h - p - (chartH * scores.get(i + 1)) / maxScore;

			g2.setColor(new Color(90, 160, 220));
			g2.drawLine(x1, y1, x2, y2);

			// 점 찍기
			g2.fillOval(x1 - 3, y1 - 3, 6, 6);

			g2.setFont(new Font("Arial", Font.BOLD, 15));
			g2.setColor(new Color(230, 230, 230));

			if(scores.get(i) == 0) 
				g2.drawString(String.valueOf(scores.get(i)), x1 - 5, y1 - 10);
			else
				g2.drawString(String.valueOf(scores.get(i)), x1 - 10, y1 - 10);
		}

		int last = scores.size() - 1;
		int x = p + (chartW * last) / (scores.size() - 1);
		int y = h - p - (chartH * scores.get(last)) / maxScore;
		g2.setColor(new Color(90, 160, 220));
		g2.fillOval(x - 3, y - 3, 6, 6);

		g2.setFont(new Font("Arial", Font.BOLD, 15));
		g2.setColor(new Color(230, 230, 230));

		if(scores.get(last) == 0) 
			g2.drawString(String.valueOf(scores.get(last)), x - 5, y - 10);
		else
			g2.drawString(String.valueOf(scores.get(last)), x - 10, y - 10);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		// 안티앨리어싱 (중요)
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		drawYAxisAndGrid(g2);
		drawLine(g2);

		g2.dispose();
	}
}
