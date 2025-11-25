package ui;

import javax.swing.*;
import java.awt.*;
import model.*;

public class ResultPanel extends JFrame {
	
	private Problem[] problems;
	
	private JPanel center;
	
	private JLabel correctLabel;
	private JLabel wrongLabel;
	
	private int correct;
	private int wrong;
	
	public ResultPanel(MainFrame frame, Problem[] problems) {
		this.problems = problems;
		correct = wrong = 0;
		
		setLayout(new BorderLayout());
		setBackground(new Color(30, 40, 60));
		
		checkAnswer();
		
		correctLabel = new JLabel(String.valueOf(correct));
		correctLabel.setFont(new Font("Arial",Font.BOLD,40));
		
		wrongLabel = new JLabel(String.valueOf(wrong));
		wrongLabel.setFont(new Font("Arial",Font.BOLD,40));
		
		center.setLayout(new FlowLayout());
		center.setOpaque(false);
		center.add(correctLabel);
		center.add(wrongLabel);
		
		add(center,BorderLayout.CENTER);
		
	}
	
	private void checkAnswer() {
		for(Problem p : problems) {
			if(p.getPlayerAnswer().equals(p.getAnswer())) {
				correct++;
			}
			else {
				wrong++;
			}
		}
	}
}
