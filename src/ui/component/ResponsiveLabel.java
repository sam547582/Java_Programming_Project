package ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ResponsiveLabel extends JLabel {

    private float baseRatio;
    private JComponent base;
    
    public ResponsiveLabel(String text, float ratio, JComponent base) {
        super(text);
        this.baseRatio = ratio;
        this.base = base;
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeFont();
            }
        });
    }
    
    
    private void resizeFont() {
        if (base == null) return;

        int w = base.getWidth();
        if (w <= 0) return;

        float newSize = w * baseRatio;
        
        newSize = Math.max(newSize, 12f);
        newSize = Math.min(newSize, 40f);
        
        setFont(getFont().deriveFont(newSize));
        revalidate();
        repaint();
    }
}

