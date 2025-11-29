package ui;

import javax.swing.*;

import ui.component.*;

import java.awt.*;
import java.awt.event.*;
import util.*;

public class StartPanel extends JPanel {
	
	private JPanel wrapperName;
	private JPanel wrapperGrade;
	private JPanel wrapperElective;
		
	StartPanel(MainFrame frame) {
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);
		
		FadeLabel labelName = new FadeLabel("Enter your name");
		FadeTextField fieldName = new FadeTextField(500, 50);
		
		wrapperName = makeFieldSet(labelName, fieldName);
		
		FadeLabel labelGrade = new FadeLabel("Choose your target grade");
		FadeButton[] btn = new FadeButton[5];
		
        btn[0] = new FadeButton("1",100,100);
        btn[0].setFont(new Font("Arial",Font.BOLD,35));
        btn[1] = new FadeButton("2",100,100);
        btn[1].setFont(new Font("Arial",Font.BOLD,35));
        btn[2] = new FadeButton("3",100,100);
        btn[2].setFont(new Font("Arial",Font.BOLD,35));
        btn[3] = new FadeButton("4",100,100); 
        btn[3].setFont(new Font("Arial",Font.BOLD,35));
        btn[4] = new FadeButton("5",100,100);
        btn[4].setFont(new Font("Arial",Font.BOLD,35));
        
        wrapperGrade = makeButtonSet(labelGrade, btn);
        
        FadeLabel labelElective = new FadeLabel("Elective Subject");
        FadeButton[] elec = new FadeButton[3];
        
        elec[0] = new FadeButton("Probability and Statistics",450,50);
        elec[0].setFont(new Font("Arial",Font.PLAIN,35));
        elec[1] = new FadeButton("Calculus",200,50);
        elec[1].setFont(new Font("Arial",Font.PLAIN,35));
        elec[2] = new FadeButton("Geometry",200,50);
        elec[2].setFont(new Font("Arial",Font.PLAIN,35));
        
        wrapperElective = makeElectiveSet(labelElective, elec);
        
        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.add(wrapperName);
		
        add(center,BorderLayout.CENTER);
        
		labelName.fadeIn(500);
		fieldName.fadeIn(500);
		
		fieldName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                	
                	StatsManager.load();
                	StatsManager.updateStats(fieldName.getText());
                	
                	fieldName.fadeOut(500, () -> {

                	    center.remove(wrapperName);
                	    center.add(wrapperElective);
                	    center.revalidate();
                	    center.repaint();
                	    
                	});
                	
                	labelName.fadeOut(500, null);	    
            	    
