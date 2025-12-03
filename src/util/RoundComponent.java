package util;

import javax.swing.*;
import java.awt.*;

public class RoundComponent<T extends JComponent> extends JComponent {

	private T inner;
	
	private Color borderColor;
	private Color bgColor;
	private Color fontColor;
	private String text;
	private Font font;
	private int radius;

	public RoundComponent(Class<T> cls, Dimension d, Color borderColor, Color bgColor, String text, Color fontColor, Font font, int radius) {
		try {
			this.inner = cls.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		this.borderColor = borderColor;
		this.bgColor = bgColor;
		this.fontColor = fontColor;
		this.text = text;
		this.font = font;
		this.radius = radius;
		
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		
		createInner();
		
		setLayout(new BorderLayout());
		setOpaque(false); // 직접 배경 그릴 거라 false

		add(inner, BorderLayout.CENTER);
	}
	
	public RoundComponent(Class<T> cls, Color borderColor, Color bgColor, String text, Color fontColor, Font font, int radius) {
		try {
			this.inner = cls.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		this.borderColor = borderColor;
		this.bgColor = bgColor;
		this.fontColor = fontColor;
		this.text = text;
		this.font = font;
		this.radius = radius;
		
		createInner();
		
		setLayout(new BorderLayout());
		setOpaque(false); // 직접 배경 그릴 거라 false

		add(inner, BorderLayout.CENTER);
	}
	
	public RoundComponent(Class<T> cls, Dimension d, Color borderColor, Color bgColor,int radius) {
		try {
			this.inner = cls.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		this.borderColor = borderColor;
		this.bgColor = bgColor;
		this.radius = radius;
		
		setPreferredSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
		
		createInner();
		
		setLayout(new BorderLayout());
		setOpaque(false);

		add(inner, BorderLayout.CENTER);
	}
	
	public RoundComponent(Class<T> cls, Color borderColor, Color bgColor,int radius) {
		try {
			this.inner = cls.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		this.borderColor = borderColor;
		this.bgColor = bgColor;
		this.radius = radius;
		
		createInner();
		
		setLayout(new BorderLayout());
		setOpaque(false);

		add(inner, BorderLayout.CENTER);
	}

	public T getInner() {
		return inner;
	}
	
	public Color getBackground() {
		return bgColor;
	}
	
	public String getText() {
		return text;
	}
	
	public void setBackground(Color color) {
		bgColor = color;
		repaint();
	}
	private void createInner() {
		if (inner instanceof JButton) {
			((JButton) inner).setBackground(bgColor);
			((JButton) inner).setText(text);
			((JButton) inner).setForeground(fontColor);
			((JButton) inner).setFont(font);

			((JButton) inner).setOpaque(false);
			((JButton) inner).setBorderPainted(false);
			((JButton) inner).setFocusPainted(false);
			((JButton) inner).setContentAreaFilled(false);
		} else if (inner instanceof JTextField) {
			((JTextField) inner).setBackground(bgColor);
			((JTextField) inner).setText(text);
			((JTextField) inner).setForeground(fontColor);
			((JTextField) inner).setFont(font);
			((JTextField) inner).setHorizontalAlignment(SwingConstants.CENTER);
			
			((JTextField) inner).setBorder(null);
			((JTextField) inner).setOpaque(false);
		} else if (inner instanceof JLabel) {
			((JLabel) inner).setBackground(bgColor);
			((JLabel) inner).setText(text);
			((JLabel) inner).setForeground(fontColor);
			((JLabel) inner).setFont(font);

			((JLabel) inner).setOpaque(false);
		} else if (inner instanceof JPanel) {
			((JPanel) inner).setBackground(bgColor);
			((JPanel) inner).setForeground(fontColor);
			((JPanel) inner).setFont(font);

			((JPanel) inner).setOpaque(false);
		}
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// 둥근 배경 채우기
		g2.setColor(bgColor);
		g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

		g2.dispose();
		super.paintComponent(g);
	}

	@Override
	protected void paintBorder(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(borderColor);
		g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

		g2.dispose();
	}
}
