package ui;

import java.util.List;
import java.io.*;
import java.net.URL;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import model.*;
import controller.*;
import util.*;

public class ProblemPanel extends JPanel {
	
	MainFrame frame;
	
	private BufferedImage img;
	
	private JPanel topWrapper;
	private JPanel top;
	private JPanel timerLabelWrapper;
	private JPanel center;
	private JPanel bottomWrapper;
	private JPanel bottomLeft;
	private JPanel bottomCenter;
	private JPanel bottomRight;
	
	private JButton[] problemNumberButton;
	private JLabel problemContentLabel;
	private JTextField answerField;
	private JButton submit;
	private JButton finish;
	
	private problemTimer timer;
	private JLabel timerLabel;
	
	private JButton black;
	private JButton white;
	
	private Problem[] problems;
	
	private String difficulty;
	
	private int now_number;
	
	ProblemPanel(MainFrame frame,String difficulty) {
		this.frame = frame;
		this.difficulty = difficulty;
		now_number = 0;
		
		timerLabel = new JLabel();
		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timerLabel.setFont(new Font("Arial",Font.BOLD,30));
		
		finish = new JButton("FINISH");
		finish.setFont(new Font("Arial",Font.BOLD,25));
		finish.addActionListener(e -> {timer.stop(); frame.showResult(problems); });
		
		setLayout(new BorderLayout());
		setBackground(new Color(30, 40, 60));
		
		timer = new problemTimer(timerLabel);
		setTimer();
		
		problems = ProblemManager.getProblem(difficulty);
		
		img = ImageUtils.getImage(problems, now_number);
		img = ImageUtils.removeBackground(img, new Color(255,255,255), 240);
		img = ImageUtils.scaleImage(img, 500);
		
		timerLabel.setForeground(Color.BLACK);
		
		createProblemContentLabel();
		
		createProblemNumberButton(this.frame);
		
		createAnswerPanel();
		
		black = new ColorButton(Color.BLACK);
		black.setPreferredSize(new Dimension(30,30));
		black.addActionListener(e -> {  
										center.setBackground(Color.BLACK);
										updateProblemContent();});
										
		white = new ColorButton(Color.WHITE);
		white.setBackground(Color.WHITE);
		white.setPreferredSize(new Dimension(30,30));
		white.addActionListener(e -> {  
										center.setBackground(Color.WHITE);
										updateProblemContent();});
		
		createJPanel();
		
		add(topWrapper, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(bottomWrapper, BorderLayout.SOUTH);
		
		Dimension d = getPreferredSize();
		this.frame.setSize(d.width + 400,d.height + 100);
		
		frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - (d.width + 400) / 2 , 0);
		timer.start();
		
	}
	
	private void updateProblemContent() {
		img = ImageUtils.getImage(problems, now_number);
		img = ImageUtils.removeBackground(img, center.getBackground(), 240);
		img = ImageUtils.scaleImage(img, 500);

		if(center.getBackground() == Color.WHITE) timerLabel.setForeground(Color.BLACK);
		else if(center.getBackground() == Color.BLACK) timerLabel.setForeground(Color.WHITE);
		
		problemContentLabel.setIcon(new ImageIcon(img));
	}
	
	private void setTimer() {
		if(difficulty.equals("easy")) {
			timer.setTime(30);
			timerLabel.setText("00:30");
		}
		else if(difficulty.equals("normal")) {
			timer.setTime(600);
			timerLabel.setText("10:00");
		}
		else if(difficulty.equals("normal")) {
			timer.setTime(900);
			timerLabel.setText("15:00");
		}
		else {
			timer.setTime(1800);
			timerLabel.setText("30:00");
		}
		
		timer.setTimeoutListener(new problemTimer.TimeoutListener() {
			@Override
			public void Timeout() {
				frame.showResult(problems);
			}
		});
	}
	
	private void createProblemNumberButton(MainFrame frame) {
		problemNumberButton = new JButton[problems.length];
		for(int i=0;i<problems.length;i++) {
			int num = i;
			
			problemNumberButton[i] = new JButton(String.valueOf(i+1));
			problemNumberButton[i].setFont(new Font("Arial", Font.BOLD, 28));
			problemNumberButton[i].addActionListener(e -> { 
													 		 now_number = num;
															 updateProblemContent();
													 
															 Dimension d = getPreferredSize();
															 frame.setSize(d.width + 400,d.height + 100); });
		}
	}
	
	private void createProblemContentLabel() {
		problemContentLabel = new JLabel();
		problemContentLabel.setFont(new Font("Arial", Font.BOLD, 28));
		problemContentLabel.setIcon(new ImageIcon(img));
		problemContentLabel.setBounds(0, 0, img.getWidth(), img.getHeight());
		problemContentLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	private void createJPanel() {
		top = new JPanel();
		top.setLayout(new BoxLayout(top,BoxLayout.X_AXIS));
		top.setOpaque(false);
		
		top.add(Box.createHorizontalGlue());
		for(int i=0;i<problems.length;i++) {
			top.add(problemNumberButton[i]);
			top.add(Box.createHorizontalStrut(20));
		}
		top.add(Box.createHorizontalGlue());
		
		topWrapper = new JPanel(new BorderLayout());
		topWrapper.setOpaque(false);
		topWrapper.add(top,BorderLayout.CENTER);
		
		timerLabelWrapper = new JPanel(new FlowLayout());
		timerLabelWrapper.setOpaque(false);
		timerLabelWrapper.add(timerLabel);
		
		center = new JPanel(new BorderLayout());
		center.setOpaque(true);
		center.setBackground(Color.WHITE);
		center.add(problemContentLabel,BorderLayout.WEST);
		center.add(timerLabelWrapper,BorderLayout.NORTH);
		
		bottomCenter = new JPanel(new FlowLayout());
		bottomCenter.setOpaque(false);
		bottomCenter.add(answerField);
		bottomCenter.add(submit);
		bottomCenter.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
		
		bottomLeft = new JPanel(new FlowLayout(FlowLayout.CENTER,30,10));
		bottomLeft.setOpaque(false);
		bottomLeft.add(black);
		bottomLeft.add(white);
		bottomLeft.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
		
        bottomRight = new JPanel(new FlowLayout());
        bottomRight.setOpaque(false);
        bottomRight.setPreferredSize(bottomLeft.getPreferredSize());
        bottomRight.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        bottomRight.add(finish);
        
		bottomWrapper = new JPanel(new BorderLayout());
		bottomWrapper.setOpaque(false);
		bottomWrapper.add(bottomCenter,BorderLayout.CENTER);
		bottomWrapper.add(bottomLeft,BorderLayout.WEST);
		bottomWrapper.add(bottomRight,BorderLayout.EAST);
	}
	
	private void createAnswerPanel() {
		answerField = new JTextField(20);
		answerField.setFont(new Font("Arial", Font.PLAIN, 20));
		
		submit = new JButton("Submit");
		submit.setFont(new Font("Arial", Font.BOLD, 18));
		submit.addActionListener(e -> problems[now_number].setPlayerAnswer(answerField.getText().trim()));	
	}
}
