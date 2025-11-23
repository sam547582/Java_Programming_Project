package ui;

import javax.swing.*;
import java.awt.*;

public class DifficultyPanel extends JPanel {

    public DifficultyPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        setBackground(new Color(50, 60, 90));

        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper,BoxLayout.Y_AXIS));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setOpaque(false);
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setOpaque(false);
        
        JLabel title = new JLabel("Select Difficulty");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        
        titlePanel.add(title);
        
        JButton back = new JButton("Back");
        back.setFont(new Font("Arial", Font.BOLD, 15));
        back.setPreferredSize(back.getPreferredSize());
        back.addActionListener(e -> frame.showPanel("menu"));
        
        backPanel.add(back);
        
        wrapper.add(titlePanel);
        wrapper.add(backPanel);
        
        add(wrapper, BorderLayout.NORTH);
        
        JPanel center = new JPanel();
        center.setOpaque(false);
        center.setLayout(new BoxLayout(center, BoxLayout.X_AXIS));

        JButton easy = BtnCreate("<html><center> "
        		+ "Easy "
        		+ "<br><br><br> "
        		+ "2 points "
        		+ "<br> "
        		+ "or "
        		+ "<br> "
        		+ "easy 3 points "
        		+ "<br><br><br>"
        		+ "10 Coins / problem"
        		+ "</center></html>", frame);
        easy.addActionListener(e -> frame.showProblem("easy"));
        
        JButton normal = BtnCreate("<html><center> "
        		+ "Normal "
        		+ "<br><br><br> "
        		+ "hard 3 points "
        		+ "<br> "
        		+ "or "
        		+ "<br> "
        		+ "easy 4 points "
        		+ "<br><br><br>"
        		+ "50 Coins / problem"
        		+ "</center></html>", frame);
        normal.addActionListener(e -> frame.showProblem("normal"));
        
        JButton hard = BtnCreate("<html><center> "
        		+ "Hard "
        		+ "<br><br><br><br> "
        		+ "hard 4 points "
        		+ "<br><br><br><br>"
        		+ "100 Coins / problem"
        		+ "</center></html>", frame);
        hard.addActionListener(e -> frame.showProblem("hard"));
        
        JButton extreme = BtnCreate("<html><center> "
        		+ "Extreme "
        		+ "<br><br><br><br> "
        		+ "KILLER"
        		+ "<br><br><br><br>"
        		+ "300 Coins / problem"
        		+ "</center></html>", frame);
        extreme.addActionListener(e -> frame.showProblem("extreme"));
        
        center.add(Box.createHorizontalStrut(25));
        center.add(easy);
        center.add(Box.createHorizontalGlue());
        center.add(normal);
        center.add(Box.createHorizontalGlue());
        center.add(hard);
        center.add(Box.createHorizontalGlue());
        center.add(extreme);
        center.add(Box.createHorizontalStrut(25));
        
        add(center, BorderLayout.CENTER);
    }
    
    private JButton BtnCreate(String text,MainFrame frame) {
    	JButton btn = new JButton(text);
    	
    	btn.setFont(new Font("Arial", Font.BOLD, 15));
    	btn.setPreferredSize(new Dimension(150,200));
    	btn.setMaximumSize(new Dimension(150,200));
    	btn.setHorizontalAlignment(SwingConstants.CENTER);
    	btn.setVerticalAlignment(SwingConstants.TOP);
    	
    	btn.setOpaque(false);
    	btn.setFocusPainted(false);

    	return btn;
    }
}
