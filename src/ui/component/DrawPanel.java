package ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import util.*;

public class DrawPanel extends JPanel {
	/* * ========================= 1. MODE ========================= */ private enum Mode {
		PEN, LINE, ERASER
	}

	private Mode mode = Mode.PEN;

	/* * ========================= 2. COMMAND STRUCTURE ========================= */ private interface DrawCommand {
		void draw(Graphics2D g, Color backColor, Color penColor);
	}

	private static class StrokeCommand implements DrawCommand {
		boolean eraser;
		boolean dependent;
		Color color;
		float size;
		List<Point> points = new ArrayList<>();

		StrokeCommand(boolean eraser, boolean dependent, Color color, float size) {
			this.eraser = eraser;
			this.dependent = dependent;
			this.color = color;
			this.size = size;
		}

		@Override
		public void draw(Graphics2D g, Color backColor, Color penColor) {
			if (points.size() < 2)
				return;

			g.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

			Color drawColor;
			if (dependent) {
				drawColor = ColorUtils.getContrastColor(backColor);
			} else {
				drawColor = color;
			}

			if (eraser) {
				g.setComposite(AlphaComposite.Clear);
			} else {
				g.setColor(drawColor);
			}

			for (int i = 1; i < points.size(); i++) {
				Point p1 = points.get(i - 1);
				Point p2 = points.get(i);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
			g.setComposite(AlphaComposite.SrcOver);
		}
	}

	private static class LineCommand implements DrawCommand {
		boolean dependent;
		Point start, end;
		Color color;
		float size;

		LineCommand(Point s, Point e, boolean dependent, Color color, float size) {
			start = s;
			end = e;
			this.dependent = dependent;
			this.color = color;
			this.size = size;
		}

