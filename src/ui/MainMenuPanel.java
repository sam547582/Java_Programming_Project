package ui;

import javax.imageio.ImageIO;
import javax.swing.*;

import ui.component.MenuLabel;

import java.awt.*;
import util.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import model.*;

public class MainMenuPanel extends JPanel {
	
	private JLabel played ;
	private JLabel accuracy;
	private JLabel correct;
	private JLabel wrong;
	
	public MainMenuPanel(MainFrame frame) {
		
		
		setLayout(new BorderLayout());
		setOpaque(true);
		
		setBackground(Color.GRAY);
	    
		ProblemStatsManager.syncStats();
		StatsManager.load();
		
	    played = new JLabel();
	    accuracy = new JLabel();
	    correct = new JLabel();
	    wrong = new JLabel();
	    
	    addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentShown(ComponentEvent e) {
	            refreshStats();
	        }
	    });
	    
	    refreshStats();
	    
	    JPanel leftMenu = new JPanel();
	    leftMenu.setLayout(new BoxLayout(leftMenu, BoxLayout.Y_AXIS));
	    leftMenu.setOpaque(false);
	    
	    leftMenu.add(Box.createVerticalStrut(60));
	    
		JLabel title = new JLabel("KICE MATH TRAINING", SwingConstants.CENTER);
		title.setFont(new Font("Arial", Font.BOLD, 26));
		title.setForeground(Color.WHITE);
		
		MenuLabel start = new MenuLabel("Start");
		start.addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		        frame.showSubject(); 
		    }
		});		

		leftMenu.add(wrapLabel(start));
		leftMenu.add(Box.createVerticalStrut(20));
		
		leftMenu.add(wrapLabel(new MenuLabel("Shop")));
		leftMenu.add(Box.createVerticalStrut(20));

		leftMenu.add(wrapLabel(new MenuLabel("Settings")));
		leftMenu.add(Box.createVerticalStrut(20));

		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right,BoxLayout.Y_AXIS));
		right.setOpaque(false);
		right.add(Box.createVerticalStrut(60));
		right.add(played);
		right.add(accuracy);
		right.add(correct);
		right.add(wrong);
		
        add(title, BorderLayout.NORTH);
        add(leftMenu, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
        
        JPanel emptyRight = new JPanel();
        emptyRight.setOpaque(false);
        add(emptyRight, BorderLayout.CENTER);
        
	}
	
	private JPanel wrapLabel(MenuLabel label) {
	    JPanel panel = new JPanel(null);
	    panel.setOpaque(false);
	    
	    panel.setPreferredSize(new Dimension(500, 70));
	    panel.setMaximumSize(new Dimension(500,70));
	    
	    Dimension d = label.getPreferredSize();
	    label.setBounds(0,0,d.width+5,d.height);
	    
	    panel.add(label);

	    return panel;
	}
	
	private void refreshStats() {
	    played.setText("Solved : " + StatsManager.getTotalPlayed());
	    played.setFont(new Font("Arial",Font.BOLD, 20));
	    played.setForeground(ColorUtils.getContrastColor(getBackground()));
	    
	    accuracy.setText("Accuracy : " + String.format("%.2f", StatsManager.getAccuracy()) + "%");
	    accuracy.setFont(new Font("Arial",Font.BOLD, 20));
	    accuracy.setForeground(ColorUtils.getContrastColor(getBackground()));
	    
	    correct.setText("Correct : " + StatsManager.getCorrect());
	    correct.setFont(new Font("Arial",Font.BOLD, 20));
	    correct.setForeground(ColorUtils.getContrastColor(getBackground()));
	    
	    wrong.setText("Wrong : " + StatsManager.getWrong());
	    wrong.setFont(new Font("Arial",Font.BOLD, 20));
	    wrong.setForeground(ColorUtils.getContrastColor(getBackground()));
	    revalidate();
	    repaint();
	}
	



}
