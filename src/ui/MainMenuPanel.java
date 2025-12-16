package ui;

import javax.imageio.ImageIO;
import javax.swing.*;

import ui.component.MenuLabel;

import java.awt.*;
import util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import model.*;

public class MainMenuPanel extends JPanel {

	private MainFrame frame;

	private JLabel played;
	private JLabel accuracy;
	private JLabel correct;
	private JLabel wrong;

	private JPanel rightWrapper;

	private RoundComponent<JPanel> rightTop;
	private RoundComponent<JPanel> rightBottom;

	public MainMenuPanel(MainFrame frame) {
		this.frame = frame;

		setLayout(new BorderLayout());
		setOpaque(false);

		setBackground(new Color(45, 50, 58));

		ProblemStatsManager.syncStats();
		TestStatsManager.syncStats();
		StatsManager.load();

		played = new JLabel();
		accuracy = new JLabel();
		correct = new JLabel();
		wrong = new JLabel();

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				refreshStats();
			}
		});

		refreshStats();

		RoundComponent<JPanel> leftMenu = new RoundComponent<>(JPanel.class, new Dimension(400, 575),
				new Color(0, 0, 0, 0), new Color(0, 0, 0, 140), 20);
		leftMenu.getInner().setLayout(new BoxLayout(leftMenu.getInner(), BoxLayout.Y_AXIS));

		leftMenu.getInner().add(Box.createVerticalStrut(50));

		JPanel westWrapper = new JPanel(new GridBagLayout());
		westWrapper.setOpaque(false);

		GridBagConstraints gbcL = new GridBagConstraints();
		gbcL.anchor = GridBagConstraints.NORTH;
		gbcL.weighty = 1.0;
		westWrapper.add(leftMenu, gbcL);

		JLabel title = new JLabel("KICE MATH TRAINING", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 35));
		title.setForeground(Color.WHITE);

		MenuLabel start = new MenuLabel("Training");
		start.setHoverColor(new Color(60, 150, 210));

		start.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				frame.showSubject();
			}
		});

		leftMenu.getInner().add(wrapLabel(start));
		leftMenu.getInner().add(Box.createVerticalStrut(20));

		MenuLabel test = new MenuLabel("Test");
		test.setHoverColor(new Color(190, 45, 60));
		test.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				showCountdownDialog();
			}
		});

		leftMenu.getInner().add(wrapLabel(test));
		leftMenu.getInner().add(Box.createVerticalStrut(20));
		
		MenuLabel setting = new MenuLabel("Settings");
		setting.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				frame.showPanel("setting");
			}
		});
		leftMenu.getInner().add(wrapLabel(setting));
		leftMenu.getInner().add(Box.createVerticalStrut(20));

		rightWrapper = new JPanel(new GridBagLayout());
		rightWrapper.setOpaque(false);

		GridBagConstraints gbcR = new GridBagConstraints();
		gbcR.gridx = 0;
		gbcR.gridy = 0;
		gbcR.anchor = GridBagConstraints.NORTH;
		gbcR.weighty = 1.0;

		createStatisticsPanel();

		rightWrapper.add(rightTop, gbcR);

		createChartPanel();

		gbcR.gridy = 1;
		rightWrapper.add(rightBottom, gbcR);

		add(title, BorderLayout.NORTH);
		add(westWrapper, BorderLayout.WEST);
		add(rightWrapper, BorderLayout.EAST);

		JPanel emptyRight = new JPanel();
		emptyRight.setOpaque(false);
		add(emptyRight, BorderLayout.CENTER);

	}

	private JPanel wrapLabel(MenuLabel label) {
		JPanel panel = new JPanel(null);
		panel.setOpaque(false);

		panel.setPreferredSize(new Dimension(500, 70));
		panel.setMaximumSize(new Dimension(500, 70));

		Dimension d = label.getPreferredSize();
		label.setBounds(0, 0, d.width + 5, d.height);

		panel.add(label);

		return panel;
	}

	private void createStatisticsPanel() {
		rightTop = new RoundComponent<>(JPanel.class, new Dimension(450, 150), new Color(0, 0, 0, 0),
				new Color(0, 0, 0, 140), 20);
		rightTop.getInner().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.CENTER;

		JLabel label = new JLabel("Statistics");
		label.setFont(new Font("Arial", Font.BOLD, 30));
		label.setForeground(Color.WHITE);

		rightTop.getInner().add(label, c);
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.WEST;

		rightTop.getInner().add(played, c);
		c.gridy = 2;
		rightTop.getInner().add(accuracy, c);
		c.gridy = 3;
		rightTop.getInner().add(correct, c);
		c.gridy = 4;
		rightTop.getInner().add(wrong, c);
	}

	private void createChartPanel() {
		rightBottom = new RoundComponent<>(JPanel.class, new Dimension(450, 400), new Color(0, 0, 0, 0),
				new Color(0, 0, 0, 140), 20);
		rightBottom.getInner().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.CENTER;

		JLabel label = new JLabel("TEST");
		label.setFont(new Font("Arial", Font.BOLD, 30));
		label.setForeground(Color.WHITE);

		LineChartPanel chart = new LineChartPanel();

		rightBottom.getInner().add(label, c);
		c.gridx = 0;
		c.gridy = 1;

		rightBottom.getInner().add(chart, c);
	}

	private void refreshStats() {
		played.setText("Solved : " + StatsManager.getTotalPlayed());
		played.setFont(new Font("Arial", Font.BOLD, 20));
		played.setForeground(ColorUtils.getContrastColor(getBackground()));

		accuracy.setText("Accuracy : " + String.format("%.2f", StatsManager.getAccuracy()) + "%");
		accuracy.setFont(new Font("Arial", Font.BOLD, 20));
		accuracy.setForeground(ColorUtils.getContrastColor(getBackground()));

		correct.setText("Correct : " + StatsManager.getCorrect());
		correct.setFont(new Font("Arial", Font.BOLD, 20));
		correct.setForeground(ColorUtils.getContrastColor(getBackground()));

		wrong.setText("Wrong : " + StatsManager.getWrong());
		wrong.setFont(new Font("Arial", Font.BOLD, 20));
		wrong.setForeground(ColorUtils.getContrastColor(getBackground()));
		revalidate();
		repaint();
	}

	/*
	 * @Override protected void paintComponent(Graphics g) { Graphics2D g2 =
	 * (Graphics2D) g.create(); GradientPaint gp = new GradientPaint(0, 0, new
	 * Color(35,45,65), 0, getHeight(), new Color(20,25,40)); g2.setPaint(gp);
	 * g2.fillRect(0, 0, getWidth(), getHeight()); g2.dispose(); }
	 */

	private void showCountdownDialog() {

		// 모달 다이얼로그 (부모 Frame 고정)
		JDialog dialog = new JDialog(frame, "Start", true);
		dialog.setSize(300, 150);
		dialog.setLocationRelativeTo(frame); // 화면 가운데
		dialog.setLayout(new BorderLayout());

		JLabel label = new JLabel("Starts in 10 seconds", SwingConstants.CENTER);
		label.setFont(new Font("Arial", Font.BOLD, 20));
		dialog.add(label, BorderLayout.CENTER);

		// 5초 카운트다운 변수 / 배열 선언 원인 체크하기
		int[] time = { 1 };

		// 1초마다 실행되는 Swing Timer
		Timer timer = new Timer(1000, null);

		dialog.addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				timer.stop();
				// 필요하면 “취소됨” 표시도 가능
				// JOptionPane.showMessageDialog(frame, "취소되었습니다.");
			}
		});

		timer.addActionListener(e -> {

			time[0]--;

			if (time[0] > 0) {
				label.setText("Starts in " + time[0] + " seconds");
			} else {
				// 타이머 종료 + 다이얼로그 종료
				timer.stop();
				dialog.dispose();

				// 화면 전환 실행
				frame.showTest(StatsManager.getElective()); // 원하는 화면으로 이동
			}
		});

		timer.start();
		dialog.setVisible(true); // 다이얼로그 표시 (모달)
	}

}
