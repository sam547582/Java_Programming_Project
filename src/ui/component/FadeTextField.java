package ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FadeTextField extends JComponent {

    private float alpha = 0f;
    private String text = "";
    private Font font = new Font("Arial", Font.PLAIN, 40);
    private Color borderColor;
    private Color fontColor;
    private Color backColor;
    
    public FadeTextField(int width, int height) {
    	
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setOpaque(false);

        // 키보드 입력 처리
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                
                if(text.length() >= 25) return;
                
                if (c == '\b') {
                	if (text.length() > 0) {
                        text = text.substring(0, text.length() - 1);
                    }
                }
                else if(!Character.isISOControl(c)){
                	text += c;
                }
                repaint();
            }
        });
        
        setFocusable(true);
    }
    
    public void setBorderColor(Color color) {
    	this.borderColor = color;
    }
    
    public void setFontColor(Color color) {
    	this.fontColor = color;
    }
    
    public void setBackColor(Color color) {
    	this.backColor = color;
    }
    
    public void setAlpha(float a) {
        alpha = Math.max(0f, Math.min(1f, a));
        repaint();
    }

    public String getText() {
        return text;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // 항상 AA 적용
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 투명도 적용
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        int w = getWidth();
        int h = getHeight();

        // 텍스트필드 배경
        g2.setColor(backColor);
        g2.fillRoundRect(0, 0, w, h, 12, 12);

        // 테두리
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(2f));
        g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);

        // 텍스트
        g2.setColor(fontColor);
        g2.setFont(font);

        FontMetrics fm = g2.getFontMetrics();
        
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        int ascent = fm.getAscent();

        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() - textHeight) / 2 + ascent;

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
