package ui;

import javax.imageio.ImageIO;
import javax.swing.*;

import ui.component.LineChartPanel;
import ui.component.MenuLabel;

import java.awt.*;
import util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import model.*;

public class MainMenuPanel extends JPanel {

	private MainFrame frame;

	private JLabel grade;
	private JLabel accuracy;
	private JLabel today;
	private JLabel elective;
	private JLabel ox;
	private JLabel recommend;

	private JPanel rightWrapper;

	private RoundComponent<JPanel> rightTop;
	private RoundComponent<JPanel> rightBottom;

	public MainMenuPanel(MainFrame frame) {
		this.frame = frame;
		
		frame.setResizable(false);
		frame.enableExit();
		
		setLayout(new BorderLayout());
		setOpaque(true);

		setBackground(new Color(45, 50, 58));

		ProblemStatsManager.syncStats();
		TestStatsManager.syncStats();
		StatsManager.load();

		grade = new JLabel();
		accuracy = new JLabel();
		today = new JLabel();
		ox = new JLabel();
		elective = new JLabel();
		recommend = new JLabel();

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				refreshStats();
			}
		});

		refreshStats();

		RoundComponent<JPanel> leftMenu = new RoundComponent<>(JPanel.class, new Dimension(400, 668),
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
		title.setFont(new Font("Arial", Font.BOLD, 45));
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
		setting.setHoverColor(new Color(80, 92, 104));
		setting.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				frame.showPanel("setting");
			}
		});
		leftMenu.getInner().add(wrapLabel(setting));
		leftMenu.getInner().add(Box.createVerticalStrut(20));

		MenuLabel exit = new MenuLabel("Exit");
		exit.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (JOptionPane.showConfirmDialog(null, "Real Exit?", "EXIT", JOptionPane.YES_NO_OPTION) == 0) {
					System.exit(0);
				}
			}
		});
		leftMenu.getInner().add(wrapLabel(exit));
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
		rightTop = new RoundComponent<>(JPanel.class, new Dimension(530, 230), new Color(0, 0, 0, 0),
				new Color(0, 0, 0, 140), 20);
		rightTop.getInner().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.insets = new Insets(0, 10, 10, 0);
		c.weightx = 1.0;
		c.anchor = GridBagConstraints.CENTER;

		JLabel label = new JLabel("HI!   " + StatsManager.getName());
		label.setFont(new Font("Arial", Font.BOLD, 30));
		label.setForeground(new Color(240, 240, 240));

		JLabel labelToday = new JLabel("Today Training : ");
		labelToday.setFont(new Font("Arial", Font.BOLD, 20));
		labelToday.setForeground(new Color(160, 165, 170));
		JLabel labelElective = new JLabel("Elective : ");
		labelElective.setFont(new Font("Arial", Font.BOLD, 20));
		labelElective.setForeground(new Color(160, 165, 170));
		JLabel labelGrade = new JLabel("Target Grade : ");
		labelGrade.setFont(new Font("Arial", Font.BOLD, 20));
		labelGrade.setForeground(new Color(160, 165, 170));

		rightTop.getInner().add(label, c);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		c.insets = new Insets(5, 10, 0, 0);
		rightTop.getInner().add(labelToday, c);
		c.gridy = 3;
		rightTop.getInner().add(labelElective, c);
		c.gridy = 4;
		rightTop.getInner().add(labelGrade, c);

		c.gridx = 1;
		c.gridy = 2;
		c.anchor = GridBagConstraints.WEST;
		rightTop.getInner().add(today, c);
		c.gridy = 3;
		rightTop.getInner().add(elective, c);
		c.gridy = 4;
		rightTop.getInner().add(grade, c);

		c.gridx = 0;
		c.gridy = 6;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(10, 0, 0, 0);
		rightTop.getInner().add(recommend, c);

		c.insets = new Insets(5, 0, 0, 40);
		c.anchor = GridBagConstraints.EAST;
		c.gridwidth = 1;
		c.gridx = 2;
		c.gridy = 2;
		rightTop.getInner().add(ox, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 3;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5, 10, 5, 10);
		rightTop.getInner().add(createSeparator(), c);
		c.gridy = 5;
		rightTop.getInner().add(createSeparator(), c);

	}

	private void createChartPanel() {
		rightBottom = new RoundComponent<>(JPanel.class, new Dimension(530, 400), new Color(0, 0, 0, 0),
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

		if (StatsManager.getTodayTraining() != 0) {
			ox.setText("NICE");
			ox.setFont(new Font("Arial", Font.BOLD, 20));
			ox.setForeground(new Color(120, 190, 120));
		} else {
			ox.setText("Go Training!!");
			ox.setFont(new Font("Arial", Font.BOLD, 20));
			ox.setForeground(new Color(200, 90, 90));
		}

		if (StatsManager.getTargetGrade() == 1) {
			recommend.setText("Recommend KILLER & Hard");
			recommend.setFont(new Font("Arial", Font.BOLD, 30));
			recommend.setForeground(new Color(200, 80, 80));
		} else if (StatsManager.getTargetGrade() == 2) {
			recommend.setText("Recommend Hard");
			recommend.setFont(new Font("Arial", Font.BOLD, 30));
			recommend.setForeground(new Color(200, 160, 90));
		} else if (StatsManager.getTargetGrade() == 3) {
			recommend.setText("Recommend Hard & Normal");
			recommend.setFont(new Font("Arial", Font.BOLD, 30));
			recommend.setForeground(new Color(140, 170, 200));
		} else if (StatsManager.getTargetGrade() == 4) {
			recommend.setText("Recommend Normal & Easy");
			recommend.setFont(new Font("Arial", Font.BOLD, 30));
			recommend.setForeground(new Color(120, 180, 140));
		} else if (StatsManager.getTargetGrade() == 5) {
			recommend.setText("Recommend Normal & Easy");
			recommend.setFont(new Font("Arial", Font.BOLD, 30));
			recommend.setForeground(new Color(120, 180, 140));
		}

		grade.setText(String.valueOf(StatsManager.getTargetGrade()));
		grade.setFont(new Font("Arial", Font.BOLD, 20));
		grade.setForeground(new Color(210, 210, 210));

		today.setText(String.valueOf(StatsManager.getTodayTraining()));
		today.setFont(new Font("Arial", Font.BOLD, 20));
		today.setForeground(new Color(200, 185, 130));

		elective.setText(StatsManager.getElective());
		if (StatsManager.getElective().equals("Prob&Stats"))
			elective.setText("Prob & Stats");

		elective.setFont(new Font("Arial", Font.BOLD, 20));

		if (StatsManager.getElective().equals("Prob&Stats")) {
			elective.setForeground(new Color(70, 100, 180));
		} else if (StatsManager.getElective().equals("Calculus")) {
			elective.setForeground(new Color(60, 160, 140));
		} else {
			elective.setForeground(new Color(230, 190, 80));
		}

		revalidate();
		repaint();
	}

	private JSeparator createSeparator() {
		JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
		sep.setForeground(new Color(120, 120, 120, 80)); // 은은하게
		return sep;
	}

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
