package ui;

import javax.swing.*;
import java.awt.*;

public class MenuLabel extends JLabel {

    private Font normalFont;
    private Font hoverFont;

    public MenuLabel(String text) {
        super(text);

        // 기본 폰트
        normalFont = new Font("맑은 고딕", Font.BOLD | Font.ITALIC, 28);
        hoverFont = new Font("맑은 고딕", Font.BOLD | Font.ITALIC, 34);  // hover 시 폰트

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
                d.width += 30; 
                
                setBounds(0, 0, d.width, getHeight());
                
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            	
            	Dimension d = getPreferredSize();
            	setBounds(0, 0, d.width, getHeight());
            	
            	setFont(normalFont);
                
                repaint();
            }
        });
    }

}
