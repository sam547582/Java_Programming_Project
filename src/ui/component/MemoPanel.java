package ui.component;

import javax.swing.*;

import util.ColorUtils;

import java.awt.*;

public class MemoPanel extends JPanel {
	
	private JTextArea field;
	private JScrollPane scroll;
	
	public MemoPanel() {
		
		setLayout(new BorderLayout());
		setOpaque(false);
		
		field = new JTextArea();
		field.setFont(new Font("Arial",Font.PLAIN,25));
		
		scroll = new JScrollPane(field);
		
		add(scroll, BorderLayout.CENTER);
	}
	
	public void updateColor(Color color) {
		field.setBackground(color);
		field.setForeground(ColorUtils.getContrastColor(color));
	}
}
