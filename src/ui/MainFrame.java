package ui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;
import model.*;
import util.StatsManager;

public class MainFrame extends JFrame {

	private WindowListener exitListener;

	private CardLayout cardLayout;

	private JPanel mainPanel;
	private StartPanel startPanel;
	private MainMenuPanel menuPanel;
	private SettingPanel settingPanel;
	private SelectPanel subjectPanel;
	private DifficultyPanel difficultyPanel;
	private ProblemPanel problemPanel;
	private TestPanel testPanel;
	private ResultPanel resultPanel;

	public MainFrame() {
		setTitle("KICE MATH TRAINING");
		setSize(1000, 800);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		Container c = getContentPane();

		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		mainPanel.setOpaque(false);

		startPanel = null;
		menuPanel = null;
		settingPanel = null;
		subjectPanel = null;
		difficultyPanel = null;
		problemPanel = null;
		testPanel = null;
		resultPanel = null;

		File file = new File("resources/data/stats.txt");
		try {
			if (!file.exists()) {
				startPanel = new StartPanel(this);
				mainPanel.add(startPanel, "start");
				cardLayout.show(mainPanel, "start");
			} else {
				showMenu();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		c.add(mainPanel);

		setVisible(true);
	}

	public void disableExit() {
		if (exitListener != null)
			return;

		exitListener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (JOptionPane.showConfirmDialog(null, "Are you sure you want to leave?\n No records are saved.",
						"WARNING", JOptionPane.YES_NO_OPTION) == 0) {

					if (testPanel != null) {
						testPanel.cancelPreload();
					}
					
					if (problemPanel != null) {
						problemPanel.cancelPreload();
					}

					System.exit(0);
				} else {
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		};
		addWindowListener(exitListener);
	}

	public void enableExit() {
		if (exitListener == null)
			return;

		removeWindowListener(exitListener);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		exitListener = null;
	}

	public void setCardPanelsOpaque(boolean opaque) {
		for (Component c : mainPanel.getComponents()) {
			if (c instanceof JComponent jc) {
				jc.setOpaque(opaque);
			}
		}
		mainPanel.repaint();
	}

	public void showPanel(String name) {
		cardLayout.show(mainPanel, name);
	}

	public void showMenu() {
		menuPanel = new MainMenuPanel(this);
		settingPanel = new SettingPanel(this);

		mainPanel.add(menuPanel, "menu");
		mainPanel.add(settingPanel, "setting");

		cardLayout.show(mainPanel, "menu");

		if (startPanel != null) {
			mainPanel.remove(startPanel);
		}

	}

	public void showSubject() {
		if (subjectPanel != null) {
			mainPanel.remove(subjectPanel);
		}

		subjectPanel = new SelectPanel(this);

		mainPanel.add(subjectPanel, "subject");
		cardLayout.show(mainPanel, "subject");
	}

	public void showDifficulty(String subject) {
		if (difficultyPanel != null) {
			mainPanel.remove(difficultyPanel);
		}

		difficultyPanel = new DifficultyPanel(this, subject);

		mainPanel.add(difficultyPanel, "difficulty");
		cardLayout.show(mainPanel, "difficulty");
	}

	public void showProblem(String difficulty, String subject) {

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		Rectangle bounds = gc.getBounds();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

		int width = bounds.width - insets.left - insets.right;
		int height = bounds.height - insets.top - insets.bottom;

		setLocation(bounds.x + insets.left, bounds.y + insets.top);
		setSize(width, height);

		problemPanel = new ProblemPanel(this, difficulty, subject);

		mainPanel.add(problemPanel, "problem");
		cardLayout.show(mainPanel, "problem");

	}

	public void showTest(String elective) {

		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
				.getDefaultConfiguration();

		Rectangle bounds = gc.getBounds();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);

		int width = bounds.width - insets.left - insets.right;
		int height = bounds.height - insets.top - insets.bottom;

		setLocation(bounds.x + insets.left, bounds.y + insets.top);
		setSize(width, height);

		testPanel = new TestPanel(this, elective);

		mainPanel.add(testPanel, "test");
		cardLayout.show(mainPanel, "test");

	}

	public void showResult(Problem[] problems, String what) {

		if (resultPanel != null) {
			mainPanel.remove(resultPanel);
		}

		resultPanel = new ResultPanel(this, problems, what);

		mainPanel.add(resultPanel, "result");
		cardLayout.show(mainPanel, "result");

		if (testPanel != null) {
			mainPanel.remove(testPanel);
		}

		if (problemPanel != null) {
			mainPanel.remove(problemPanel);
		}
	}

}
