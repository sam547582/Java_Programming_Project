package ui;

import java.awt.*;
import javax.swing.*;
import model.*;

public class MainFrame extends JFrame {
	
	private CardLayout cardLayout;
	
	private JPanel mainPanel;
	private MainMenuPanel menuPanel;
	private DifficultyPanel difficultyPanel;
	private ProblemPanel problemPanel;
	private ResultPanel resultPanel;
	
	public MainFrame() {
		setTitle("KICE MATH TRAINING");
		setSize(600, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		Container c = getContentPane();
		
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		
		menuPanel = new MainMenuPanel(this);
		difficultyPanel = new DifficultyPanel(this);
		problemPanel = null;
		resultPanel = null;
		
		mainPanel.add(menuPanel, "menu");
		mainPanel.add(difficultyPanel, "difficulty");
		c.add(mainPanel);
		setVisible(true);
	}
	
	public void showPanel(String name) {
		cardLayout.show(mainPanel, name);
	}
	
	public void showProblem(String difficulty) {
		
		problemPanel = new ProblemPanel(this, difficulty);
		
		mainPanel.add(problemPanel, "problem");
		cardLayout.show(mainPanel, "problem");
		
	}
	
	public void showResult(Problem[] problems) {
		
		if(resultPanel != null) {
			mainPanel.remove(resultPanel);
		}
		
		resultPanel = new ResultPanel(this, problems);
		
		mainPanel.add(resultPanel, "result");
		cardLayout.show(mainPanel, "result");
		
		mainPanel.remove(problemPanel);
	}

}
