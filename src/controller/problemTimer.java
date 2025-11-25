package controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class problemTimer extends Timer {
	private int remainSeconds;
	private TimeoutListener listener;
	
	public problemTimer() {
		super(1000, null);
		
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

        System.out.println("남은 시간: " + remainSeconds + "초"); 

        if (remainSeconds <= 0) {
            stop();

            listener.Timeout();
        }
    }
	
    public void setTimeoutListener(TimeoutListener listener) {
        this.listener = listener;
    }
	   
	public interface TimeoutListener {
		void Timeout();
	}

}