                	new Timer(500, evt -> {
                        labelElective.fadeIn(500);
                        
                        new Timer(700, evt2 -> {
                        	int delay = 0;

                            for (FadeButton b : elec) {

                                int d = delay;

                                new Timer(d, evt3 -> {
                                    b.fadeIn(200);
                                }) {{
                                    setRepeats(false);
                                    start();
                                }};

                                delay += 200; 
                            }
                        	
                        }) {{
                            setRepeats(false);
                            start();
                        }}; 
                        
                    }) {{
                        setRepeats(false);
                        start();
                    }}; 

                }
            }
		});
		
		for(FadeButton b : elec) {
			b.addActionListener(e -> {
										
				labelElective.fadeOut(100, () -> {

            	    center.remove(wrapperElective);
            	    center.add(wrapperGrade);
            	    center.revalidate();
            	    center.repaint();
            	    
            	});		
				for(FadeButton b2 : elec) {
					b2.fadeOut(100, null);
				}
				
				
				new Timer(100, evt -> {
                    labelGrade.fadeIn(500);
                    
                    new Timer(700, evt2 -> {
                    	int delay = 0;

                        for (FadeButton b3 : btn) {

                            int d = delay;

                            new Timer(d, evt3 -> {
                                b3.fadeIn(200);
                            }) {{
                                setRepeats(false);
                                start();
                            }};

                            delay += 200; 
                        }
                    	
                    }) {{
                        setRepeats(false);
                        start();
                    }}; 
                    
                }) {{
                    setRepeats(false);
                    start();
                }};
			});
		}
		
		for(FadeButton b : btn) {
			b.addActionListener(e -> {
				labelGrade.fadeOut(100, () -> {

            	    center.remove(wrapperGrade);
            	    center.revalidate();
            	    center.repaint();
            	    
            	});		
				for(FadeButton b2 :btn) {
					b2.fadeOut(100, null);
				}
				
				new Timer(300, evt -> {
					frame.showMenu();
				}) {{
                    setRepeats(false);
                    start();
                }};
				
			});
		}
		
		
	}
		/*
		new Timer(900, evt -> {
            frame.showMenu();
        }) {{
            setRepeats(false);
            start();
        }}; */
	
	
	private JPanel makeFieldSet(FadeLabel label, FadeTextField field) {
	    JPanel w = new JPanel();
	    w.setOpaque(false);
	    w.setLayout(new BoxLayout(w, BoxLayout.Y_AXIS));
	    
	    label.setFontColor(ColorUtils.getContrastColor(getBackground()));
	    label.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    field.setBorderColor(ColorUtils.getContrastColor(getBackground()));
		field.setFontColor(ColorUtils.getContrastColor(getBackground()));
		field.setBackColor(getBackground());
	    field.setAlignmentX(Component.CENTER_ALIGNMENT);

	    w.add(label);
	    w.add(Box.createVerticalStrut(20));
	    w.add(field);
	    
	    w.setSize(w.getPreferredSize());
	    return w;
	}
	
	private JPanel makeButtonSet(FadeLabel label, FadeButton[] btn) {
	    JPanel w = new JPanel();
	    w.setOpaque(false);
	    w.setLayout(new BoxLayout(w, BoxLayout.Y_AXIS));
	    
	    label.setFontColor(ColorUtils.getContrastColor(getBackground()));
	    label.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    w.add(label);
	    w.add(Box.createVerticalStrut(20));
	    
	    JPanel btnWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER,20,0));
	    btnWrapper.setOpaque(false);
	    
	    for(FadeButton b : btn) {
		    b.setBorderColor(ColorUtils.getContrastColor(getBackground()));
			b.setFontColor(ColorUtils.getContrastColor(getBackground()));
			b.setButtonColor(getBackground());
		    b.setAlignmentX(Component.CENTER_ALIGNMENT);
		    
		    btnWrapper.add(b);
	    }
	    
	    w.add(btnWrapper);
	    
	    w.setSize(w.getPreferredSize());
	    return w;
	}
	
	private JPanel makeElectiveSet(FadeLabel label, FadeButton[] btn) {

	    JPanel w = new JPanel();
	    w.setOpaque(false);
	    w.setLayout(new GridBagLayout());

	    GridBagConstraints c = new GridBagConstraints();
	    c.insets = new Insets(10, 10, 10, 10);  // 여백
	    c.anchor = GridBagConstraints.CENTER;
	    c.fill = GridBagConstraints.NONE;

	    // --- LEFT : LABEL (가운데 정렬) ---
	    c.gridx = 0;
	    c.gridy = 1;     // 가운데 라인에 위치
	    c.weightx = 0;
	    c.weighty = 1;
	    c.anchor = GridBagConstraints.CENTER;

	    label.setFontColor(ColorUtils.getContrastColor(getBackground()));
	    w.add(label, c);


	    // --- RIGHT : BUTTONS ---
	    c.gridx = 1;
	    c.insets = new Insets(40,0,40,0);
	    c.weightx = 1;
	    c.anchor = GridBagConstraints.CENTER;
	    c.weighty = 0;

	    for (int i = 0; i < btn.length; i++) {
	        c.gridy = i;     // 0 / 1 / 2
	        FadeButton b = btn[i];

	        b.setBorderColor(ColorUtils.getContrastColor(getBackground()));
	        b.setFontColor(ColorUtils.getContrastColor(getBackground()));
	        b.setButtonColor(getBackground());
	        b.setAlignmentX(Component.CENTER_ALIGNMENT);

	        w.add(b, c);
	    }

	    return w;
	}


	

}
