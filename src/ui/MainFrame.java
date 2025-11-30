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
		difficultyPanel = null;
		problemPanel = null;
		resultPanel = null;
		
		bg.add(mainPanel, BorderLayout.CENTER);
		setContentPane(bg);
		
		File file = new File("resources/data/stats.txt");
        try {
            if (!file.exists()) {
            	mainPanel.add(startPanel,"start");
            	cardLayout.show(mainPanel,  "start");
            }
            else {
            	showMenu();
            }
        }
        catch (Exception e) {
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
		
		if(startPanel != null) {
			mainPanel.remove(startPanel);
		}
		
	}
	
	public void showDifficulty() {
		difficultyPanel = new DifficultyPanel(this);
		
		mainPanel.add(difficultyPanel, "difficulty");
		cardLayout.show(mainPanel, "difficulty");
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
