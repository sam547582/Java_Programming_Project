package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import util.*;
import java.awt.event.*;

public class Carousel extends JPanel {

	private JScrollPane scrollPane;
	private JPanel cardPanel;

	private int currentIndex = 0;
	
	RoundComponent<JButton> btnLeft;
	RoundComponent<JButton> btnRight;

	
	public Carousel() {
		setLayout(new BorderLayout());
		setOpaque(false);
		
		// 왼쪽 버튼
		btnLeft = new RoundComponent<>(JButton.class, new Dimension(70, 50), new Color(0, 0, 0, 0),
				new Color(0, 0, 0, 0), "<", new Color(152, 255, 153), new Font("Arial", Font.BOLD, 35), 1);
		btnLeft.getInner().addActionListener(e -> moveIndex(-1));

		// 오른쪽 버튼
		btnRight = new RoundComponent<>(JButton.class, new Dimension(70, 50), new Color(0, 0, 0, 0),
				new Color(0, 0, 0, 0), ">", new Color(152, 255, 153), new Font("Arial", Font.BOLD, 35), 1);
		btnRight.getInner().addActionListener(e -> moveIndex(1));

		// 카드 패널
		cardPanel = new JPanel(new GridBagLayout());
		cardPanel.setOpaque(false);

		scrollPane = new JScrollPane(cardPanel);
		scrollPane.setOpaque(false);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(30);

		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(null);

		RoundComponent<JPanel> wrapper = new RoundComponent<>(JPanel.class, new Color(0,0,0,0), Color.WHITE, 20);
		wrapper.getInner().setLayout(new BorderLayout());
		wrapper.getInner().add(scrollPane, BorderLayout.CENTER);
		
		wrapper.add(Box.createRigidArea(new Dimension(1, 1)), BorderLayout.EAST);
		wrapper.add(Box.createRigidArea(new Dimension(1, 1)), BorderLayout.WEST);
		add(btnLeft, BorderLayout.WEST);
		add(Box.createRigidArea(new Dimension(1, 20)), BorderLayout.SOUTH);
		add(Box.createRigidArea(new Dimension(1, 20)), BorderLayout.NORTH);
		add(wrapper, BorderLayout.CENTER);
		add(btnRight, BorderLayout.EAST);
	}

	public void initializeStartPosition() {
		SwingUtilities.invokeLater(() -> {
			scrollToIndex(0); // 첫 카드 중앙으로 이동
		});
	}

	public void addCard(Component card) {
		GridBagConstraints c = new GridBagConstraints();

		int index = cardPanel.getComponentCount();

		c.gridx = index;
		c.gridy = 0;
		if (index == 0)
			c.insets = new Insets(0, 150, 0, 25);
		else if (index == 4)
			c.insets = new Insets(0, 25, 0, 150);
		else
			c.insets = new Insets(0, 25, 0, 25);

		cardPanel.add(card, c);

		cardPanel.revalidate();
		cardPanel.repaint();
	}

	private int cardCount() {
		return cardPanel.getComponentCount();
	}

	private void moveIndex(int dir) {
		int total = cardCount();
		if (total == 0)
			return;

		currentIndex += dir;

		if (currentIndex < 0)
			currentIndex = 0;
		if (currentIndex >= total)
			currentIndex = total - 1;

		scrollToIndex(currentIndex);
	}

	public void scrollToIndex(int index) {
		if (index < 0 || index >= cardPanel.getComponentCount())
			return;

		// 스크롤을 적용하기 전에 레이아웃 계산이 끝났음을 보장
		cardPanel.doLayout();

		Component card = cardPanel.getComponent(index);
		Rectangle r = card.getBounds(); // 카드의 위치 정보

		JViewport vp = scrollPane.getViewport();
		int viewportW = vp.getExtentSize().width;

		// 카드의 중앙 X 좌표
		int targetX = r.x + r.width / 2 - viewportW / 2;

		// 경계 보정
		int maxX = scrollPane.getHorizontalScrollBar().getMaximum();
		if (targetX < 0)
			targetX = 0;
		if (targetX > maxX)
			targetX = maxX;

		vp.setViewPosition(new Point(targetX, 0));
	}

}
