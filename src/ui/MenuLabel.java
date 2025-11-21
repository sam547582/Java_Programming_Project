package ui;

import javax.swing.*;
import java.awt.*;

public class MenuLabel extends JLabel {

    private Font normalFont;
    private Font hoverFont;

    public MenuLabel(String text) {
        super(text);

        normalFont = new Font("Arial", Font.BOLD | Font.ITALIC, 28);
        hoverFont = new Font("Arial", Font.BOLD | Font.ITALIC, 34);

        setFont(normalFont);
        setForeground(Color.WHITE);
        
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.CENTER);

        setOpaque(false);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setFont(hoverFont);
                
                Dimension d = getPreferredSize();    
                setBounds(0, 0, d.width+5, d.height);
                
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            	setFont(normalFont);
            	
            	Dimension d = getPreferredSize();
            	setBounds(0, 0, d.width+5, d.height);
                
                repaint();
            }
        });
    }

}
