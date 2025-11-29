package ui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FadeButton extends JComponent {

    private float alpha = 0f;                 // 투명도
    private String text;                      // 버튼에 표시될 텍스트
    private Font font = new Font("Arial", Font.BOLD, 22);

    private Color backgroundColor = new Color(60, 120, 200);
    private Color fontColor = Color.WHITE;
    private Color hoverColor = new Color(80, 140, 220);
    private Color pressedColor = new Color(40, 100, 180);
    private Color currentColor = backgroundColor;
    private Color borderColor = new Color(255, 255, 255);
    private float borderThickness = 2f;
    
    private boolean hovered = false;
    private boolean pressed = false;

    private int arc = 20;                     // 둥근 모서리 정도

    private ActionListener clickListener;


    public FadeButton(String text, int width, int height) {
        this.text = text;
        this.alpha = 0f;
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setOpaque(false);
        setFocusable(true);

        // 마우스 인터랙션
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                currentColor = hoverColor;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                currentColor = backgroundColor;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                pressed = true;
                currentColor = pressedColor;
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                currentColor = hovered ? hoverColor : backgroundColor;
                repaint();

                // 클릭 처리
                if (clickListener != null) {
                    clickListener.actionPerformed(
                        new ActionEvent(FadeButton.this, ActionEvent.ACTION_PERFORMED, text)
                    );
                }
            }
        });
    }

    public void setButtonColor(Color color) {
        this.backgroundColor = color;
        this.currentColor = color;
    }
    
    public void setFont(Font font) {
        this.font = font;
    }
    
    public void setFontColor(Color color) {
        this.fontColor = color;
    }
    
    public void setBorderColor(Color c) {
        this.borderColor = c;
    }

    public void setBorderThickness(float t) {
        this.borderThickness = t;
    }

    public void setCornerRadius(int radius) {
        this.arc = radius;
    }

    public void addActionListener(ActionListener l) {
        this.clickListener = l;
    }

    public void setAlpha(float a) {
        alpha = Math.max(0f, Math.min(1f, a));
        repaint();
    }


    // ========== 페이드 기능 ==========
    public void fadeIn(int duration) {
        int delay = 20;
        int steps = duration / delay;

        Timer timer = new Timer(delay, null);
        timer.addActionListener(e -> {
            alpha += 1f / steps;
            if (alpha >= 1f) {
                alpha = 1f;
                ((Timer)e.getSource()).stop();
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


    // ========== 그리기 ==========
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // 안티알리아싱
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // 투명도
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        int w = getWidth();
        int h = getHeight();
        
        // 테두리
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(borderThickness));
        g2.drawRoundRect(1, 1, w - 2, h - 2, arc, arc);

        int inset = (int)borderThickness;
        g2.setColor(currentColor);
        g2.fillRoundRect(inset, inset, w - inset*2, h - inset*2, arc - inset, arc - inset);
        
        // 텍스트
        g2.setColor(fontColor);
        g2.setFont(font);

        FontMetrics fm = g2.getFontMetrics();
        int tw = fm.stringWidth(text);
        int th = fm.getHeight();
        int ascent = fm.getAscent();

        int x = (w - tw) / 2;
        int y = (h - th) / 2 + ascent;

        g2.drawString(text, x, y);

        g2.dispose();
    }
}
