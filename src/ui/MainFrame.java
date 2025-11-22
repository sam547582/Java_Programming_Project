package ui;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
	
	private CardLayout cardLayout;
	private JPanel mainPanel;
	
	public MainFrame() {
		setTitle("KICE MATH TRAINING");
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		Container c = getContentPane();
		
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		
		MainMenuPanel menuPanel = new MainMenuPanel(this);
		DifficultyPanel difficultyPanel = new DifficultyPanel(this);
		
		mainPanel.add(menuPanel, "menu");
		mainPanel.add(difficultyPanel, "difficulty");
		
		c.add(mainPanel);
		setVisible(true);
	}
	
	//화면 전환 메소드
	public void showPanel(String name) {
		cardLayout.show(mainPanel, name);
	}

}
