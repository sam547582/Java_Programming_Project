package ui;

import javax.swing.*;
import java.awt.*;
import model.*;
import ui.component.MenuLabel;
import ui.component.ResponsiveLabel;
import util.*;

public class ResultPanel extends JPanel {
	
	private Problem[] problems;
	
	private JPanel top;
	private JPanel topLeftWrapper;
	private JPanel topRightWrapper;
	
	private JPanel center;
	
	private JPanel bottom;
	
	private MenuLabel menu;
	
	private JLabel resultLabel;
	private JLabel correctLabel;
	private JLabel wrongLabel;
	private JLabel[] ox;
	
	private int correct;
	private int wrong;
	private int problemSize;
	
	ResultPanel(MainFrame frame, Problem[] problems) {
		this.problems = problems;
		problemSize = problems.length;
		ox = new JLabel[problemSize];
		correct = wrong = 0;
		
		setLayout(new BorderLayout());
		setBackground(Color.GRAY);
		frame.setSize(1000,550);
		
		checkAnswer();
		StatsManager.updateStats(correct , wrong);

		resultLabel = new JLabel("RESULT");
		resultLabel.setFont(new Font("Arial",Font.BOLD | Font.ITALIC ,60));
		resultLabel.setForeground(ColorUtils.getContrastColor(getBackground()));
		Dimension d = resultLabel.getPreferredSize();
		resultLabel.setPreferredSize(new Dimension(d.width + 10, d.height));
		
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
		
		center = new JPanel(new GridBagLayout());
		center.setOpaque(false);
        
		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		
		int gridx = 0;
		
        int start = 0;
        while (start <= problemSize) {
            JPanel table = createTable(start, start+5);
            table.setOpaque(false);
            
            c.gridx = gridx++;
            c.weightx = 1.0;         
            center.add(table, c);
            
            start+=5;
            
            if (start < problemSize) {
            	c.gridx = gridx++;
                c.weightx = 0;   
                center.add(createVerticalSeparator(), c);
            }
        }

        add(center, BorderLayout.CENTER);
		
		bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
		bottom.setOpaque(false);
		bottom.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
		bottom.add(correctLabel);
		bottom.add(wrongLabel);
		
		add(top,BorderLayout.NORTH);
		add(center,BorderLayout.CENTER);
		add(bottom,BorderLayout.SOUTH);
	}
	
	private JPanel createVerticalSeparator() {
	    JPanel sep = new JPanel();
	    sep.setBackground(new Color(180,180,180));
	    sep.setPreferredSize(new Dimension(2, 1));
	    sep.setMaximumSize(new Dimension(2, Integer.MAX_VALUE));
	    return sep;
	}

    private JPanel createTable(int start, int end) {
    	
        JPanel table = new JPanel(new GridBagLayout());
        table.setOpaque(false);

        GridBagConstraints base = new GridBagConstraints();
        base.fill = GridBagConstraints.BOTH;
        base.weighty = 0;

        addHeaderCell(table, "NUM",    0, 0, 0.1, base);
        addSeparator(table, 0, 1, base);
        addHeaderCell(table, "YOUR ANSWER", 0, 2, 0.3, base);
        addSeparator(table, 0, 3, base);
        addHeaderCell(table, "ANSWER",    0, 4, 0.2, base);
        addSeparator(table, 0, 5, base);
        addHeaderCell(table, "O/X",       0, 6, 0.1, base);

        for (int row = start; row < end; row++) {
        	int y = (row - start) + 1;
        	
        	if (row >= problemSize) {
        		addRowCell(table, " ", y, 0, 0.1, base);
                addSeparator(table, y, 1, base);

                addRowCell(table, " ", y, 2, 0.3, base);
                addSeparator(table, y, 3, base);

                addRowCell(table, " ", y, 4, 0.2, base);
                addSeparator(table, y, 5, base);

                addRowCell(table, " ", y, 6, 0.1, base);
                continue;
        	}

            addRowCell(table, String.valueOf(row + 1), y, 0, 0.1, base);
            addSeparator(table, y, 1, base);

            addRowCell(table, problems[row].getPlayerAnswer(), y, 2, 0.3, base);
            addSeparator(table, y, 3, base);

            addRowCell(table, problems[row].getAnswer(), y, 4, 0.2, base);
            addSeparator(table, y, 5, base);

            addRowCell(table, ox[row].getText(), y, 6, 0.1, base);
        }

        return table;
    }
    
    private JPanel makeCellWrapper(JLabel label) {
        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        wrap.setMinimumSize(new Dimension(0, 50));
        wrap.setPreferredSize(new Dimension(0, 50));
        wrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        wrap.add(label);
        return wrap;
    }

    private void addHeaderCell(JPanel table, String text, int y, int x, double weightx, GridBagConstraints base) {
        GridBagConstraints c = (GridBagConstraints) base.clone();
        c.gridx = x;
        c.gridy = y;
        c.weightx = weightx;
        c.weighty = 0;
        
        ResponsiveLabel label = new ResponsiveLabel(text, 0.035f, table);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(ColorUtils.getContrastColor(getBackground()));
        
        JPanel wrap = makeCellWrapper(label);

        table.add(wrap, c);
    }
    
    private void addRowCell(JPanel table, String text, int y, int x, double weightx, GridBagConstraints base) {
        GridBagConstraints c = (GridBagConstraints) base.clone();
        c.gridx = x;
        c.gridy = y;
        c.weightx = weightx;
        c.weighty = 1;

        ResponsiveLabel label = new ResponsiveLabel(text, 0.03f, table);
        label.setFont(new Font("Arial", Font.BOLD, 26));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(ColorUtils.getContrastColor(getBackground()));
        
        if(text.equals("X")) {
        	label.setForeground(Color.RED);
        }
        else if(text.equals("O")) {
        	label.setForeground(Color.BLUE);
        }
        
        JPanel wrap = makeCellWrapper(label);

        table.add(wrap, c);
    }
    
    private void addSeparator(JPanel table, int y, int x, GridBagConstraints base) {
        GridBagConstraints c = (GridBagConstraints)base.clone();
        c.gridx = x;
        c.gridy = y;
        c.weightx = 0;
        c.weighty = 1;
        c.fill = GridBagConstraints.NONE;

        JPanel sep = new JPanel();
        sep.setBackground(new Color(180,180,180));
        sep.setPreferredSize(new Dimension(1,25));
        table.add(sep, c);
    }
 
	
	private void checkAnswer() {
		int cnt = 0;
		for(Problem p : problems) {
			if(p.getPlayerAnswer().equals(p.getAnswer())) {
				ox[cnt] = new JLabel("O");
				correct++;
			}
			else {
				ox[cnt] = new JLabel("X");
				wrong++;
			}		
			cnt++;
		}
	}
}
