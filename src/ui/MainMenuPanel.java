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

	public MainMenuPanel(MainFrame frame) {
		this.frame = frame;

		setLayout(new BorderLayout());
		setOpaque(true);

		setBackground(new Color(60, 45, 85));

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

		RoundComponent<JPanel> leftMenu = new RoundComponent<>(JPanel.class, new Dimension(400, 550),
				new Color(0, 0, 0, 0), new Color(0, 0, 0, 140), 20);
		leftMenu.getInner().setLayout(new BoxLayout(leftMenu.getInner(), BoxLayout.Y_AXIS));

		leftMenu.getInner().add(Box.createVerticalStrut(50));

		JPanel westWrapper = new JPanel(new GridBagLayout());
		westWrapper.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weighty = 1.0;
		westWrapper.add(leftMenu,gbc);

		JLabel title = new JLabel("KICE MATH TRAINING", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 35));
		title.setForeground(Color.WHITE);

		MenuLabel start = new MenuLabel("Training");
		start.setForeground(new Color(60, 150, 210));

		start.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				frame.showSubject();
			}
		});

		leftMenu.getInner().add(wrapLabel(start));
		leftMenu.getInner().add(Box.createVerticalStrut(20));

		MenuLabel test = new MenuLabel("Test");
		test.setForeground(new Color(190, 45, 60));
		test.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				showCountdownDialog();
			}
		});

		leftMenu.getInner().add(wrapLabel(test));
		leftMenu.getInner().add(Box.createVerticalStrut(20));

		leftMenu.getInner().add(wrapLabel(new MenuLabel("Settings")));
		leftMenu.getInner().add(Box.createVerticalStrut(20));

		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
		right.setOpaque(false);
		right.add(Box.createVerticalStrut(60));
		right.add(played);
		right.add(accuracy);
		right.add(correct);
		right.add(wrong);

		add(title, BorderLayout.NORTH);
		add(westWrapper, BorderLayout.WEST);
		add(right, BorderLayout.EAST);

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
		int[] time = { 10 };

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
