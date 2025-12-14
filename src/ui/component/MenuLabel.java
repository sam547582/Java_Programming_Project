package ui.component;

import javax.swing.*;
import java.awt.*;

public class MenuLabel extends JLabel {

    private Font normalFont;
    private Font hoverFont;
    
    private Color normalColor;
    private Color hoverColor;
    
    public MenuLabel(String text) {
        super(text);
        
        normalFont = new Font("Arial", Font.BOLD | Font.ITALIC, 45);
        hoverFont = new Font("Arial", Font.BOLD | Font.ITALIC, 55);
        normalColor = Color.WHITE;
        hoverColor = Color.WHITE;
        
        setFont(normalFont);
        setForeground(Color.WHITE);
        
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.CENTER);

        setOpaque(false);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setFont(hoverFont);
                setForeground(hoverColor);
                
                Dimension d = getPreferredSize();    
                setBounds(0, 0, d.width+5, d.height);
                
				setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
            	setFont(normalFont);
            	setForeground(normalColor);
            	
            	Dimension d = getPreferredSize();
            	setBounds(0, 0, d.width+5, d.height);
                
				setCursor(Cursor.getDefaultCursor());

                repaint();
            }
        });
    }
    
    public void setHoverColor(Color color) {
    	hoverColor = color;
    }

}