		@Override
		public void draw(Graphics2D g, Color backColor, Color penColor) {
			g.setStroke(new BasicStroke(size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

			Color drawColor;
			if (dependent) {
				drawColor = ColorUtils.getContrastColor(backColor);
			} else {
				drawColor = color;
			}

			g.setColor(drawColor);
			g.drawLine(start.x, start.y, end.x, end.y);
		}
	}

	/* * ========================= 3. STATE ========================= */ private List<DrawCommand> commands = new ArrayList<>();
	private Stack<DrawCommand> redo = new Stack<>();
	private StrokeCommand currentStroke;
	private float penSize = 2.0f;
	private Color backColor = Color.WHITE;
	private Color penColor = Color.BLACK;
	/* * ========================= 4. UI ========================= */ private JLabel label;
	private RoundComponent<JButton> penBtn, lineBtn, eraserBtn, colorBtn, undoBtn, redoBtn;
	/* * ========================= 5. LINE PREVIEW ========================= */ private int startX, startY, currentX,
			currentY;
	private boolean isPreviewingLine = false;

	/* * ========================= 6. CONSTRUCTOR ========================= */ public DrawPanel() {
		setLayout(new BorderLayout());
		setOpaque(false);
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		label = new JLabel("Draw Mode");
		label.setFont(new Font("Arial", Font.BOLD, 30));
		label.setForeground(penColor);
		penBtn = new RoundComponent<>(JButton.class, new Dimension(100, 50), new Color(0, 0, 0, 0),
				new Color(80, 85, 95), "PEN", Color.WHITE, new Font("Arial", Font.BOLD, 20), 20);
		lineBtn = new RoundComponent<>(JButton.class, new Dimension(100, 50), new Color(0, 0, 0, 0),
				new Color(70, 100, 160), "LINE", Color.WHITE, new Font("Arial", Font.BOLD, 20), 20);
		eraserBtn = new RoundComponent<>(JButton.class, new Dimension(120, 50), new Color(0, 0, 0, 0),
				new Color(160, 80, 80), "ERASER", Color.WHITE, new Font("Arial", Font.BOLD, 20), 20);
		colorBtn = new RoundComponent<>(JButton.class, new Dimension(120, 50), new Color(0, 0, 0, 0),
				new Color(140, 120, 170), "COLOR", Color.WHITE, new Font("Arial", Font.BOLD, 20), 20);
		undoBtn = new RoundComponent<>(JButton.class, new Dimension(100, 50), new Color(0, 0, 0, 0),
				new Color(90, 130, 120), "UNDO", Color.WHITE, new Font("Arial", Font.BOLD, 20), 20);
		redoBtn = new RoundComponent<>(JButton.class, new Dimension(100, 50), new Color(0, 0, 0, 0),
				new Color(170, 140, 90), "REDO", Color.WHITE, new Font("Arial", Font.BOLD, 20), 20);
		penBtn.getInner().addActionListener(e -> mode = Mode.PEN);
		lineBtn.getInner().addActionListener(e -> mode = Mode.LINE);
		eraserBtn.getInner().addActionListener(e -> mode = Mode.ERASER);
		colorBtn.getInner().addActionListener(e -> {
			Color c = JColorChooser.showDialog(this, "Choose Color", penColor);
			if (c != null) {
				penColor = c;
				label.setForeground(c);
			}
		});
		undoBtn.getInner().addActionListener(e -> undo());
		redoBtn.getInner().addActionListener(e -> redo());
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setOpaque(false);
		toolBar.add(penBtn);
		toolBar.add(Box.createHorizontalStrut(10));
		toolBar.add(lineBtn);
		toolBar.add(Box.createHorizontalStrut(10));
		toolBar.add(eraserBtn);
		toolBar.add(Box.createHorizontalStrut(10));
		toolBar.add(colorBtn);
		toolBar.add(Box.createHorizontalStrut(10));
		toolBar.add(undoBtn);
		toolBar.add(Box.createHorizontalStrut(10));
		toolBar.add(redoBtn);
		JPanel bottom = new JPanel(new FlowLayout());
		bottom.setOpaque(false);
		bottom.add(toolBar);
		add(label, BorderLayout.NORTH);
		add(bottom, BorderLayout.SOUTH);
	}

	boolean isThemeColor(Color c) {
		return c.equals(Color.BLACK) || c.equals(Color.WHITE);
	}

	/* * ========================= 7. PAINT ========================= */ @Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (DrawCommand cmd : commands) {
			cmd.draw(g2d, backColor, penColor);
		}

		if (mode == Mode.LINE && isPreviewingLine) {
			g2d.setStroke(new BasicStroke(penSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g2d.setColor(penColor);
			g2d.drawLine(startX, startY, currentX, currentY);
		}
	}

	/* * ========================= 8. MOUSE ========================= */ private MouseAdapter mouseAdapter = new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent e) {
			if (!isVisible())
				return;
			if (mode == Mode.PEN || mode == Mode.ERASER) {
				currentStroke = new StrokeCommand(mode == Mode.ERASER, isThemeColor(penColor), penColor, penSize);
				currentStroke.points.add(e.getPoint());
				commands.add(currentStroke);
				redo.clear();
			} else if (mode == Mode.LINE) {
				startX = e.getX();
				startY = e.getY();
				currentX = startX;
				currentY = startY;
				isPreviewingLine = true;
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (!isVisible())
				return;
			if (mode == Mode.PEN || mode == Mode.ERASER) {
				currentStroke.points.add(e.getPoint());
				repaint();
			} else if (mode == Mode.LINE) {
				currentX = e.getX();
				currentY = e.getY();
				repaint();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (mode == Mode.LINE && isPreviewingLine) {
				commands.add(new LineCommand(new Point(startX, startY), e.getPoint(), isThemeColor(penColor), penColor,
						penSize));
				redo.clear();
				isPreviewingLine = false;
				repaint();
			}
		}
	};

	/* * ========================= 9. UNDO / REDO ========================= */ private void undo() {
		if (commands.isEmpty())
			return;
		redo.push(commands.remove(commands.size() - 1));
		repaint();
	}

	private void redo() {
		if (redo.isEmpty())
			return;
		commands.add(redo.pop());
		repaint();
	}

	/* * ========================= 10. THEME CHANGE ========================= */ public void updateColor(Color bg) {
		backColor = bg;
		if (penColor.equals(Color.BLACK) || penColor.equals(Color.WHITE))
			penColor = ColorUtils.getContrastColor(bg);
		label.setForeground(penColor);
		repaint();
	}
}