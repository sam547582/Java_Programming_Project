package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.*;

public class SettingPanel extends JPanel {
	
	private MainFrame frame;

	private JPanel panel;

	private JPanel top;
	private JPanel bottom;

	private JPanel settingWrapper;
	private JPanel backWrapper;
	private JPanel applyWrapper;

	private RoundComponent<JTextField> fieldName;

	private String name;
	private String elective;
	private int targetGrade;
	
	private String[] elecSet;
	private String[] gradeSet;
	
	SettingPanel(MainFrame frame) {
		setLayout(new BorderLayout());
		setBackground(new Color(45, 50, 58));
		
		this.frame = frame;
		
		name = StatsManager.getName();
		elective = StatsManager.getElective();
		targetGrade = StatsManager.getTargetGrade();
		
		elecSet = new String[] { "Prob&Stats", "Calculus", "Geometry" };
		gradeSet = new String[] { "1", "2", "3", "4", "5" };
		
		panel = new JPanel(new GridBagLayout());
		panel.setOpaque(false);

		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(null);
		
		RoundComponent<JPanel> wrapper = new RoundComponent<>(JPanel.class, new Color(0, 0, 0, 0),
				new Color(0, 0, 0, 100), 30);
		wrapper.setLayout(new BorderLayout());
		wrapper.add(scrollPane,BorderLayout.CENTER);
		
		JLabel setting = new JLabel("Settings");
		setting.setFont(new Font("Arial", Font.BOLD, 45));
		setting.setForeground(new Color(230, 233, 238));

		settingWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
		settingWrapper.setOpaque(false);
		settingWrapper.add(setting);

		RoundComponent<JButton> back = new RoundComponent<>(JButton.class, new Dimension(120, 30),
				new Color(0, 0, 0, 0), new Color(55, 65, 85), "BACK", new Color(235, 240, 245),
				new Font("Arial", Font.BOLD, 25), 20);
		back.getInner().addActionListener(e -> frame.showPanel("menu"));
		back.setHoverBackground(new Color(75, 90, 120));
		back.setHoverForeground(new Color(245, 248, 252));

		backWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
		backWrapper.setOpaque(false);
		backWrapper.add(back);

		RoundComponent<JButton> apply = new RoundComponent<>(JButton.class, new Dimension(140, 40),
				new Color(0, 0, 0, 0), new Color(55, 65, 85), "APPLY", new Color(235, 240, 245),
				new Font("Arial", Font.BOLD, 25), 20);
		apply.getInner().addActionListener(e -> {
			if (JOptionPane.showConfirmDialog(null, "Are you sure you want to apply?", "APPLY",
					JOptionPane.YES_NO_OPTION) == 0) {
				setApply();
			}

		});
		apply.setHoverBackground(new Color(75, 90, 120));
		apply.setHoverForeground(new Color(245, 248, 252));

		applyWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
		applyWrapper.setOpaque(false);
		applyWrapper.add(apply);

		top = new JPanel(new BorderLayout());
		top.setOpaque(false);
		top.add(settingWrapper, BorderLayout.CENTER);
		top.add(backWrapper, BorderLayout.WEST);
		top.add(Box.createRigidArea(new Dimension(backWrapper.getPreferredSize())), BorderLayout.EAST);

		bottom = new JPanel(new BorderLayout());
		bottom.setOpaque(false);
		bottom.add(applyWrapper, BorderLayout.CENTER);

		add(wrapper, BorderLayout.CENTER);
		add(top, BorderLayout.NORTH);
		add(bottom, BorderLayout.SOUTH);

		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(10, 15, 10, 15);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.weightx = 1;
		gbc.gridy = 0;

		panel.add(createRow("name"), gbc);

		List<RoundComponent<JButton>> btnElective = new ArrayList<>();
		List<RoundComponent<JButton>> btnGrade = new ArrayList<>();

		btnElective.add(new RoundComponent<>(JButton.class, new Dimension(190, 40), new Color(0, 0, 0, 0),
				new Color(55, 60, 70), "Prob & Stats", new Color(220, 225, 230), new Font("Arial", Font.BOLD, 25), 20));
		btnElective.add(new RoundComponent<>(JButton.class, new Dimension(190, 40), new Color(0, 0, 0, 0),
				new Color(55, 60, 70), "Calculus", new Color(220, 225, 230), new Font("Arial", Font.BOLD, 25), 20));
		btnElective.add(new RoundComponent<>(JButton.class, new Dimension(190, 40), new Color(0, 0, 0, 0),
				new Color(55, 60, 70), "Geometry", new Color(220, 225, 230), new Font("Arial", Font.BOLD, 25), 20));

		if (elective.equals("Prob&Stats")) {
			btnElective.get(0).setBackground(new Color(90, 155, 255));
			btnElective.get(0).setForeground(Color.WHITE);
			btnElective.get(0).setHoverBackground(ColorUtils.getShadowColor(new Color(90, 155, 255)));
		} else if (elective.equals("Calculus")) {
			btnElective.get(1).setBackground(new Color(90, 155, 255));
			btnElective.get(1).setForeground(Color.WHITE);
			btnElective.get(1).setHoverBackground(ColorUtils.getShadowColor(new Color(90, 155, 255)));
		} else {
			btnElective.get(2).setBackground(new Color(90, 155, 255));
			btnElective.get(2).setForeground(Color.WHITE);
			btnElective.get(2).setHoverBackground(ColorUtils.getShadowColor(new Color(90, 155, 255)));
		}

		for (int i = 0; i < 3; i++) {
			final int idx = i;
			btnElective.get(i).getInner().addActionListener(e -> {

				elective = elecSet[idx];

				for (RoundComponent<JButton> b : btnElective) {
					b.setBackground(new Color(55, 60, 70));
					b.setForeground(new Color(220, 225, 230));
					b.setHoverBackground(ColorUtils.getShadowColor(new Color(55, 60, 70)));
				}

				btnElective.get(idx).setBackground(new Color(90, 155, 255));
				btnElective.get(idx).setForeground(Color.WHITE);
				btnElective.get(idx).setHoverBackground(ColorUtils.getShadowColor(new Color(90, 155, 255)));
			});
		}

		btnGrade.add(new RoundComponent<>(JButton.class, new Dimension(80, 40), new Color(0, 0, 0, 0),
				new Color(55, 60, 70), "1", new Color(220, 225, 230), new Font("Arial", Font.BOLD, 25), 20));
		btnGrade.add(new RoundComponent<>(JButton.class, new Dimension(80, 40), new Color(0, 0, 0, 0),
				new Color(55, 60, 70), "2", new Color(220, 225, 230), new Font("Arial", Font.BOLD, 25), 20));
		btnGrade.add(new RoundComponent<>(JButton.class, new Dimension(80, 40), new Color(0, 0, 0, 0),
				new Color(55, 60, 70), "3", new Color(220, 225, 230), new Font("Arial", Font.BOLD, 25), 20));
		btnGrade.add(new RoundComponent<>(JButton.class, new Dimension(80, 40), new Color(0, 0, 0, 0),
				new Color(55, 60, 70), "4", new Color(220, 225, 230), new Font("Arial", Font.BOLD, 25), 20));
		btnGrade.add(new RoundComponent<>(JButton.class, new Dimension(80, 40), new Color(0, 0, 0, 0),
				new Color(55, 60, 70), "5", new Color(220, 225, 230), new Font("Arial", Font.BOLD, 25), 20));

		btnGrade.get(targetGrade - 1).setBackground(new Color(90, 155, 255));
		btnGrade.get(targetGrade - 1).setForeground(Color.WHITE);
		btnGrade.get(targetGrade - 1).setHoverBackground(ColorUtils.getShadowColor(new Color(90, 155, 255)));

		for (int i = 0; i < 5; i++) {
			final int idx = i;
			btnGrade.get(i).getInner().addActionListener(e -> {

				targetGrade = Integer.parseInt(gradeSet[idx]);

				for (RoundComponent<JButton> b : btnGrade) {
					b.setBackground(new Color(55, 60, 70));
					b.setForeground(new Color(220, 225, 230));
					b.setHoverBackground(ColorUtils.getShadowColor(new Color(55, 60, 70)));
				}

				btnGrade.get(idx).setBackground(new Color(90, 155, 255));
				btnGrade.get(idx).setForeground(Color.WHITE);
				btnGrade.get(idx).setHoverBackground(ColorUtils.getShadowColor(new Color(90, 155, 255)));

			});
		}

		gbc.gridy = 1;
		panel.add(createRow("elective", btnElective), gbc);

		gbc.gridy = 2;
		panel.add(createRow("TargetGrade", btnGrade), gbc);
	}

