package ui;

import java.awt.*;
import javax.swing.*;

class FadeLabel extends JComponent {

    private float alpha = 0f;
    private String text;
    private Font font;
    private Color fontColor;
    
    FadeLabel(String text) {
        this.text = text;
        this.font = new Font("Arial", Font.PLAIN, 40);
        
        setOpaque(false);

        // 폰트 메트릭 기반으로 preferred size 고정
        FontMetrics fm = getFontMetrics(font);
        int w = fm.stringWidth(text);
        int h = fm.getHeight();
        
        Dimension size = new Dimension(w, h);

        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        
    }
    
    public void setFont(Font font) {
    	this.font = font;
    	repaint();
    }
    
    public void setFontColor(Color color) {
    	this.fontColor = color;
    }
    
    public void setAlpha(float value) {
        alpha = Math.min(1f, Math.max(0f, value));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        
        g2.setFont(font);
        g2.setColor(fontColor);

        FontMetrics fm = g2.getFontMetrics();
        int x = 0;
        int y = fm.getAscent();
        
        g2.drawString(text, x, y);
        g2.dispose();
    }

    public void fadeIn(int duration) {
        int delay = 20;
        int steps = duration / delay;

        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            alpha += 1f / steps;
            if (alpha >= 1f) {
                alpha = 1f;
                timer.stop();
            }
            repaint();
        });

        alpha = 0f;
        timer.start();
    }
    
    public void fadeOut(int duration, Runnable after) {
        int delay = 20;
        int steps = duration / delay;

        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {

            alpha -= 1f / steps;
            if (alpha <= 0f) {
                alpha = 0f;
                ((Timer)e.getSource()).stop();
                
                if (after != null) after.run();
            }
            repaint();
        });

        alpha = 1f;
        timer.start();
    }
}