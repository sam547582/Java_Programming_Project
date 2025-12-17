package ui.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import util.*;
import java.awt.event.*;

public class Carousel extends JPanel {

	private JScrollPane scrollPane;
	private JPanel cardPanel;

	private Timer animTimer;
	private int animStartX;
	private int animTargetX;
	private long animStartTime;

	private final int ANIM_DURATION = 300;

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

		RoundComponent<JPanel> wrapper = new RoundComponent<>(JPanel.class, new Color(0, 0, 0, 0), Color.WHITE, 20);
		wrapper.getInner().setLayout(new BorderLayout());
		wrapper.getInner().add(scrollPane, BorderLayout.CENTER);

		wrapper.add(Box.createRigidArea(new Dimension(1, 1)), BorderLayout.EAST);
		wrapper.add(Box.createRigidArea(new Dimension(1, 1)), BorderLayout.WEST);

		JPanel[] btnWrapper = new JPanel[2];
		btnWrapper[0] = new JPanel(new GridBagLayout());
		btnWrapper[0].setOpaque(false);
		btnWrapper[0].setPreferredSize(new Dimension(70, 50));
		btnWrapper[0].add(btnLeft);

		btnWrapper[1] = new JPanel(new GridBagLayout());
		btnWrapper[1].setOpaque(false);
		btnWrapper[1].setPreferredSize(new Dimension(70, 50));
		btnWrapper[1].setLayout(new GridBagLayout());
		btnWrapper[1].add(btnRight);

		add(btnWrapper[0], BorderLayout.WEST);
		add(Box.createRigidArea(new Dimension(1, 20)), BorderLayout.SOUTH);
		add(Box.createRigidArea(new Dimension(1, 20)), BorderLayout.NORTH);
		add(wrapper, BorderLayout.CENTER);
		add(btnWrapper[1], BorderLayout.EAST);

		SwingUtilities.invokeLater(() -> {
			updateArrowState();
		});
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

		updateArrowState();
	}

	private void updateArrowState() {
		int total = cardPanel.getComponentCount();
		btnLeft.getInner().setEnabled(currentIndex > 0);
		btnRight.getInner().setEnabled(currentIndex < total - 1);
	}

	public void scrollToIndex(int index) {
		if (index < 0 || index >= cardPanel.getComponentCount())
			return;

		cardPanel.doLayout();

		Component card = cardPanel.getComponent(index);
		Rectangle r = card.getBounds();

		JViewport vp = scrollPane.getViewport();
		int viewportW = vp.getExtentSize().width;

		int targetX = r.x + r.width / 2 - viewportW / 2;

		int maxX = scrollPane.getHorizontalScrollBar().getMaximum();
		targetX = Math.max(0, Math.min(targetX, maxX));

		// ★ 이제 직접 이동이 아니라 "애니메이션 실행"
		animateScrollTo(targetX);
	}

	private void animateScrollTo(int targetX) {
		if (animTimer != null && animTimer.isRunning())
			animTimer.stop();

		JViewport vp = scrollPane.getViewport();
		animStartX = vp.getViewPosition().x;
		animTargetX = targetX;
		animStartTime = System.currentTimeMillis();

		animTimer = new Timer(5, e -> { 
			long elapsed = System.currentTimeMillis() - animStartTime;
			float t = Math.min(1f, (float) elapsed / ANIM_DURATION);

			// 부드럽게: ease-out 효과
			float eased = (float) (1 - Math.pow(1 - t, 3));

			int newX = animStartX + Math.round((animTargetX - animStartX) * eased);

			vp.setViewPosition(new Point(newX, 0));

			if (t >= 1f)
				animTimer.stop();
		});

		animTimer.start();
	}

}
