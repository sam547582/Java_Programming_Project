package util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoundComponent<T extends JComponent> extends JComponent {

	private T inner;

	private Color borderColor;
	private Color bgColor;
	private Color fontColor;
	private String text;
	private Font font;
	private int radius;

	private boolean hover;

	public RoundComponent(Class<T> cls, Dimension d, Color borderColor, Color bgColor, String text, Color fontColor,
			Font font, int radius) {
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

		inner.setPreferredSize(d);
		inner.setMaximumSize(d);
		inner.setMinimumSize(d);

		setLayout(new BorderLayout());
		setOpaque(false); // 직접 배경 그릴 거라 false

		add(inner, BorderLayout.CENTER);
	}

	public RoundComponent(Class<T> cls, Color borderColor, Color bgColor, String text, Color fontColor, Font font,
			int radius) {
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

	public RoundComponent(Class<T> cls, Dimension d, Color borderColor, Color bgColor, int radius) {
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

		inner.setPreferredSize(d);
		inner.setMaximumSize(d);
		inner.setMinimumSize(d);

		setLayout(new BorderLayout());
		setOpaque(false);

		add(inner, BorderLayout.CENTER);
	}

	public RoundComponent(Class<T> cls, Color borderColor, Color bgColor, int radius) {
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
			((JButton) inner).setRolloverEnabled(false);
			((JButton) inner).setContentAreaFilled(false);
			((JButton) inner).addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					hover = true;
					((JButton) inner).setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					repaint();
				}

				@Override
				public void mouseExited(MouseEvent e) {
					hover = false;
					((JButton) inner).setCursor(Cursor.getDefaultCursor());
					repaint();
				}
			});

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

		if (hover) {
			g2.setColor(ColorUtils.getShadowColor(bgColor));
			inner.setForeground(ColorUtils.getShadowColor(fontColor));
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
		} else {
			g2.setColor(bgColor);
			inner.setForeground(fontColor);
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
		}

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

	@Override
	public boolean contains(int x, int y) {
		int width = getWidth();
		int height = getHeight();

		Shape round = new java.awt.geom.RoundRectangle2D.Float(0, 0, width, height, radius, radius);

		return round.contains(x, y);
	}

}
