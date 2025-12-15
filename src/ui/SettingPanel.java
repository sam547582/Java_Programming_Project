package ui;

import javax.swing.*;
import java.awt.*;
import util.*;

public class SettingPanel extends JPanel {

	private JPanel panel;
	
	SettingPanel(MainFrame frame) {
		setLayout(new BorderLayout());
		setBackground(new Color(45, 50, 58));
		
		panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);
		
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(null);

		add(scrollPane, BorderLayout.CENTER);
		
		int row = 0;
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.insets = new Insets(10, 15, 10, 15);
		gbc.weightx = 1;
		gbc.gridy = row;
		
		panel.add(createRow("name"),gbc);
	}

	private RoundComponent<JPanel> createRow(String label) {
		RoundComponent<JPanel> rowPanel = new RoundComponent<>(JPanel.class,new Color(0, 0, 0, 0),
				new Color(0, 0, 0, 180), 30);
		rowPanel.setLayout(new BorderLayout());
		
		JPanel wrapper = new JPanel(new GridBagLayout());
		wrapper.setOpaque(false);
		RoundComponent<JTextField> field = new RoundComponent<>(JTextField.class, new Dimension(200, 40), new Color(0, 0, 0, 0), Color.WHITE,
				"", Color.BLACK, new Font("Arial", Font.PLAIN, 20), 20);
		
	    
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 40, 10, 40);

		gbc.gridx = 0;
		gbc.weightx = 0;
		wrapper.add(createLabel(label), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0;
		wrapper.add(field, gbc);
		
		rowPanel.add(wrapper,BorderLayout.CENTER);
		
		return rowPanel;
	}
	

	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text + " : ");
		label.setForeground(new Color(220, 220, 220));
		label.setFont(new Font("Arial", Font.BOLD, 30));
		return label;
	}

}
