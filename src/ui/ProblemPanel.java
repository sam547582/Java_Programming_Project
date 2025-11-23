package ui;

import java.util.List;
import java.io.*;
import java.net.URL;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import model.*;

public class ProblemPanel extends JPanel {
	
	private BufferedImage img;
	private BufferedImage scaled_img;
	
	private JPanel topWrapper;
	private JPanel top;
	private JPanel center;
	private JPanel bottom;
	
	private JButton[] problemNumberButton;
	private JLabel problemContentLabel;
	private JTextField answerField;
	private JButton submit;
	
	private Problem[] problems;
	
	private String difficulty = "easy";
	private int size;
	private int now_number;
	
	ProblemPanel(MainFrame frame,String difficulty) {
		this.difficulty = difficulty;
		
		setLayout(new BorderLayout());
		setBackground(new Color(30, 40, 60));
		
		getProblem();
		
		getImage(0);
		scaled_img = scaleImage(img, 500);
		
		createProblemNumberButton();
		
		createProblemContentLabel();
		
		createAnswerPanel();
		
		createJPanel();
		
		add(topWrapper, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
		
	}
	
	private void createProblemNumberButton() {
		problemNumberButton = new JButton[size];
		for(int i=0;i<size;i++) {
			int num = i;
			
			problemNumberButton[i] = new JButton(String.valueOf(i+1));
			problemNumberButton[i].setFont(new Font("Arial", Font.BOLD, 28));
			problemNumberButton[i].addActionListener(e -> {getImage(num); updateImage();});
		}
	}
	
	private void createProblemContentLabel() {
		problemContentLabel = new JLabel();
		problemContentLabel.setFont(new Font("Arial", Font.BOLD, 28));
		problemContentLabel.setIcon(new ImageIcon(scaled_img));
		problemContentLabel.setBounds(0, 0, scaled_img.getWidth(), scaled_img.getHeight());
		problemContentLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	private void createJPanel() {
		top = new JPanel();
		top.setLayout(new BoxLayout(top,BoxLayout.X_AXIS));
		top.setOpaque(false);
		
		top.add(Box.createHorizontalGlue());
		for(int i=0;i<size;i++) {
			top.add(problemNumberButton[i]);
			top.add(Box.createHorizontalStrut(20));
		}
		top.add(Box.createHorizontalGlue());
		
		topWrapper = new JPanel(new BorderLayout());
		topWrapper.setOpaque(false);
		topWrapper.add(top,BorderLayout.CENTER);
		
		center = new JPanel(new BorderLayout());
		center.setOpaque(false);
		center.setBackground(Color.WHITE);
		center.add(problemContentLabel,BorderLayout.WEST);
		
		bottom = new JPanel(new FlowLayout());
		bottom.setOpaque(false);
		bottom.add(answerField);
		bottom.add(submit);
		bottom.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
	}
	
	private void createAnswerPanel() {
		answerField = new JTextField(20);
		answerField.setFont(new Font("Arial", Font.PLAIN, 20));
		
		submit = new JButton("Submit");
		submit.setFont(new Font("Arial", Font.BOLD, 18));
		submit.addActionListener(e -> checkAnswer(problems[now_number]));	
	}
	
	private void checkAnswer(Problem nowProblem) {
		String userInput = answerField.getText().trim();
		
        if (userInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Write Answer");
            return;
        }

        if (userInput.equals(nowProblem.getAnswer())) {
            JOptionPane.showMessageDialog(this, "Correct!");
        } else {
            JOptionPane.showMessageDialog(this, "Miscorrect");
        }
    }
	
	private void getProblem() {
		if(difficulty.equals("easy")) {
			List<Problem> all = ProblemManager.loadProblems("img/easy");
			
			size = Math.min(all.size(), 20);
			
			problems = ProblemManager.pickRandom(all,size);
		}
		else if(difficulty.equals("normal")) {
			List<Problem> all = ProblemManager.loadProblems("img/normal");
			
			size = Math.min(all.size(), 20);
			
			problems = ProblemManager.pickRandom(all,size);
		}
		else if(difficulty.equals("hard")) {
			List<Problem> all = ProblemManager.loadProblems("img/hard");
			
			size = Math.min(all.size(), 10);
			
			problems = ProblemManager.pickRandom(all,size);
		}
		else if(difficulty.equals("extreme")) {
			List<Problem> all = ProblemManager.loadProblems("img/extreme");
			
			size = Math.min(all.size(), 5);
			
			problems = ProblemManager.pickRandom(all,size);
		}
	}
	
	private void getImage(int num) {
		BufferedImage original = null;
		
		now_number = num;
		
		try {
			URL path = getClass().getClassLoader().getResource(problems[num].getPath());
			if (path == null) {
			    System.out.println("Can't find image" + problems[num].getPath());
			}
			original = ImageIO.read(path);
		}
		catch(IOException e) {
		    JOptionPane.showMessageDialog(this,"Error" + e.getMessage());
		}

		
		img = removeBackground(original, 240); 
	}
	
	private BufferedImage scaleImage(BufferedImage input, int newWidth) {
	    int orgWidth = input.getWidth();
	    int orgHeight = input.getHeight();

	    double ratio = (double)newWidth / orgWidth;
	    int newHeight = (int)(orgHeight * ratio);

	    Image tmp = input.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

	    BufferedImage scaled = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = scaled.createGraphics();
	    g2.drawImage(tmp, 0, 0, null);
	    g2.dispose();

	    return scaled;
	}
	
	private void updateImage() {
		scaled_img = scaleImage(img, 500);
		problemContentLabel.setIcon(new ImageIcon(scaled_img));
		problemContentLabel.setIcon(new ImageIcon(scaled_img));
	}
	
    private BufferedImage removeBackground(BufferedImage img, int threshold) {
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        
        Color panelBg = new Color(255, 255, 255);
        Color textColor = getContrastColor(panelBg);
        
        int tr = textColor.getRed();
        int tg = textColor.getGreen();
        int tb = textColor.getBlue();
        int textRGB = (0xFF << 24) | (tr << 16) | (tg << 8) | tb;
        
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                int rgb = img.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                
                int brightness = (r + g + b) / 3;

                if (brightness >= threshold) {
                    output.setRGB(x, y, 0x00000000);
                }
                else {
                    output.setRGB(x, y, textRGB);
                }
            }
        }

        return output;
    }
    
    private Color getContrastColor(Color bg) {
        double brightness = bg.getRed() * 0.299
                          + bg.getGreen() * 0.587
                          + bg.getBlue() * 0.114;

        return brightness > 128 ? Color.BLACK : Color.WHITE;
    }

	
	
}
