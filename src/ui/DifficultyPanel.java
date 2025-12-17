package ui;

import javax.swing.*;

import util.RoundComponent;

import java.awt.*;

public class DifficultyPanel extends JPanel {

	private MainFrame frame;
	private String subject;

	public DifficultyPanel(MainFrame frame, String subject) {
		this.frame = frame;
		this.subject = subject;
		
		frame.setResizable(false);

		setLayout(new BorderLayout());
		setBackground(new Color(28, 28, 30));
		setOpaque(true);

		JPanel wrapper = new JPanel();
		wrapper.setOpaque(false);
		wrapper.setLayout(new BorderLayout());

		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		titlePanel.setOpaque(false);
		JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
		backPanel.setOpaque(false);

		JLabel title = new JLabel("Select Difficulty");
		title.setFont(new Font("Arial", Font.BOLD, 45));
		title.setForeground(new Color(200, 210, 220));

		titlePanel.add(title);

		RoundComponent<JButton> back = new RoundComponent<>(JButton.class, new Dimension(120, 30),
				new Color(0, 0, 0, 0), new Color(60, 70, 78), "BACK", new Color(220, 225, 230),
				new Font("Arial", Font.BOLD, 25), 20);
		back.getInner().addActionListener(e -> frame.showSubject());
		back.setHoverBackground(new Color(75, 90, 120));
		back.setHoverForeground(new Color(245, 248, 252));
		backPanel.add(back);

		wrapper.add(titlePanel, BorderLayout.CENTER);
		wrapper.add(backPanel, BorderLayout.WEST);
		wrapper.add(Box.createRigidArea(new Dimension(backPanel.getPreferredSize())), BorderLayout.EAST);

		add(wrapper, BorderLayout.NORTH);

		RoundComponent<JPanel> center = new RoundComponent<>(JPanel.class, new Color(0, 0, 0, 0),
				new Color(240, 242, 245, 200), 20);
		center.getInner().setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		RoundComponent<JButton> easy = new RoundComponent<>(JButton.class, new Dimension(250, 70),
				new Color(0, 0, 0, 0), new Color(230, 240, 233), "EASY", new Color(46, 94, 78),
				new Font("Arial", Font.BOLD, 35), 20);
		easy.getInner().addActionListener(e -> showCountdownDialog("easy"));
		easy.setHoverBackground(new Color(86, 168, 128));
		easy.setHoverForeground(new Color(245, 248, 246));

		RoundComponent<JButton> normal = new RoundComponent<>(JButton.class, new Dimension(250, 70),
				new Color(0, 0, 0, 0), new Color(232, 237, 245), "NORMAL", new Color(42, 63, 96),
				new Font("Arial", Font.BOLD, 35), 20);
		normal.getInner().addActionListener(e -> showCountdownDialog("normal"));
		normal.setHoverBackground(new Color(90, 130, 200));
		normal.setHoverForeground(new Color(245, 247, 250));

		RoundComponent<JButton> hard = new RoundComponent<>(JButton.class, new Dimension(250, 70),
				new Color(0, 0, 0, 0), new Color(245, 240, 230), "HARD", new Color(110, 82, 50),
				new Font("Arial", Font.BOLD, 35), 20);
		hard.getInner().addActionListener(e -> showCountdownDialog("hard"));
		hard.setHoverBackground(new Color(180, 130, 80));
		hard.setHoverForeground(new Color(255, 250, 240));

		RoundComponent<JButton> extreme = new RoundComponent<>(JButton.class, new Dimension(250, 70),
				new Color(0, 0, 0, 0), new Color(245, 235, 235), "KILLER", new Color(120, 30, 40),
				new Font("Arial", Font.BOLD, 35), 20);
		extreme.getInner().addActionListener(e -> showCountdownDialog("extreme"));
		extreme.setHoverBackground(new Color(180, 50, 60));
		extreme.setHoverForeground(new Color(255, 240, 240));

		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(30, 30, 30, 30);

		center.getInner().add(easy, c);

		c.gridx = 1;
		c.gridy = 0;
		center.getInner().add(normal, c);

		c.gridx = 0;
		c.gridy = 1;
		center.getInner().add(hard, c);

		c.gridx = 1;
		c.gridy = 1;
		center.getInner().add(extreme, c);

		add(Box.createRigidArea(new Dimension(100, 100)), BorderLayout.EAST);
		add(Box.createRigidArea(new Dimension(100, 100)), BorderLayout.WEST);
		add(Box.createRigidArea(new Dimension(100, 70)), BorderLayout.SOUTH);
		add(center, BorderLayout.CENTER);
	}

	private void showCountdownDialog(String difficulty) {

		// 모달 다이얼로그 (부모 Frame 고정)
		JDialog dialog = new JDialog(frame, "Start", true);
		dialog.setSize(300, 150);
		dialog.setLocationRelativeTo(frame); // 화면 가운데
		dialog.setLayout(new BorderLayout());

		JLabel label = new JLabel("Starts in 5 seconds", SwingConstants.CENTER);
		label.setFont(new Font("Arial", Font.BOLD, 20));
		dialog.add(label, BorderLayout.CENTER);

		// 5초 카운트다운 변수 / 배열 선언 원인 체크하기
		int[] time = { 5 };

		// 1초마다 실행되는 Swing Timer
		Timer timer = new Timer(1000, null);

		dialog.addWindowListener(new java.awt.event.WindowAdapter() {

			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				timer.stop();
				// 필요하면 “취소됨” 표시도 가능
				// JOptionPane.showMessageDialog(frame, "취소되었습니다.");
			}
		});

		timer.addActionListener(e -> {

			time[0]--;

			if (time[0] > 0) {
				label.setText("Starts in " + time[0] + " seconds");
			} else {
				// 타이머 종료 + 다이얼로그 종료
				timer.stop();
				dialog.dispose();

				// 화면 전환 실행
				frame.showProblem(difficulty, subject); // 원하는 화면으로 이동
			}
		});

		timer.start();
		dialog.setVisible(true); // 다이얼로그 표시 (모달)
	}

}
