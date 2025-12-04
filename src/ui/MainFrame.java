package ui;

import java.awt.*;
import java.io.File;

import javax.swing.*;
import model.*;

public class MainFrame extends JFrame {

	private BackgroundPanel bg;

	private CardLayout cardLayout;

	private JPanel mainPanel;
	private JPanel startPanel;
	private MainMenuPanel menuPanel;
	private SelectPanel subjectPanel;
	private DifficultyPanel difficultyPanel;
	private ProblemPanel problemPanel;
	private ResultPanel resultPanel;

	public MainFrame() {
		setTitle("KICE MATH TRAINING");
		setSize(900, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		bg = new BackgroundPanel();
		bg.setLayout(new BorderLayout());

		Container c = getContentPane();

		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		mainPanel.setOpaque(false);

		startPanel = new StartPanel(this);
		menuPanel = null;
		subjectPanel = null;
		difficultyPanel = null;
		problemPanel = null;
		resultPanel = null;

		bg.add(mainPanel, BorderLayout.CENTER);
		setContentPane(bg);

		File file = new File("resources/data/stats.txt");
		try {
			if (!file.exists()) {
				mainPanel.add(startPanel, "start");
				cardLayout.show(mainPanel, "start");
			} else {
				showMenu();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		setVisible(true);
	}

	public void showPanel(String name) {
		cardLayout.show(mainPanel, name);
	}

	public void showMenu() {
		menuPanel = new MainMenuPanel(this);

		mainPanel.add(menuPanel, "menu");
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

	public void showProblem(String difficulty,String subject) {

		problemPanel = new ProblemPanel(this, difficulty, subject);

		mainPanel.add(problemPanel, "problem");
		cardLayout.show(mainPanel, "problem");

	}

	public void showResult(Problem[] problems) {

		if (resultPanel != null) {
			mainPanel.remove(resultPanel);
		}

		resultPanel = new ResultPanel(this, problems);

		mainPanel.add(resultPanel, "result");
		cardLayout.show(mainPanel, "result");

		mainPanel.remove(problemPanel);
	}

}
