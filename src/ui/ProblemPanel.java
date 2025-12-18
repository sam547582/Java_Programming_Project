package ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import model.*;
import ui.component.*;
import controller.*;
import util.*;

public class ProblemPanel extends JPanel {

	private MainFrame frame;

	private SwingWorker<Void, Integer> preloadWorker;
	private final Map<String, BufferedImage> imageCache = new HashMap<>();

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
	private JLabel rateLabel;

	private JLabel loadingLabel;

	private RoundComponent<JButton> black;
	private RoundComponent<JButton> white;

	private Problem[] problems;

	private String difficulty;

	private int now_number;

	ProblemPanel(MainFrame frame, String difficulty, String subject) {
		this.frame = frame;
		this.difficulty = difficulty;

		frame.setResizable(true);
		frame.disableExit();

		now_number = 0;

		timerLabel = new JLabel();
		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timerLabel.setFont(new Font("Arial", Font.BOLD, 40));

		rateLabel = new JLabel();
		rateLabel.setFont(new Font("Arial", Font.BOLD, 40));

		finish = new RoundComponent<>(JButton.class, new Dimension(150, 40), new Color(0, 0, 0, 0), Color.BLACK,
				"FINISH", Color.WHITE, new Font("Arial", Font.BOLD, 30), 20);
		finish.getInner().addActionListener(e -> {
			if (JOptionPane.showConfirmDialog(frame, "Real Finish?", "FINISH", JOptionPane.DEFAULT_OPTION) == 0) {
				timer.stop();
				frame.showResult(problems, "problem");
			}
		});

		drawToggleButton = new RoundComponent<>(JButton.class, new Dimension(150, 30), new Color(0, 0, 0, 0),
				new Color(50, 70, 95), "DRAW", new Color(230, 235, 240), new Font("Arial", Font.BOLD, 30), 20);
		drawToggleButton.setFont(new Font("Arial", Font.BOLD, 20));
		drawToggleButton.getInner().addActionListener(e -> {
			boolean now = drawPanel.isVisible();
			drawPanel.setVisible(!now);
		});

		setLayout(new BorderLayout());
		setBackground(Color.GRAY);

		timer = new problemTimer(timerLabel);
		setTimer();

		problems = ProblemManager.getProblem(difficulty, subject);

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

		SwingUtilities.invokeLater(() -> {
			loadFirstProblem();
			updateProblemContent();
			preloadImages();
		});

		timer.start();

	}

	private void loadFirstProblem() {
		Color bg = Color.WHITE;

		String key = 0 + "_" + bg.getRGB();
		if (imageCache.containsKey(key))
			return;

		BufferedImage img = ImageUtils.getImage(problems, 0);
		img = ImageUtils.removeBackground(img, bg, 210);
		img = scaleImageNeed(img, calculateImageSize());

		imageCache.put(key, img);
	}

	private void preloadImages() {

		preloadWorker = new SwingWorker<Void, Integer>() {

			@Override
			protected Void doInBackground() {

				Color[] themes = { Color.WHITE, Color.BLACK };

				int maxSize = calculateImageSize();

				for (int i = 0; i < problems.length; i++) {

					for (Color bg : themes) {
						if (isCancelled())
							return null;

						String key = i + "_" + bg.getRGB();
						if (imageCache.containsKey(key))
							continue;

						BufferedImage img = ImageUtils.getImage(problems, i);
						img = ImageUtils.removeBackground(img, bg, 210);
						img = scaleImageNeed(img, maxSize);

						imageCache.put(key, img);
					}

					publish(i);
				}

				return null;

			}

			@Override
			protected void process(List<Integer> chunks) {
				int lastLoaded = chunks.get(chunks.size() - 1);

				if (lastLoaded == now_number) {
					updateProblemContent();
				}
			}

		};
		preloadWorker.execute();
	}

	public void cancelPreload() {
		if (preloadWorker != null && !preloadWorker.isDone()) {
			preloadWorker.cancel(true);
		}
	}

	private void updateProblemContent() {
		Color bg = null;
		if (center == null)
			bg = Color.WHITE;
		else
			bg = center.getBackground();

		String key = now_number + "_" + bg.getRGB();

		BufferedImage img = imageCache.get(key);

		if (img == null) {
			problemContentLabel.setIcon(null);
			loadingLabel.setVisible(true);
			return;
		}

		problemContentLabel.setIcon(new ImageIcon(img));
		loadingLabel.setVisible(false);
		timerLabel.setForeground(ColorUtils.getContrastColor(bg));

		double all = problems[now_number].getSolveCount() + problems[now_number].getWrongCount();
		double solve = problems[now_number].getSolveCount();
		double rate = (all == 0) ? 0 : solve / all;

		rateLabel.setText(all == 0 ? "0%" : String.format("%.2f", rate * 100) + "%");
		rateLabel.setForeground(ColorUtils.getAccuracyColor(rate));

		revalidate();
		repaint();
	}

	String makeCacheKey(int problemIndex, Color bg) {
		return problemIndex + "_" + bg.getRGB();
	}

