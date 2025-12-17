package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class problemTimer extends Timer {
	private int remainSeconds;
	private JLabel label;
	private TimeoutListener listener;

	public problemTimer(JLabel label) {
		super(1000, null);
		this.label = label;

		updateLabel();

		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
	}

	public void setTime(int seconds) {
		remainSeconds = seconds;
	}

	private void tick() {
		remainSeconds--;
		updateLabel();

		if (remainSeconds <= 0) {
			stop();

			listener.Timeout();
		}
	}

	private void updateLabel() {
		int min = remainSeconds / 60;
		int sec = remainSeconds % 60;

		label.setText(String.format("%02d:%02d", min, sec));
	}

	public void setTimeoutListener(TimeoutListener listener) {
		this.listener = listener;
	}

	public interface TimeoutListener {
		void Timeout();
	}

}
