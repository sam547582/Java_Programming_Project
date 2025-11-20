package ui;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
	
	public MainMenuPanel() {
		setLayout(new BorderLayout());
	    setBackground(new Color(30, 40, 60));
	    
	    JPanel leftMenu = new JPanel();
	    leftMenu.setLayout(new BoxLayout(leftMenu, BoxLayout.Y_AXIS));
	    leftMenu.setOpaque(false);

	    leftMenu.add(Box.createVerticalStrut(60));
	    
		JLabel title = new JLabel("수능 수학 훈련 프로그램", SwingConstants.CENTER);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 26));
		title.setForeground(Color.WHITE);
		
		leftMenu.add(wrapLabel(new MenuLabel("시작"), 40));
		leftMenu.add(Box.createVerticalStrut(20));
		
		leftMenu.add(wrapLabel(new MenuLabel("상점"), 40));
		leftMenu.add(Box.createVerticalStrut(20));

		leftMenu.add(wrapLabel(new MenuLabel("설정"), 40));
		leftMenu.add(Box.createVerticalStrut(20));


        add(title, BorderLayout.NORTH);
        add(leftMenu, BorderLayout.WEST);
        
        JPanel emptyRight = new JPanel();
        emptyRight.setOpaque(false);
        add(emptyRight, BorderLayout.CENTER);
	}
	
	private JPanel wrapLabel(MenuLabel label, int height) {
	    JPanel panel = new JPanel(null);
	    panel.setOpaque(false);
	    
	    panel.setPreferredSize(new Dimension(150, height));
	    panel.setMaximumSize(new Dimension(150, height));
	    
	    label.setBounds(0, 0, 150, height);

	    panel.add(label);

	    return panel;
	}
	



}
