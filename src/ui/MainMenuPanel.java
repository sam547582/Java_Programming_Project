package ui;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
	
	public MainMenuPanel(MainFrame frame) {
		setLayout(new BorderLayout());
	    setBackground(new Color(30, 40, 60));
	    
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
		        frame.showPanel("difficulty"); 
		    }
		});		
		leftMenu.add(wrapLabel(start));
		leftMenu.add(Box.createVerticalStrut(20));
		
		leftMenu.add(wrapLabel(new MenuLabel("Shop")));
		leftMenu.add(Box.createVerticalStrut(20));

		leftMenu.add(wrapLabel(new MenuLabel("Settings")));
		leftMenu.add(Box.createVerticalStrut(20));


        add(title, BorderLayout.NORTH);
        add(leftMenu, BorderLayout.WEST);
        
        JPanel emptyRight = new JPanel();
        emptyRight.setOpaque(false);
        add(emptyRight, BorderLayout.CENTER);
	}
	
	private JPanel wrapLabel(MenuLabel label) {
	    JPanel panel = new JPanel(null);
	    panel.setOpaque(false);
	    
	    panel.setPreferredSize(new Dimension(300, 60));
	    panel.setMaximumSize(new Dimension(300,60));
	    
	    Dimension d = label.getPreferredSize();
	    label.setBounds(0,0,d.width+5,d.height);
	    
	    panel.add(label);

	    return panel;
	}
	



}
