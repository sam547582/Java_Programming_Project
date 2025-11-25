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
	
	private ImageUtils imgUtils;
	
	private BufferedImage img;
	private BufferedImage removed_img;
	private BufferedImage scaled_img;
	
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
	
	private int size;
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
		finish.addActionListener(e -> {frame.showResult(problems); });
		
		setLayout(new BorderLayout());
		setBackground(new Color(30, 40, 60));
		
		timer = new problemTimer(timerLabel);
		setTimer();
		
		getProblem();
		
		imgUtils = new ImageUtils(null, problems);
		
		img = imgUtils.getImage(now_number);
		removed_img = imgUtils.removeBackground(240);
		scaled_img = imgUtils.scaleImage(500);
		timerLabel.setForeground(imgUtils.getTextColor());
		
		createProblemContentLabel();
		
		createProblemNumberButton(this.frame);
		
		createAnswerPanel();
		
		black = new ColorButton(Color.BLACK);
		black.setPreferredSize(new Dimension(30,30));
		black.addActionListener(e -> {  
										center.setBackground(Color.BLACK);
										imgUtils.setPanel(center);
										
										removed_img = imgUtils.removeBackground(240);
										scaled_img = imgUtils.scaleImage(500);
										timerLabel.setForeground(imgUtils.getTextColor());
										problemContentLabel.setIcon(new ImageIcon(scaled_img)); });
		white = new ColorButton(Color.WHITE);
		white.setBackground(Color.WHITE);
		white.setPreferredSize(new Dimension(30,30));
		white.addActionListener(e -> {  
										center.setBackground(Color.WHITE);
										imgUtils.setPanel(center);
										
										removed_img = imgUtils.removeBackground(240);
										scaled_img = imgUtils.scaleImage(500);
										timerLabel.setForeground(imgUtils.getTextColor());
										problemContentLabel.setIcon(new ImageIcon(scaled_img)); });
		
		createJPanel();
		
		add(topWrapper, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(bottomWrapper, BorderLayout.SOUTH);
		
		Dimension d = getPreferredSize();
		this.frame.setSize(d.width + 400,d.height + 100);
		
		frame.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - (d.width + 400) / 2 , 0);
		timer.start();
		
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
				frame.setSize(600,400);
				frame.showPanel("difficulty");
			}
		});
	}
	
	private void createProblemNumberButton(MainFrame frame) {
		problemNumberButton = new JButton[size];
		for(int i=0;i<size;i++) {
			int num = i;
			
			problemNumberButton[i] = new JButton(String.valueOf(i+1));
			problemNumberButton[i].setFont(new Font("Arial", Font.BOLD, 28));
			problemNumberButton[i].addActionListener(e -> { 
													 now_number = num;
													 imgUtils.setPanel(center);
													 
													 img = imgUtils.getImage(now_number); 
													 removed_img = imgUtils.removeBackground(240);
													 scaled_img = imgUtils.scaleImage(500);
													 
													 timerLabel.setForeground(imgUtils.getTextColor());
													 problemContentLabel.setIcon(new ImageIcon(scaled_img));
													 
													 Dimension d = getPreferredSize();
													 frame.setSize(d.width + 400,d.height + 100); });
		}
	}
	
	private void createProblemContentLabel() {
		problemContentLabel = new JLabel();
		problemContentLabel.setFont(new Font("Arial", Font.BOLD, 28));
		problemContentLabel.setIcon(new ImageIcon(scaled_img));
		problemContentLabel.setBounds(0, 0, scaled_img.getWidth(), scaled_img.getHeight());
		problemContentLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	private void createJPanel() {
		top = new JPanel();
		top.setLayout(new BoxLayout(top,BoxLayout.X_AXIS));
		top.setOpaque(false);
		
		top.add(Box.createHorizontalGlue());
		for(int i=0;i<size;i++) {
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
	
	private void getProblem() {
		if(difficulty.equals("easy")) {
			List<Problem> all = ProblemManager.loadProblems("img/easy");
			
			size = Math.min(all.size(), 20);
			
			problems = ProblemManager.pickRandom(all,size);
		}
		else if(difficulty.equals("normal")) {
			List<Problem> all = ProblemManager.loadProblems("img/normal");
			
			size = Math.min(all.size(), 20);
			
			problems = ProblemManager.pickRandom(all,size);
		}
		else if(difficulty.equals("hard")) {
			List<Problem> all = ProblemManager.loadProblems("img/hard");
			
			size = Math.min(all.size(), 10);
			
			problems = ProblemManager.pickRandom(all,size);
		}
		else if(difficulty.equals("extreme")) {
			List<Problem> all = ProblemManager.loadProblems("img/extreme");
			
			size = Math.min(all.size(), 5);
			
			problems = ProblemManager.pickRandom(all,size);
		}
	}
}
