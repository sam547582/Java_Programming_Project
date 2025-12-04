package ui;

import java.util.List;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.event.*;

import model.*;
import ui.component.*;
import controller.*;
import util.*;

public class TestPanel extends JPanel {

	private MainFrame frame;

	private BufferedImage img;

	private JPanel topWrapper;
	private JPanel top;
	private JPanel timerLabelWrapper;
	private RoundComponent<JPanel> center;
	private JPanel bottomWrapper;
	private JPanel bottomLeft;
	private JPanel bottomCenter;
	private JPanel bottomRight;
	private CalcPanel calcPanel;
	private MemoPanel memoPanel;
	private DrawPanel drawPanel;

	private JTabbedPane toolTabs;
	private JScrollPane scrollPane;

	private RoundComponent<JButton>[] problemNumberButton;
	private JLabel problemContentLabel;
	private RoundComponent<JTextField> answerField;
	private RoundComponent<JButton> submit;
	private RoundComponent<JButton> finish;
	private RoundComponent<JButton> drawToggleButton;
	private problemTimer timer;
	private JLabel timerLabel;

	private RoundComponent<JButton> black;
	private RoundComponent<JButton> white;

	private Problem[] problems;

	private String difficulty;
	private String subject;

	private int now_number;

	TestPanel(MainFrame frame, String elective) {
		this.frame = frame;

		now_number = 0;

		timerLabel = new JLabel();
		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timerLabel.setFont(new Font("Arial", Font.BOLD, 30));

		finish = new RoundComponent<>(JButton.class, new Dimension(150, 40), new Color(0, 0, 0, 0), Color.BLACK,
				"FINISH", Color.WHITE, new Font("Arial", Font.BOLD, 30), 20);
		finish.getInner().addActionListener(e -> {
			timer.stop();
			frame.showResult(problems, "test");
		});

		drawToggleButton = new RoundComponent<>(JButton.class, new Dimension(150, 30), new Color(0, 0, 0, 0),
				Color.BLACK, "DRAW", Color.WHITE, new Font("Arial", Font.BOLD, 30), 20);
		drawToggleButton.setFont(new Font("Arial", Font.BOLD, 20));
		drawToggleButton.getInner().addActionListener(e -> {
			boolean now = drawPanel.isVisible();
			drawPanel.setVisible(!now);

			if (!now) {
				drawPanel.initCanvas(center.getBackground());
			}
		});

		setLayout(new BorderLayout());
		setBackground(Color.GRAY);

		timer = new problemTimer(timerLabel);
		setTimer();

		problems = TestManager.getTest(elective);

		img = ImageUtils.getImage(problems, now_number);
		img = ImageUtils.removeBackground(img, new Color(255, 255, 255), 240);
		img = ImageUtils.scaleImage(img, 500);

		timerLabel.setForeground(Color.BLACK);

		createProblemContentLabel();

		createProblemNumberButton(this.frame);

		createAnswerPanel();

		black = new RoundComponent<>(JButton.class, new Dimension(70, 70), new Color(0, 0, 0, 0), Color.BLACK, "B",
				Color.WHITE, new Font("Arial", Font.BOLD, 35), 70);

		black.getInner().addActionListener(e -> {
			center.setBackground(Color.BLACK);
			calcPanel.updateColor(Color.BLACK);
			memoPanel.updateColor(Color.BLACK);
			drawPanel.updateColor(Color.BLACK);
			updateProblemContent();
		});

		white = new RoundComponent<>(JButton.class, new Dimension(70, 70), new Color(0, 0, 0, 0), Color.WHITE, "W",
				Color.BLACK, new Font("Arial", Font.BOLD, 35), 70);

		white.getInner().addActionListener(e -> {
			center.setBackground(Color.WHITE);
			calcPanel.updateColor(Color.WHITE);
			memoPanel.updateColor(Color.WHITE);
			drawPanel.updateColor(Color.WHITE);
			updateProblemContent();
		});

		createJPanel();

		add(topWrapper, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(bottomWrapper, BorderLayout.SOUTH);

		Dimension d = getPreferredSize();
		this.frame.setSize(d.width + 400, d.height + 100);

		frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - (d.width + 400) / 2, 0);
		timer.start();

	}

	private void updateProblemContent() {
		img = ImageUtils.getImage(problems, now_number);
		img = ImageUtils.removeBackground(img, center.getBackground(), 240);
		img = ImageUtils.scaleImage(img, 500);

		timerLabel.setForeground(ColorUtils.getContrastColor(center.getBackground()));

		problemContentLabel.setIcon(new ImageIcon(img));
	}

