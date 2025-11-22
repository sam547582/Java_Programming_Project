package ui;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
	
	private CardLayout cardLayout;
	
	private JPanel mainPanel;
	private MainMenuPanel menuPanel;
	private DifficultyPanel difficultyPanel;
	private ProblemPanel problemPanel;
	
	public MainFrame() {
		setTitle("KICE MATH TRAINING");
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		Container c = getContentPane();
		
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		
		menuPanel = new MainMenuPanel(this);
		difficultyPanel = new DifficultyPanel(this);
		problemPanel = new ProblemPanel(this);
		
		mainPanel.add(menuPanel, "menu");
		mainPanel.add(difficultyPanel, "difficulty");
		mainPanel.add(problemPanel, "problem");
		
		c.add(mainPanel);
		setVisible(true);
	}
	
	public void showPanel(String name) {
		cardLayout.show(mainPanel, name);
	}
	
	public void showProblem(String difficulty) {
		problemPanel.setDifficulty(difficulty);
		cardLayout.show(mainPanel, "problem");
	}

}
