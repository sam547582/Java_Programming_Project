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
	
	private MenuLabel menu;
	
	private JLabel[] problemNumberLabel;
	private JLabel resultLabel;
	private JLabel correctLabel;
	private JLabel wrongLabel;
	private JLabel[] ox;
	
	private int correct;
	private int wrong;
	private int problemSize;
	private int setSize;
	
	ResultPanel(MainFrame frame, Problem[] problems) {
		this.problems = problems;
		problemSize = problems.length;
		problemNumberLabel = new JLabel[problemSize];
		ox = new JLabel[problemSize];
		correct = wrong = 0;
		
		if(problemSize < 5) setSize = 1;
		else if(problemSize >= 5 && problemSize < 10) setSize = 2;
		else setSize = 3;
		
		setLayout(new BorderLayout());
		setBackground(new Color(30,40,60));
		frame.setSize(1000,550);
		
		checkAnswer();
		
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
		
        JPanel center = new JPanel(new GridLayout(1, setSize, 20, 0));
        center.setOpaque(false);

        for (int start = 0; start < problemSize; start += 5) {
            int end = Math.min(start + 5, problemSize);
            JPanel table = createTable(start, end);
            table.setOpaque(false);

            table.setPreferredSize(new Dimension(320, table.getPreferredSize().height));
            table.setMaximumSize(new Dimension(320, table.getPreferredSize().height));

            center.add(table);
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
	
    private JPanel createTable(int start, int end) {
        JPanel table = new JPanel(new GridBagLayout());
        table.setOpaque(false);

        GridBagConstraints base = new GridBagConstraints();
        base.fill = GridBagConstraints.BOTH;
        base.weighty = 0;

        // ---------- HEADER ----------
        addHeaderCell(table, "NUMBER",    0, 0, 0.1, base);
        addSeparator(table, 0, 1, base);
        addHeaderCell(table, "YOUR ANSWER", 0, 2, 0.4, base);
        addSeparator(table, 0, 3, base);
        addHeaderCell(table, "ANSWER",    0, 4, 0.4, base);
        addSeparator(table, 0, 5, base);
        addHeaderCell(table, "O/X",       0, 6, 0.1, base);

        // ---------- ROWS ----------
        for (int row = start; row < end; row++) {
            int y = (row - start) + 1;

            addRowCell(table, String.valueOf(row + 1), y, 0, 0.1, base);
            addSeparator(table, y, 1, base);

            addRowCell(table, problems[row].getPlayerAnswer(), y, 2, 0.4, base);
            addSeparator(table, y, 3, base);

            addRowCell(table, problems[row].getAnswer(), y, 4, 0.4, base);
            addSeparator(table, y, 5, base);

            addRowCell(table, ox[row].getText(), y, 6, 0.1, base);
        }

        return table;
    }
    
    private void addHeaderCell(JPanel table, String text, int y, int x, double weightx, GridBagConstraints base) {
        GridBagConstraints c = (GridBagConstraints) base.clone();
        c.gridx = x;
        c.gridy = y;
        c.weightx = weightx;

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 30));
        label.setForeground(ColorUtils.getContrastColor(getBackground()));

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        wrap.add(label);

        table.add(wrap, c);
    }
    
    private void addRowCell(JPanel table, String text, int y, int x, double weightx, GridBagConstraints base) {
        GridBagConstraints c = (GridBagConstraints) base.clone();
        c.gridx = x;
        c.gridy = y;
        c.weightx = weightx;
        c.weighty = 1;

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 26));
        label.setForeground(ColorUtils.getContrastColor(getBackground()));

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setOpaque(false);
        wrap.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        wrap.add(label);

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
