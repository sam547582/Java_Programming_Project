package ui;

import javax.swing.*;
import java.awt.*;
import model.*;
import ui.component.MenuLabel;
import ui.component.ResponsiveLabel;
import util.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class ResultPanel extends JPanel {

	private MainFrame frame;

	private Problem[] problems;

	private JPanel top;
	private JPanel topLeftWrapper;
	private JPanel topRightWrapper;
	private JPanel topBottomWrapper;

	private JPanel center;

	private ArrayList<JPanel> rowPanels;

	private JPanel bottom;

	private JLabel menu;

	private JLabel resultLabel;
	private JLabel correctLabel;
	private JLabel wrongLabel;
	private JLabel scoreLabel;
	private JLabel[] ox;

	private String what;
	private int correct;
	private int wrong;
	private int problemSize;

	ResultPanel(MainFrame frame, Problem[] problems, String what) {
		this.frame = frame;
		this.problems = problems;
		this.what = what;
		rowPanels = new ArrayList<>();
		problemSize = problems.length;
		ox = new JLabel[problemSize];
		correct = wrong = 0;

		setLayout(new BorderLayout());
		setBackground(Color.GRAY);

		if (what.equals("test")) {
			frame.setSize(1200, 850);
		} else if (what.equals("problem")) {
			frame.setSize(1000, 550);
		}

		checkAnswer();
		StatsManager.updateStats(correct, wrong);

		resultLabel = new JLabel("RESULT");
		resultLabel.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 50));
		resultLabel.setForeground(ColorUtils.getContrastColor(getBackground()));
		Dimension d = resultLabel.getPreferredSize();
		resultLabel.setPreferredSize(new Dimension(d.width + 10, d.height));

		menu = new JLabel("MENU");
		menu.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 50));
		menu.setForeground(ColorUtils.getContrastColor(getBackground()));
		d = menu.getPreferredSize();
		menu.setPreferredSize(new Dimension(d.width + 10, d.height));
		menu.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				frame.setLocationRelativeTo(null);
				frame.showPanel("menu");
				frame.setSize(900, 600);
			}

			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				menu.setForeground(Color.BLACK);
				menu.repaint();
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				menu.setForeground(Color.WHITE);
				menu.repaint();
			}
		});

		correctLabel = new JLabel("CORRECT : " + String.valueOf(correct));
		correctLabel.setFont(new Font("Arial", Font.BOLD, 40));
		correctLabel.setForeground(ColorUtils.getContrastColor(getBackground()));
		correctLabel.setVisible(false);

		wrongLabel = new JLabel("WRONG : " + String.valueOf(wrong));
		wrongLabel.setFont(new Font("Arial", Font.BOLD, 40));
		wrongLabel.setForeground(ColorUtils.getContrastColor(getBackground()));
		wrongLabel.setVisible(false);

		JLabel label = new JLabel("SCORE");
		label.setFont(new Font("Arial", Font.BOLD, 50));
		label.setForeground(Color.WHITE);

		scoreLabel = new JLabel(String.valueOf(TestStatsManager.getScore(TestManager.getSelectedName())));
		scoreLabel.setFont(new Font("Arial", Font.BOLD, 50));
		scoreLabel.setForeground(Color.GREEN);
		scoreLabel.setVisible(false);

		topLeftWrapper = new JPanel(new FlowLayout());
		topLeftWrapper.setOpaque(false);
		topLeftWrapper.add(resultLabel);

		topRightWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		topRightWrapper.setOpaque(false);
		topRightWrapper.add(menu);

		topBottomWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		topBottomWrapper.setOpaque(false);
		if (what.equals("test")) {
			topBottomWrapper.add(label);
			topBottomWrapper.add(scoreLabel);
		}

		top = new JPanel(new BorderLayout());
		top.setOpaque(false);
		top.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		top.add(topLeftWrapper, BorderLayout.WEST);
		top.add(topRightWrapper, BorderLayout.EAST);
		top.add(topBottomWrapper, BorderLayout.SOUTH);

		center = new JPanel(new GridBagLayout());
		center.setOpaque(false);

		GridBagConstraints c = new GridBagConstraints();

		int gridx = 0;

		int cnt = 0;
		int start = 0;

		if (what.equals("problem")) {
			cnt = 5;
		} else if (what.equals("test")) {
			cnt = 10;
		}

		while (start < problemSize) {
			JPanel table = createTable(start, start + cnt);
			table.setOpaque(false);

			c.gridx = gridx++;
			c.gridy = 0;
			c.weightx = 1.0;
			c.fill = GridBagConstraints.BOTH;
			c.weighty = 1.0;
			center.add(table, c);

			start += cnt;

			if (start < problemSize) {
				GridBagConstraints sepC = new GridBagConstraints();
				sepC.gridx = gridx++;
				sepC.gridy = 0;
				sepC.weightx = 0;
				sepC.weighty = 0;
				sepC.fill = GridBagConstraints.NONE;

				center.add(createVerticalSeparator(table.getPreferredSize().height + 15), sepC);
			}
		}

		add(center, BorderLayout.CENTER);

		bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
		bottom.setOpaque(false);
		bottom.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
		bottom.setPreferredSize(new Dimension(300, 80));
		bottom.setMaximumSize(new Dimension(300, 80));
		bottom.setMinimumSize(new Dimension(300, 80));
		bottom.add(correctLabel);
		bottom.add(wrongLabel);

		add(top, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
		
		frame.setLocationRelativeTo(null);
		SwingUtilities.invokeLater(() -> startRowAnimation());
	}

	private void startRowAnimation() {
		final int delay = 150;

		final int[] index = { 0 };

		Timer timer = new Timer(delay, e -> {
			if (index[0] < rowPanels.size()) {
				JPanel row = rowPanels.get(index[0]);
				row.setVisible(true);
				row.revalidate();
				row.repaint();
				index[0]++;
			} else {
				correctLabel.setVisible(true);
				wrongLabel.setVisible(true);
				scoreLabel.setVisible(true);
				((Timer) e.getSource()).stop();
			}
		});

		timer.start();
	}

	private JPanel createVerticalSeparator(int height) {

		JPanel wrapper = new JPanel();
		wrapper.setOpaque(false);
		wrapper.setPreferredSize(new Dimension(2, height));
		wrapper.setMinimumSize(new Dimension(2, height));
		wrapper.setMaximumSize(new Dimension(2, height));

		JPanel sep = new JPanel();
		sep.setBackground(new Color(180, 180, 180));
		sep.setPreferredSize(new Dimension(2, height));
		sep.setMinimumSize(new Dimension(2, height));
		sep.setMaximumSize(new Dimension(2, height));

		wrapper.add(sep);

		return wrapper;
	}

	private JPanel createTable(int start, int end) {

		JPanel table = new JPanel(new GridBagLayout());
		table.setOpaque(false);

		GridBagConstraints base = new GridBagConstraints();
		base.fill = GridBagConstraints.BOTH;
		base.gridx = 0;
		base.gridy = 0;
		base.weighty = 0;

		JPanel header = new JPanel(new GridBagLayout());
		header.setOpaque(false);

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0;

		addHeaderCell(header, "NUM", 0, 0, 0.2, c);
		addSeparator(header, 0, 1, c);
		addHeaderCell(header, "YOUR ANS", 0, 2, 0.3, c);
		addSeparator(header, 0, 3, c);
		addHeaderCell(header, "ANS", 0, 4, 0.2, c);
		addSeparator(header, 0, 5, c);
		addHeaderCell(header, "O/X", 0, 6, 0.2, c);

		table.add(header, base);

		for (int row = start; row < end; row++) {
			int y = (row - start) + 1;

			JPanel wrapper = new JPanel(new BorderLayout());
			wrapper.setOpaque(false);
			wrapper.setMinimumSize(new Dimension(0, 50));
			wrapper.setPreferredSize(new Dimension(0, 50));
			wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

			JPanel rowPanel = new JPanel(new GridBagLayout());
			rowPanel.setOpaque(false);
			rowPanel.setVisible(false);

			if (row >= problemSize) {
				addRowCell(rowPanel, " ", 0, 0, 0.2, c);
				addSeparator(rowPanel, 0, 1, c);

				addRowCell(rowPanel, " ", 0, 2, 0.3, c);
				addSeparator(rowPanel, 0, 3, c);

				addRowCell(rowPanel, " ", 0, 4, 0.2, c);
				addSeparator(rowPanel, 0, 5, c);

				addRowCell(rowPanel, " ", 0, 6, 0.2, c);

				wrapper.add(rowPanel, BorderLayout.CENTER);

				rowPanels.add(rowPanel);

				GridBagConstraints r = new GridBagConstraints();
				r.fill = GridBagConstraints.BOTH;
				r.gridx = 0;
				r.weightx = 1.0;
				r.gridy = y;
				table.add(wrapper, r);
				continue;
			}

			addRowCell(rowPanel, String.valueOf(row + 1), 0, 0, 0.2, c);
			addSeparator(rowPanel, 0, 1, c);

			addRowCell(rowPanel, problems[row].getPlayerAnswer(), 0, 2, 0.3, c);
			addSeparator(rowPanel, 0, 3, c);

			addRowCell(rowPanel, problems[row].getAnswer(), 0, 4, 0.2, c);
			addSeparator(rowPanel, 0, 5, c);

			addRowCell(rowPanel, ox[row].getText(), 0, 6, 0.2, c);

			wrapper.add(rowPanel, BorderLayout.CENTER);

			rowPanels.add(rowPanel);

			GridBagConstraints r = new GridBagConstraints();
			r.fill = GridBagConstraints.BOTH;
			r.gridx = 0;
			r.weightx = 1.0;
			r.gridy = y;
			table.add(wrapper, r);
		}

		return table;
	}

	private JPanel makeCellWrapper(JLabel label) {
		JPanel wrap = new JPanel(new BorderLayout());
		wrap.setOpaque(false);
		wrap.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		wrap.setMinimumSize(new Dimension(0, 50));
		wrap.setPreferredSize(new Dimension(0, 50));
		wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

		wrap.add(label);
		return wrap;
	}

	private void addHeaderCell(JPanel table, String text, int y, int x, double weightx, GridBagConstraints base) {
		GridBagConstraints c = (GridBagConstraints) base.clone();
		c.gridx = x;
		c.gridy = y;
		c.weightx = weightx;
		c.weighty = 0;

		ResponsiveLabel label = new ResponsiveLabel(text, 0.05f, table);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Arial", Font.BOLD, 30));
		label.setForeground(ColorUtils.getContrastColor(getBackground()));

		JPanel wrap = makeCellWrapper(label);

		table.add(wrap, c);
	}

	private void addRowCell(JPanel table, String text, int y, int x, double weightx, GridBagConstraints base) {
		GridBagConstraints c = (GridBagConstraints) base.clone();
		c.gridx = x;
		c.gridy = y;
		c.weightx = weightx;
		c.weighty = 1;

		ResponsiveLabel label = new ResponsiveLabel(text, 0.04f, table);
		label.setFont(new Font("Arial", Font.BOLD, 26));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(ColorUtils.getContrastColor(getBackground()));

		if (text.equals("X")) {
			label.setForeground(Color.RED);
		} else if (text.equals("O")) {
			label.setForeground(Color.BLUE);
		}

		JPanel wrap = makeCellWrapper(label);

		table.add(wrap, c);
	}

	private void addSeparator(JPanel table, int y, int x, GridBagConstraints base) {
		GridBagConstraints c = (GridBagConstraints) base.clone();
		c.gridx = x;
		c.gridy = y;
		c.weightx = 0;
		c.weighty = 1;
		c.fill = GridBagConstraints.NONE;

		JPanel sep = new JPanel();
		sep.setBackground(new Color(180, 180, 180));
		sep.setPreferredSize(new Dimension(1, 25));
		table.add(sep, c);
	}

	private void checkAnswer() {

		Map<String, int[]> stats = null;
		int score = 0;

		if (what.equals("problem"))
			stats = ProblemStatsManager.loadProblemStats();
		else if (what.equals("test"))
			stats = TestStatsManager.loadTestStats();

		int cnt = 0;

		for (Problem p : problems) {
			String key = p.getPath();
			int cr = p.getSolveCount();
			int wr = p.getWrongCount();

			if (p.getPlayerAnswer().equals(p.getAnswer())) {
				ox[cnt] = new JLabel("O");

				if (what.equals("problem")) {
					p.setSolveCount(cr + 1);
					stats.get(key)[0] = p.getSolveCount();
				} else if (what.equals("test"))
					score += p.getScore();
				correct++;
			} else {
				ox[cnt] = new JLabel("X");

				if (what.equals("problem")) {
					p.setWrongCount(wr + 1);
					stats.get(key)[1] = p.getWrongCount();
				}
				wrong++;
			}
			cnt++;
		}

		if (what.equals("test")) {
			stats.get(TestManager.getSelectedName())[0]++;
			stats.get(TestManager.getSelectedName())[1] = score;

			TestStatsManager.saveTestStats(stats);
		} else if (what.equals("problem")) {
			ProblemStatsManager.saveProblemStats(stats);
		}

	}
}
