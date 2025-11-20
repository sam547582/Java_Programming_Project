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
	    
		JLabel title = new JLabel("수능 수학 훈련 프로그램", SwingConstants.CENTER);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 26));
		title.setForeground(Color.WHITE);
		
		JLabel easy = createMenuLabel("쉬움");
		leftMenu.add(easy);
		leftMenu.add(Box.createVerticalStrut(20));
		
		JLabel normal = createMenuLabel("보통");
		leftMenu.add(normal);
		leftMenu.add(Box.createVerticalStrut(20));
		
		JLabel hard = createMenuLabel("어려움");
		leftMenu.add(hard);
		leftMenu.add(Box.createVerticalStrut(20));
		
		JLabel start = createMenuLabel("게임 시작");
		leftMenu.add(start);

        add(title, BorderLayout.NORTH);
        add(leftMenu, BorderLayout.WEST);
        
        JPanel emptyRight = new JPanel();
        emptyRight.setOpaque(false);
        add(emptyRight, BorderLayout.CENTER);
	}
	
	private JLabel createMenuLabel(String text) {
	    JLabel label = new JLabel(text);
	    label.setForeground(Color.WHITE);
	    label.setFont(new Font("맑은 고딕", Font.BOLD | Font.ITALIC, 28));

	    label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
	    label.setAlignmentX(Component.LEFT_ALIGNMENT);


	    label.addMouseListener(new java.awt.event.MouseAdapter() {
	        @Override
	        public void mouseEntered(java.awt.event.MouseEvent e) {
	            label.setFont(new Font("맑은 고딕", Font.BOLD | Font.ITALIC, 32));
	        }

	        @Override
	        public void mouseExited(java.awt.event.MouseEvent e) {
	            label.setFont(new Font("맑은 고딕", Font.BOLD | Font.ITALIC, 28));
	        }

	        @Override
	        public void mouseClicked(java.awt.event.MouseEvent e) {
	            System.out.println(text + " 클릭됨");
	        }
	    });

	    return label;
	}


}
