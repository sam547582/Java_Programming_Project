package ui;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
	
	private CardLayout cardLayout;
	private JPanel mainPanel;
	
	public MainFrame() {
		setTitle("수능 수학 트레이닝 프로그램");
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);
		
		MainMenuPanel menuPanel = new MainMenuPanel();
		DifficultyPanel difficultyPanel = new DifficultyPanel();
		
		mainPanel.add(menuPanel, "menu");
		mainPanel.add(difficultyPanel, "difficulty");
		
		add(mainPanel);
		setVisible(true);
	}
	
	//화면 전환 메소드
	public void showPanel(String name) {
		cardLayout.show(mainPanel, name);
	}

}
