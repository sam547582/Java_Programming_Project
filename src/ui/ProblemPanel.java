package ui;

import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ProblemPanel extends JPanel {
	
	private BufferedImage img;
	
	private String difficulty = "";
	
	private JLabel problemNumberLabel;
	private JLabel problemContentLabel;
	private JTextField answerField;
	private JButton submit;
	
	private String answer;
	
	ProblemPanel(MainFrame frame) {
		
		setLayout(new BorderLayout());
		setBackground(new Color(30, 40, 60));
		
		answer = "12";
		
		BufferedImage original = null;
		try {
			original = ImageIO.read(new File("resources/img/easy/test3.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		img = removeBackground(original, 240); 
		BufferedImage scaled_img = scaleImage(img, 400);
		
		problemNumberLabel = new JLabel("Problem 1", SwingConstants.CENTER);
		problemNumberLabel.setFont(new Font("Arial", Font.BOLD, 28));
		problemNumberLabel.setForeground(Color.WHITE);;
		
		problemContentLabel = new JLabel();
		problemContentLabel.setFont(new Font("Arial", Font.BOLD, 28));
		problemContentLabel.setIcon(new ImageIcon(scaled_img));
		problemContentLabel.setBounds(0, 0, scaled_img.getWidth(), scaled_img.getHeight());
		problemContentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		answerField = new JTextField(20);
		answerField.setFont(new Font("Arial", Font.PLAIN, 20));
		
		submit = new JButton("Submit");
		submit.setFont(new Font("Arial", Font.BOLD, 18));
		submit.addActionListener(e -> checkAnswer());	
		
		JPanel top = new JPanel(new BorderLayout());
		top.setOpaque(false);
		top.add(problemNumberLabel,BorderLayout.CENTER);
		top.setBorder(BorderFactory.createEmptyBorder(20, 0, 20 , 0));
		
		JPanel center = new JPanel(new BorderLayout());
		center.setOpaque(true);
		center.add(problemContentLabel,BorderLayout.WEST);
		
		JPanel bottom = new JPanel(new FlowLayout());
		bottom.setOpaque(false);
		bottom.add(answerField);
		bottom.add(submit);
		bottom.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
		
		add(top, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
		
	}
	
	public void setDifficulty(String difficulty) {
		this.difficulty = difficulty;
		
	}
	
	private void checkAnswer() {
		String userInput = answerField.getText().trim();
		
        if (userInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Write Answer");
            return;
        }

        if (userInput.equals(answer)) {
            JOptionPane.showMessageDialog(this, "Correct!");
        } else {
            JOptionPane.showMessageDialog(this, "Miscorrect");
        }
    }

	
	private static BufferedImage scaleImage(BufferedImage input, int newWidth) {
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
	 	
    private static BufferedImage removeBackground(BufferedImage img, int threshold) {
        int w = img.getWidth();
        int h = img.getHeight();

        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {

                int rgb = img.getRGB(x, y);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                int brightness = (r + g + b) / 3;

                if (brightness >= threshold) {
                    output.setRGB(x, y, 0x00FFFFFF);
                } else {
                    output.setRGB(x, y, (rgb & 0xFFFFFF) | 0xFF000000);
                }
            }
        }

        return output;
    }

	
	
}
