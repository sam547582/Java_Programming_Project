package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import util.*;

public class StartPanel extends JPanel {
	
	private JPanel wrapperCurrent;
	private JPanel wrapperNext;
	
		
	StartPanel(MainFrame frame) {
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);
		
		FadeLabel labelName = new FadeLabel("Enter your name");
		labelName.setOpaque(false);
		labelName.setFontColor(ColorUtils.getContrastColor(getBackground()));
		
		FadeTextField fieldName = new FadeTextField(300, 40);
		fieldName.setBorderColor(ColorUtils.getContrastColor(getBackground()));
		fieldName.setFontColor(ColorUtils.getContrastColor(getBackground()));
		fieldName.setBackColor(getBackground());
		
		wrapperCurrent = makeWrapper(labelName, fieldName);
		
		FadeLabel label2 = new FadeLabel("What is your grade?");
        label2.setFontColor(Color.WHITE);

        FadeTextField field2 = new FadeTextField(300, 40);
        field2.setFontColor(Color.WHITE);
        field2.setBackColor(Color.BLACK);
        field2.setBorderColor(Color.WHITE);
        
        wrapperNext = makeWrapper(label2, field2);
        
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(wrapperCurrent);
		
        add(center,BorderLayout.CENTER);
        
		labelName.fadeIn(500);
		fieldName.fadeIn(500);
		
		fieldName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	fieldName.fadeOut(500, () -> {

                	    center.remove(wrapperCurrent);
                	    center.add(wrapperNext);

                	    center.revalidate();
                	    center.repaint();

                	    label2.fadeIn(500);
                	    field2.fadeIn(500);
                	    
                	    field2.requestFocusInWindow();
                	});
                	labelName.fadeOut(500, null);
                }
            }
        });
	}
	
	private JPanel makeWrapper(FadeLabel label, FadeTextField field) {
	    JPanel w = new JPanel();
	    w.setOpaque(false);
	    w.setLayout(new BoxLayout(w, BoxLayout.Y_AXIS));
	    
	    label.setFontColor(ColorUtils.getContrastColor(getBackground()));
	    label.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    field.setBorderColor(ColorUtils.getContrastColor(getBackground()));
		field.setFontColor(ColorUtils.getContrastColor(getBackground()));
		field.setBackColor(getBackground());
	    field.setAlignmentX(Component.CENTER_ALIGNMENT);

	    w.add(label);
	    w.add(Box.createVerticalStrut(20));
	    w.add(field);
	    
	    w.setSize(w.getPreferredSize());
	    return w;
	}
	

}
