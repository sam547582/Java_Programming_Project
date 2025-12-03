package ui;

import javax.swing.*;

import util.RoundComponent;

import java.awt.*;

public class SelectPanel extends JPanel {

	private MainFrame frame;

	public SelectPanel(MainFrame frame) {
		this.frame = frame;

		setLayout(new BorderLayout());
		setBackground(Color.BLACK);
		setOpaque(true);

		JPanel wrapper = new JPanel();
		wrapper.setOpaque(false);
		wrapper.setLayout(new BorderLayout());

		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titlePanel.setOpaque(false);
		JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
		backPanel.setOpaque(false);

		JLabel title = new JLabel("Select Subject");
		title.setFont(new Font("Arial", Font.BOLD, 45));
		title.setForeground(Color.WHITE);

		titlePanel.add(title);

		RoundComponent<JButton> back = new RoundComponent<>(JButton.class, new Dimension(120, 30), Color.BLACK,
				new Color(255, 150, 138, 0), "BACK", new Color(152, 255, 153), new Font("Arial", Font.BOLD, 25), 20);
		back.getInner().addActionListener(e -> frame.showPanel("menu"));

		backPanel.add(back);

		wrapper.add(titlePanel, BorderLayout.CENTER);
		wrapper.add(backPanel, BorderLayout.WEST);
		wrapper.add(Box.createRigidArea(new Dimension(back.getPreferredSize())), BorderLayout.EAST);

		add(wrapper, BorderLayout.NORTH);

		RoundComponent<Carousel> center = new RoundComponent<>(Carousel.class, Color.BLACK, Color.GRAY, 20);
		center.getInner().setAlignmentX(CENTER_ALIGNMENT);
		// center.getInner().setLayout(new GridBagLayout());

		// GridBagConstraints c = new GridBagConstraints();

		RoundComponent<JButton> math1 = new RoundComponent<>(JButton.class, new Dimension(250, 250), Color.BLACK,
				new Color(255, 150, 138), "MATH I", new Color(152, 255, 153), new Font("Arial", Font.BOLD, 35), 500);
		math1.getInner().addActionListener(e -> frame.showDifficulty("math1"));

		RoundComponent<JButton> math2 = new RoundComponent<>(JButton.class, new Dimension(250, 250), Color.BLACK,
				new Color(255, 150, 138), "MATH II", new Color(85, 203, 205), new Font("Arial", Font.BOLD, 35), 500);
		math2.getInner().addActionListener(e -> frame.showDifficulty("math2"));

		RoundComponent<JButton> probability = new RoundComponent<>(JButton.class, new Dimension(250, 250), Color.BLACK,
				new Color(255, 150, 138), "Prob & Stats", new Color(246, 234, 194), new Font("Arial", Font.BOLD, 35),
				500);
		probability.getInner().addActionListener(e -> frame.showDifficulty("probability"));

		RoundComponent<JButton> calculus = new RoundComponent<>(JButton.class, new Dimension(250, 250), Color.BLACK,
				new Color(255, 150, 138), "Calculus", new Color(255, 15, 13), new Font("Arial", Font.BOLD, 35), 500);
		calculus.getInner().addActionListener(e -> frame.showDifficulty("calculus"));

		RoundComponent<JButton> geometry = new RoundComponent<>(JButton.class, new Dimension(250, 250), Color.BLACK,
				new Color(255, 150, 138), "Geometry", new Color(255, 15, 13), new Font("Arial", Font.BOLD, 35), 500);
		geometry.getInner().addActionListener(e -> frame.showDifficulty("geometry"));

		/*
		 * c.gridx = 0; c.gridy = 0; c.insets = new Insets(30, 30, 30, 30);
		 * 
		 * center.getInner().add(math1, c);
		 * 
		 * c.gridx = 0; c.gridy = 1; center.getInner().add(math2, c);
		 * 
		 * c.gridx = 0; c.gridy = 2; center.getInner().add(probability, c);
		 * 
		 * c.gridx = 0; c.gridy = 3; center.getInner().add(calculus, c);
		 * 
		 * c.gridx = 0; c.gridy = 4; center.getInner().add(geometry, c);
		 */
		
		center.getInner().addCard(math1);
		center.getInner().addCard(math2);
		center.getInner().addCard(probability);
		center.getInner().addCard(calculus);
		center.getInner().addCard(geometry);
		
		center.getInner().initializeStartPosition();
		
		add(Box.createRigidArea(new Dimension(100, 100)), BorderLayout.EAST);
		add(Box.createRigidArea(new Dimension(100, 100)), BorderLayout.WEST);
		add(Box.createRigidArea(new Dimension(100, 70)), BorderLayout.SOUTH);

		add(center, BorderLayout.CENTER);
	}

}