	private void setApply() {
		StatsManager.updateName(name);
		StatsManager.updateElective(elective);
		StatsManager.updateTargetGrade(targetGrade);
	}

	private RoundComponent<JPanel> createRow(String label) {
		RoundComponent<JPanel> rowPanel = new RoundComponent<>(JPanel.class, new Color(0, 0, 0, 0),
				new Color(20, 24, 30, 190), 30);
		rowPanel.setLayout(new BorderLayout());

		JPanel wrapper = new JPanel(new GridBagLayout());
		wrapper.setOpaque(false);
		fieldName = new RoundComponent<>(JTextField.class, new Dimension(400, 40), new Color(0, 0, 0, 0), Color.WHITE,
				"", Color.BLACK, new Font("Arial", Font.PLAIN, 20), 20);
		fieldName.getInner().setText(name);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 30, 10, 30);

		gbc.gridx = 0;
		gbc.weightx = 0;
		wrapper.add(createLabel(label), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0;
		wrapper.add(fieldName, gbc);

		rowPanel.add(wrapper, BorderLayout.CENTER);

		return rowPanel;
	}

	private RoundComponent<JPanel> createRow(String label, List<RoundComponent<JButton>> button) {
		RoundComponent<JPanel> rowPanel = new RoundComponent<>(JPanel.class, new Color(0, 0, 0, 0),
				new Color(20, 24, 30, 190), 30);
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
			if (row == button.size())
				gbc.insets = new Insets(10, 10, 10, 30);
			gbc.gridx = row;
			gbc.weightx = 0;
			wrapper.add(btn, gbc);
			row++;
		}

		rowPanel.add(wrapper, BorderLayout.CENTER);

		return rowPanel;
	}

	private JLabel createLabel(String text) {
		JLabel label = new JLabel(text + " : ");
		label.setForeground(new Color(220, 220, 220));
		label.setFont(new Font("Arial", Font.BOLD, 30));
		return label;
	}

}
