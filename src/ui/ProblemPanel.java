package ui;

import java.awt.*;
import javax.swing.*;

public class ProblemPanel extends JPanel {
	
	private String difficulty = "";
	
	private JLabel problemNumberLabel;
	private JLabel problemContentLabel;
	private JTextField answerField;
	private JButton submit;
	
	ProblemPanel(MainFrame frame) {
		
		setLayout(new BorderLayout());
		setBackground(new Color(30, 40, 60));
		
		problemNumberLabel = new JLabel("Problem 1", SwingConstants.CENTER);
		problemNumberLabel.setFont(new Font("Arial", Font.BOLD, 28));
		problemNumberLabel.setForeground(Color.WHITE);;
		
		problemContentLabel = new JLabel("Problem showing", SwingConstants.CENTER);
		problemContentLabel.setFont(new Font("Arial", Font.BOLD, 28));
		problemContentLabel.setForeground(Color.BLACK);
		
		answerField = new JTextField(20);
		answerField.setFont(new Font("Arial", Font.PLAIN, 20));
		
		submit = new JButton("Submit");
		submit.setFont(new Font("Arial", Font.BOLD, 18));
		
		JPanel top = new JPanel(new BorderLayout());
		top.setOpaque(false);
		top.add(problemNumberLabel,BorderLayout.CENTER);
		top.setBorder(BorderFactory.createEmptyBorder(20, 0, 20 , 0));
		
		JPanel center = new JPanel(new BorderLayout());
		center.setOpaque(true);
		center.add(problemContentLabel,BorderLayout.CENTER);
		
		JPanel bottom = new JPanel(new FlowLayout());
		bottom.setOpaque(false);
		bottom.add(answerField);
		bottom.add(submit);
		bottom.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
		
		add(top, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
		
	}
	
	
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
		
	}
}
