package ui;

import java.awt.*;
import javax.swing.*;
import util.*;

public class CalcPanel extends JPanel {

    private JTextField display;   // 식 표시 + 결과 표시
    private JTextField xField;    // x 값 입력
    // x 계산 버튼
    private JPanel buttonPanel;
    private JPanel xPanel;
    public CalcPanel() {

        setLayout(new BorderLayout());
        setOpaque(false);

        // -----------------------------------
        // (1) Display 영역 (식 표시용)
        // -----------------------------------
        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setHorizontalAlignment(SwingConstants.CENTER);
        add(display, BorderLayout.NORTH);

        // -----------------------------------
        // (2) 일반 계산기 버튼 (센터)
        // -----------------------------------
        
        buttonPanel = new JPanel(new GridLayout(5, 4, 5, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        buttonPanel.setOpaque(false);
        String[] buttons = {
            "7","8","9","/",     
            "4","5","6","*",     
            "1","2","3","-",     
            "0","x","C","+",      // x 버튼 추가!
            "^", "(", ")", "="    // 지수, 괄호 등 다항식 계산용
        };
        
        int cnt = 0;
        for (String text : buttons) {
        	JButton btn = new JButton(text);
            btn.setFont(new Font("Arial", Font.BOLD, 24));
            
            buttonPanel.add(btn);

            // ★ 버튼 기능은 나중에 붙일 거야
            btn.addActionListener(e -> {
                String t = btn.getText();

                if (t.equals("C")) {
                    display.setText("");
                } 
                else if (t.equals("=")) {
                	String expr = display.getText();
                    try {
                    	if (!xField.getText().isBlank() && expr.contains("x")) {

                            double xVal = Double.parseDouble(xField.getText());
                            double result = ExpressionEvaluator.evaluateWithX(expr, xVal);
                            display.setText(String.valueOf(result));

                        } else {
                            // 일반 계산
                            double result = ExpressionEvaluator.evaluate(expr);
                            display.setText(String.valueOf(result));
                        }
                    } catch (Exception ex) {
                        display.setText("Error");
                    }
                } 
                else {
                    // display에 문자 붙이기
                    display.setText(display.getText() + t);
                }
            });
        }
        
        add(buttonPanel, BorderLayout.CENTER);

        xPanel = new JPanel(new FlowLayout());
        xPanel.add(new JLabel("x = "));
        xPanel.getComponent(0).setFont(new Font("Arial",Font.BOLD,20));
        xPanel.setOpaque(false);
        xField = new JTextField(6);
        xField.setFont(new Font("Arial", Font.PLAIN, 22));
        xPanel.add(xField);
        
        add(xPanel, BorderLayout.SOUTH);
        
        updateColor(new Color(255,255,255));
    }
    
    public void updateColor(Color color) {
    	for(Component comp : getComponents()) {
    		comp.setBackground(color);
    		comp.setForeground(ColorUtils.getContrastColor(color));
    	}
    	
    	for(Component comp : buttonPanel.getComponents()) {
    		comp.setBackground(color);
    		comp.setForeground(ColorUtils.getContrastColor(color));
    	}
    	
    	for(Component comp : xPanel.getComponents()) {
    		comp.setBackground(color);
    		comp.setForeground(ColorUtils.getContrastColor(color));
    	}
    }
}