	private void setTimer() {
		int time = 0;

		if (difficulty.equals("easy")) {
			time = 600;
		} else if (difficulty.equals("normal")) {
			time = 1200;
		} else if (difficulty.equals("hard")) {
			time = 1800;
		} else {
			time = 2400;
		}

		if (StatsManager.getTargetGrade() == 1) {
			time *= 1.0;
		} else if (StatsManager.getTargetGrade() == 2) {
			time *= 1.1;
		} else if (StatsManager.getTargetGrade() == 3) {
			time *= 1.2;
		} else if (StatsManager.getTargetGrade() == 4) {
			time *= 1.3;
		} else if (StatsManager.getTargetGrade() == 5) {
			time *= 1.4;
		}

		String min = String.valueOf(time / 60);
		String sec = String.valueOf(time % 60);
		if (time / 60 >= 10) {
			if (time % 60 >= 10)
				timerLabel.setText(min + ":" + sec);
			else
				timerLabel.setText(min + ":" + "0" + sec);
		} else {
			if (time % 60 >= 10)
				timerLabel.setText("0" + min + ":" + sec);
			else
				timerLabel.setText("0" + min + ":" + "0" + sec);
		}

		timer.setTime(time);

		timer.setTimeoutListener(new problemTimer.TimeoutListener() {
			@Override
			public void Timeout() {
				int check = JOptionPane.showConfirmDialog(frame, "FINISH", "FINISH", JOptionPane.DEFAULT_OPTION);
				if (check == 0 || check == -1) {
					frame.showResult(problems, "problem");
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void createProblemNumberButton(MainFrame frame) {
		problemNumberButton = new RoundComponent[problems.length];
		for (int i = 0; i < problems.length; i++) {
			int num = i;

			problemNumberButton[i] = new RoundComponent<>(JButton.class, new Dimension(50, 50), new Color(0, 0, 0, 0),
					new Color(10, 10, 10), String.valueOf(i + 1), new Color(220, 225, 230),
					new Font("Arial", Font.BOLD, 20), 160);

			problemNumberButton[i].getInner().addActionListener(e -> {
				now_number = num;
				if (!problems[now_number].getPlayerAnswer().equals("")) {
					answerField.getInner().setText(problems[now_number].getPlayerAnswer());
				}
				else {
					answerField.getInner().setText("");
				}
				updateProblemContent();

			});
		}
	}

	private int calculateImageSize() {

		int frameHeight = frame.getContentPane().getHeight();

		int reserved = 200;
		int maxSize = frameHeight - reserved;

		return Math.max(maxSize, 100);
	}

	private BufferedImage scaleImageNeed(BufferedImage img, int maxSize) {

		int w = img.getWidth();
		int h = img.getHeight();

		int maxDim = Math.max(w, h);

		// 이미 충분히 작으면 그대로 사용
		if (maxDim <= maxSize) {
			return img;
		}

		double scale = (double) maxSize / maxDim;

		int newW = (int) (w * scale);

		return ImageUtils.scaleImage(img, newW);
	}

	private void createProblemContentLabel() {
		problemContentLabel = new JLabel();
		problemContentLabel.setOpaque(false);
		problemContentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		problemContentLabel.setVerticalAlignment(SwingConstants.TOP);

		loadingLabel = new JLabel("LOADING");
		loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loadingLabel.setVerticalAlignment(SwingConstants.CENTER);
		loadingLabel.setAlignmentX(0.5f);
		loadingLabel.setAlignmentY(0.5f);
		loadingLabel.setPreferredSize(new Dimension(800, 600));
		loadingLabel.setFont(new Font("Arial", Font.BOLD, 80));
		loadingLabel.setForeground(new Color(0, 0, 0, 80));
		loadingLabel.setVisible(true);
	}

	private void createJPanel() {
		top = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
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
		topWrapper.add(top, BorderLayout.CENTER);
		topWrapper.add(drawToggleButton, BorderLayout.EAST);

		timerLabelWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
		timerLabelWrapper.setOpaque(false);
		timerLabelWrapper.add(timerLabel);
		timerLabelWrapper.add(rateLabel);

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

		JPanel subPanel = new JPanel(new BorderLayout());
		subPanel.setOpaque(false);
		subPanel.add(problemContentLabel, BorderLayout.WEST);
		subPanel.add(timerLabelWrapper, BorderLayout.NORTH);
		subPanel.add(toolTabs, BorderLayout.CENTER);

		center = new RoundComponent<>(JPanel.class, new Color(0, 0, 0, 0), Color.WHITE, 30);
		center.getInner().setLayout(new OverlayLayout(center.getInner()));

		drawPanel = new DrawPanel();
		drawPanel.setVisible(false);

		center.getInner().add(loadingLabel);
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

			if (answerField.getInner().getText().equals("")) {
				JOptionPane.showConfirmDialog(null, "Enter your answer", "X", JOptionPane.DEFAULT_OPTION);
				return;
			}

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
				problemNumberButton[now_number].setBackground(new Color(90, 95, 105));
				problemNumberButton[now_number].setForeground(new Color(180, 185, 190));
			}
		});
	}
}
