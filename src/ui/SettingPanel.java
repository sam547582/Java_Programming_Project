package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.insets = new Insets(10, 15, 10, 15);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1;
		gbc.gridy = 0;
		
		panel.add(createRow("name"),gbc);
		
		List<RoundComponent<JButton>> btnElective = new ArrayList<>();
		List<RoundComponent<JButton>> btnGrade = new ArrayList<>();
		
		btnElective.add(new RoundComponent<>(JButton.class, new Dimension(200, 40), new Color(0, 0, 0, 0), Color.BLACK,
				"Prob & Stats", Color.WHITE, new Font("Arial", Font.BOLD, 25), 20));
		btnElective.add( new RoundComponent<>(JButton.class, new Dimension(200, 40), new Color(0, 0, 0, 0), Color.BLACK,
				"Calculus", Color.WHITE, new Font("Arial", Font.BOLD, 25), 20));
		btnElective.add(new RoundComponent<>(JButton.class, new Dimension(200, 40), new Color(0, 0, 0, 0), Color.BLACK,
				"Geometry", Color.WHITE, new Font("Arial", Font.BOLD, 25), 20));
		
		btnGrade.add(new RoundComponent<>(JButton.class, new Dimension(80, 40), new Color(0, 0, 0, 0), Color.BLACK,
				"1", Color.WHITE, new Font("Arial", Font.BOLD, 25), 20));
		btnGrade.add(new RoundComponent<>(JButton.class, new Dimension(80, 40), new Color(0, 0, 0, 0), Color.BLACK,
				"2", Color.WHITE, new Font("Arial", Font.BOLD, 25), 20));
		btnGrade.add(new RoundComponent<>(JButton.class, new Dimension(80, 40), new Color(0, 0, 0, 0), Color.BLACK,
				"3", Color.WHITE, new Font("Arial", Font.BOLD, 25), 20));
		btnGrade.add(new RoundComponent<>(JButton.class, new Dimension(80, 40), new Color(0, 0, 0, 0), Color.BLACK,
				"4", Color.WHITE, new Font("Arial", Font.BOLD, 25), 20));
		btnGrade.add(new RoundComponent<>(JButton.class, new Dimension(80, 40), new Color(0, 0, 0, 0), Color.BLACK,
				"5", Color.WHITE, new Font("Arial", Font.BOLD, 25), 20));
		
		gbc.gridy = 1;
		panel.add(createRow("elective",btnElective),gbc);
		
		gbc.gridy = 2;
		panel.add(createRow("TargetGrade",btnGrade),gbc);
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
		gbc.insets = new Insets(10, 30, 10, 30);

		gbc.gridx = 0;
		gbc.weightx = 0;
		wrapper.add(createLabel(label), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0;
		wrapper.add(field, gbc);
		
		rowPanel.add(wrapper,BorderLayout.CENTER);

		return rowPanel;
	}
	
	private RoundComponent<JPanel> createRow(String label, List<RoundComponent<JButton>> button) {
		RoundComponent<JPanel> rowPanel = new RoundComponent<>(JPanel.class,new Color(0, 0, 0, 0),
				new Color(0, 0, 0, 180), 30);
		rowPanel.setLayout(new BorderLayout());
		
		JPanel wrapper = new JPanel(new GridBagLayout());
		wrapper.setOpaque(false);
		
	    
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 30, 10, 10);

		gbc.gridx = 0;
		gbc.weightx = 0;
		wrapper.add(createLabel(label), gbc);
		
		int row = 1;
		gbc.insets = new Insets(10, 10, 10, 10);
		for (RoundComponent<JButton> btn : button) {
			if(row == button.size()) gbc.insets = new Insets(10, 10, 10, 30);
			gbc.gridx = row;
			gbc.weightx = 0;
			wrapper.add(btn, gbc);
			row++;
		}
		
		
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
