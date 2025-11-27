package ui;

import javax.swing.*;
import java.awt.*;
import model.*;
import util.*;

public class ResultPanel extends JPanel {
	
	private Problem[] problems;
	
	private JPanel top;
	private JPanel topLeftWrapper;
	private JPanel topRightWrapper;
	
	private JPanel center;
	
	private JPanel left;
	
	private JPanel bottom;
	
	private JLabel leftLabel1;
	private JLabel leftLabel2;
	private JLabel leftLabel3;
	private JLabel leftLabel4;
	
	private MenuLabel menu;
	
	private JLabel[] problemNumberLabel;
	private JLabel resultLabel;
	private JLabel correctLabel;
	private JLabel wrongLabel;
	private JLabel[] ox;
	
	private int correct;
	private int wrong;
	
	ResultPanel(MainFrame frame, Problem[] problems) {
		this.problems = problems;
		problemNumberLabel = new JLabel[problems.length];
		ox = new JLabel[problems.length];
		
		correct = wrong = 0;
		
		setLayout(new BorderLayout());
		setBackground(new Color(30,40,60));
		frame.setSize(1000,550);
		
		checkAnswer();
		
		resultLabel = new JLabel("RESULT");
		resultLabel.setFont(new Font("Arial",Font.BOLD | Font.ITALIC ,60));
		resultLabel.setForeground(ColorUtils.getContrastColor(getBackground()));
		Dimension d = resultLabel.getPreferredSize();
		resultLabel.setPreferredSize(new Dimension(d.width + 10, d.height));
		
		leftLabel1 = new JLabel("NUMBER");
		leftLabel1.setFont(new Font("Arial",Font.BOLD,30));
		leftLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		leftLabel1.setForeground(ColorUtils.getContrastColor(getBackground()));
		
		leftLabel2 = new JLabel("YOUR ANSWER");
		leftLabel2.setFont(new Font("Arial",Font.BOLD,30));
		leftLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		leftLabel2.setForeground(ColorUtils.getContrastColor(getBackground()));
		
		leftLabel3 = new JLabel("ANSWER");
		leftLabel3.setFont(new Font("Arial",Font.BOLD,30));
		leftLabel3.setHorizontalAlignment(SwingConstants.CENTER);
		leftLabel3.setForeground(ColorUtils.getContrastColor(getBackground()));
		
		leftLabel4 = new JLabel("O/X");
		leftLabel4.setFont(new Font("Arial",Font.BOLD,30));
		leftLabel4.setHorizontalAlignment(SwingConstants.CENTER);
		leftLabel4.setForeground(ColorUtils.getContrastColor(getBackground()));
		
		menu = new MenuLabel("MENU");
		menu.setFont(new Font("Arial",Font.BOLD,30));
		menu.setForeground(ColorUtils.getContrastColor(getBackground()));
		menu.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        frame.showPanel("menu");
		        frame.setSize(600,400);
		    }
		});		
		
		correctLabel = new JLabel("CORRECT : " + String.valueOf(correct));
		correctLabel.setFont(new Font("Arial",Font.BOLD,40));
		correctLabel.setForeground(ColorUtils.getContrastColor(getBackground()));
		
		wrongLabel = new JLabel("WRONG : " + String.valueOf(wrong));
		wrongLabel.setFont(new Font("Arial",Font.BOLD,40));
		wrongLabel.setForeground(ColorUtils.getContrastColor(getBackground()));
		
		topLeftWrapper = new JPanel(new FlowLayout());
		topLeftWrapper.setOpaque(false);
		topLeftWrapper.add(resultLabel);
		
		topRightWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER,20,0));
		topRightWrapper.setOpaque(false);
		topRightWrapper.add(menu);
		
		top = new JPanel(new BorderLayout());
		top.setOpaque(false);
		top.setBorder(BorderFactory.createEmptyBorder(10,0,20,0));
		top.add(topLeftWrapper,BorderLayout.WEST);
		top.add(topRightWrapper,BorderLayout.EAST);
		
		center = new JPanel();
		center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
		center.setOpaque(false);
		
		JPanel header = new JPanel(new GridLayout(1,4));
		header.setOpaque(false);
		header.add(leftLabel1);
		header.add(leftLabel2);
		header.add(leftLabel3);
		header.add(leftLabel4);
		center.add(header);
		
		for(int i=0;i<problems.length;i++){
		    center.add(createSet(i));
		}
		
		bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
		bottom.setOpaque(false);
		bottom.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
		bottom.add(correctLabel);
		bottom.add(wrongLabel);
		
		add(top,BorderLayout.NORTH);
		add(center,BorderLayout.CENTER);
		add(bottom,BorderLayout.SOUTH);
	}
	
	private JPanel createSet(int num) {
		
		problemNumberLabel[num] = new JLabel(String.valueOf(num+1));
		problemNumberLabel[num].setFont(new Font("Arial",Font.BOLD,30));
		problemNumberLabel[num].setForeground(ColorUtils.getContrastColor(getBackground()));
		problemNumberLabel[num].setHorizontalAlignment(SwingConstants.CENTER);
		problemNumberLabel[num].setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel myAnswer = new JLabel(problems[num].getPlayerAnswer());
		myAnswer.setFont(new Font("Arial",Font.BOLD,30));
		myAnswer.setForeground(ColorUtils.getContrastColor(getBackground()));
		myAnswer.setHorizontalAlignment(SwingConstants.CENTER);
		myAnswer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel answer = new JLabel(problems[num].getAnswer());
		answer.setFont(new Font("Arial",Font.BOLD,30));
		answer.setForeground(ColorUtils.getContrastColor(getBackground()));
		answer.setHorizontalAlignment(SwingConstants.CENTER);
		answer.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel row = new JPanel(new GridLayout(1,4));
		row.setOpaque(false);
	    row.add(problemNumberLabel[num]);
	    row.add(myAnswer);
	    row.add(answer);
	    row.add(ox[num]);
		
		return row;
	}
	
	private void checkAnswer() {
		int cnt = 0;
		for(Problem p : problems) {
			if(p.getPlayerAnswer().equals(p.getAnswer())) {
				ox[cnt] = new JLabel("O");
				ox[cnt].setFont(new Font("Arial",Font.BOLD,30));
				ox[cnt].setForeground(Color.BLUE);
				correct++;
			}
			else {
				ox[cnt] = new JLabel("X");
				ox[cnt].setFont(new Font("Arial",Font.BOLD,30));
				ox[cnt].setForeground(Color.RED);
				wrong++;
			}
			
			ox[cnt].setHorizontalAlignment(SwingConstants.CENTER);
			ox[cnt].setAlignmentX(Component.CENTER_ALIGNMENT);
			
			cnt++;
		}
	}
}