	private void setTimer() {
		timer.setTime(6000);
		timerLabel.setText("100:00");

		timer.setTimeoutListener(new problemTimer.TimeoutListener() {
			@Override
			public void Timeout() {
				frame.showResult(problems, "test");
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void createProblemNumberButton(MainFrame frame) {

		problemNumberButton = new RoundComponent[problems.length];
		for (int i = 0; i < problems.length; i++) {
			int num = i;

			problemNumberButton[i] = new RoundComponent<>(JButton.class, new Dimension(60, 60), new Color(0, 0, 0, 0),
					Color.BLACK, String.valueOf(i + 1), Color.WHITE, new Font("Arial", Font.BOLD, 20), 120);

			problemNumberButton[i].getInner().addActionListener(e -> {
				now_number = num;
				updateProblemContent();

				Dimension d = getPreferredSize();
				frame.setSize(d.width + 400, d.height + 100);
			});
		}
	}

	private void createProblemContentLabel() {
		problemContentLabel = new JLabel();
		problemContentLabel.setOpaque(false);
		problemContentLabel.setFont(new Font("Arial", Font.BOLD, 28));
		problemContentLabel.setIcon(new ImageIcon(img));
		problemContentLabel.setBounds(0, 0, img.getWidth(), img.getHeight());
		problemContentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		problemContentLabel.setVerticalAlignment(SwingConstants.TOP);
	}

	private void createJPanel() {
		top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
		top.setOpaque(false);

		scrollPane = new JScrollPane(top);
		scrollPane.setOpaque(false);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(30);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(null);
		
		scrollPane.setPreferredSize(new Dimension(300, 90));
		scrollPane.setMaximumSize(new Dimension(300, 90));
		scrollPane.setMinimumSize(new Dimension(300, 90));
		for (int i = 0; i < problems.length; i++) {
			top.add(problemNumberButton[i]);
		}

		topWrapper = new JPanel(new BorderLayout());
		topWrapper.setOpaque(false);
		topWrapper.add(scrollPane, BorderLayout.CENTER);
		topWrapper.add(drawToggleButton, BorderLayout.EAST);

		timerLabelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		timerLabelWrapper.setOpaque(false);
		timerLabelWrapper.add(timerLabel);

		toolTabs = new JTabbedPane(JTabbedPane.RIGHT);
		toolTabs.setPreferredSize(new Dimension(250, 500));
		toolTabs.setOpaque(false);
		toolTabs.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
			@Override
			protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {

			}
		});

		memoPanel = new MemoPanel();
		memoPanel.setOpaque(false);

		calcPanel = new CalcPanel();
		calcPanel.setOpaque(false);

		drawPanel = new DrawPanel();
		drawPanel.setOpaque(false);
		drawPanel.setVisible(false);

		toolTabs.addTab("Memo", memoPanel);
		toolTabs.addTab("Calc", calcPanel);

		// 기존 UI 묶음 subPanel
		JPanel subPanel = new JPanel(new BorderLayout());
		subPanel.setOpaque(false);
		subPanel.add(problemContentLabel, BorderLayout.WEST);
		subPanel.add(timerLabelWrapper, BorderLayout.NORTH);
		subPanel.add(toolTabs, BorderLayout.CENTER);

		// center = overlay 패널
		center = new RoundComponent<>(JPanel.class, new Color(0, 0, 0, 0), Color.WHITE, 30);
		center.getInner().setLayout(new OverlayLayout(center.getInner()));
		// DrawPanel 생성 + OFF 상태로
		drawPanel = new DrawPanel();
		drawPanel.setVisible(false);

		// 순서: drawPanel 먼저 → 최상단
		center.getInner().add(drawPanel);
		center.getInner().add(subPanel);

		bottomCenter = new JPanel(new FlowLayout());
		bottomCenter.setOpaque(false);
		bottomCenter.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
		bottomCenter.add(answerField);
		bottomCenter.add(submit);

		bottomLeft = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
		bottomLeft.setOpaque(false);
		bottomLeft.add(black);
		bottomLeft.add(white);

		bottomRight = new JPanel(new FlowLayout());
		bottomRight.setOpaque(false);
		bottomRight.setAlignmentY(CENTER_ALIGNMENT);
		bottomRight.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
		bottomRight.add(finish);

		bottomWrapper = new JPanel(new BorderLayout());
		bottomWrapper.setOpaque(false);
		bottomWrapper.setAlignmentY(CENTER_ALIGNMENT);
		bottomWrapper.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
		bottomWrapper.add(bottomCenter, BorderLayout.CENTER);
		bottomWrapper.add(bottomLeft, BorderLayout.WEST);
		bottomWrapper.add(bottomRight, BorderLayout.EAST);
	}

	private void createAnswerPanel() {
		answerField = new RoundComponent<>(JTextField.class, new Dimension(300, 40), new Color(0, 0, 0, 0), Color.WHITE,
				"", Color.BLACK, new Font("Arial", Font.PLAIN, 20), 20);

		submit = new RoundComponent<>(JButton.class, new Dimension(150, 40), new Color(0, 0, 0, 0), Color.BLACK,
				"SUBMIT", Color.WHITE, new Font("Arial", Font.BOLD, 25), 20);

		submit.getInner().addActionListener(e -> {
			for (int i = 0; i < answerField.getInner().getText().length(); i++) {
				int ascii = answerField.getInner().getText().charAt(i);
				if (ascii < 48 || ascii > 57) {
					JOptionPane.showConfirmDialog(null, "Please enter only numbers", "X", JOptionPane.DEFAULT_OPTION);
					return;
				}
			}
			if (JOptionPane.showConfirmDialog(null, "Are you sure you want to submit this answer?", "SUBMIT",
					JOptionPane.YES_NO_OPTION) == 0) {
				problems[now_number].setPlayerAnswer(answerField.getInner().getText());
				answerField.getInner().setText("");
			}
		});
	}
}
