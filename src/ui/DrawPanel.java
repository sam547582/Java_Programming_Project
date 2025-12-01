package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

import util.*;

public class DrawPanel extends JPanel {
	
	private enum Mode { PEN, LINE, ERASER};
	private Mode mode = Mode.PEN;
	
	private Stack<BufferedImage> undo = new Stack<>();
	private Stack<BufferedImage> redo = new Stack<>();
	
    private BufferedImage canvas;
    private Graphics2D g2;
    private Color backColor;
    private Color penColor;
    
    private JLabel label;
    private JButton penBtn;
    private JButton lineBtn;
    private JButton eraserBtn;
    private JButton colorBtn;
    private JButton undoBtn;
    private JButton redoBtn;
    
    private int startX, startY;
    private int currentX, currentY;
    int lastX, lastY;

    private boolean isPreviewingLine = false;
    
    DrawPanel() {
    	backColor = Color.WHITE;
    	penColor = Color.BLACK;
    	
    	setLayout(new BorderLayout());
        setOpaque(false);
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        
        label = new JLabel("Draw Mode");
        label.setOpaque(false);
        label.setFont(new Font("Arial",Font.BOLD,30));
        label.setForeground(ColorUtils.getContrastColor(backColor));
        
        penBtn = new JButton("PEN");
        lineBtn = new JButton("LINE");
        eraserBtn = new JButton("ERASER");
        colorBtn = new JButton("COLOR");
        undoBtn = new JButton("UNDO");
        redoBtn = new JButton("REDO");
        
        penBtn.addActionListener(e -> mode = Mode.PEN);
        lineBtn.addActionListener(e -> mode = Mode.LINE);
        eraserBtn.addActionListener(e -> mode = Mode.ERASER);
        colorBtn.addActionListener(e -> {
        	Color newColor = JColorChooser.showDialog(this, "Choose Color", penColor);
        	if(newColor != null) {
        		penColor = newColor;
        		g2.setColor(penColor);
        	}
        });
        undoBtn.addActionListener(e -> Undo());
        redoBtn.addActionListener(e -> Redo());
        
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(true);
        toolBar.setOpaque(false);
        toolBar.add(penBtn);
        toolBar.addSeparator();
        toolBar.add(lineBtn);
        toolBar.addSeparator();
        toolBar.add(eraserBtn);
        toolBar.addSeparator();
        toolBar.add(colorBtn);
        toolBar.addSeparator();
        toolBar.add(undoBtn);
        toolBar.addSeparator();
        toolBar.add(redoBtn);
        
        JPanel toolWrapper = new JPanel(new FlowLayout());
        toolWrapper.setOpaque(false);
        toolWrapper.add(toolBar);
        
        add(label, BorderLayout.NORTH);
        add(toolWrapper, BorderLayout.SOUTH);
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (canvas == null || canvas.getWidth() != getWidth() || canvas.getHeight() != getHeight()) {
            initCanvas(backColor);
        }

        g.drawImage(canvas, 0, 0, null);
        
        if (mode == Mode.LINE && isPreviewingLine) {
        	Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(g2.getColor());
            g2d.setStroke(new BasicStroke(2));
            g2d.drawLine(startX, startY, currentX, currentY);
            g2d.dispose();
        }
    }

    /** center 전체 크기만큼 캔버스 생성 */
    public void initCanvas(Color color) {
    	
    	if (canvas != null) return;
    	
    	backColor = color;
    	
        int W = getWidth();
        int H = getHeight();

        if (W <= 0 || H <= 0) return;

        canvas = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);

        g2 = canvas.createGraphics();
        g2.setStroke(new BasicStroke(2));
        g2.setColor(ColorUtils.getContrastColor(color));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    public void Undo() {
    	if(undo.isEmpty()) return;
    	
    	redo.push(copyImage(canvas));
    	
    	canvas = undo.pop();
    	
    	 g2 = canvas.createGraphics();
         g2.setStroke(new BasicStroke(2));
         g2.setColor(ColorUtils.getContrastColor(backColor));
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    public void Redo() {
    	if(redo.isEmpty()) return;
    	
    	undo.push(copyImage(canvas));
    	
    	canvas = redo.pop();
    	
    	 g2 = canvas.createGraphics();
         g2.setStroke(new BasicStroke(2));
         g2.setColor(ColorUtils.getContrastColor(backColor));
         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    
    // ChatGpt 참고
    private BufferedImage copyImage(BufferedImage src) {
        BufferedImage copy = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        Graphics2D g = copy.createGraphics();
        g.drawImage(src, 0, 0, null);
        g.dispose();
        return copy;
    }

    
    private MouseAdapter mouseAdapter = new MouseAdapter() {

        @Override
        public void mousePressed(MouseEvent e) {
            if (!isVisible()) return; 
            
            if (mode == Mode.PEN || mode == Mode.ERASER) { 
            	undo.push(copyImage(canvas));
            	redo.clear();
            	
	            lastX = e.getX();
	            lastY = e.getY();
            }
            else if (mode == Mode.LINE) {
            	startX = e.getX();
            	startY = e.getY();
            	
            	currentX = startX;
            	currentY = startY;
            	
            	isPreviewingLine = true;
            }
            
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!isVisible()) return;
            
            if (mode == Mode.PEN) { 
	            int x = e.getX();
	            int y = e.getY();
	
	            g2.drawLine(lastX, lastY, x, y);
	
	            lastX = x;
	            lastY = y;
	
	            repaint();
            }
            else if (mode == Mode.LINE) {
            	currentX = e.getX();
            	currentY = e.getY();
            	repaint();
            }
            else if (mode == Mode.ERASER) {
            	int size = 12;
            	int x = e.getX();
	            int y = e.getY();
	            
	            // ChatGpt 참고
	            for (int dx = -size; dx <= size; dx++) {
	                for (int dy = -size; dy <= size; dy++) {

	                    int px = x + dx;
	                    int py = y + dy;

	                    if (dx*dx + dy*dy <= size*size) { 
	                        if (px >= 0 && py >= 0 && px < canvas.getWidth() && py < canvas.getHeight()) {
	                            canvas.setRGB(px, py, 0x00000000); 
	                        }
	                    }
	                }
	            }
	            
	            lastX = x;
	            lastY = y;
	
	            repaint();
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
        	if (mode == Mode.LINE && isPreviewingLine) {
        		undo.push(copyImage(canvas));
        		redo.clear();
        		
        		int x = e.getX();
        		int y = e.getY();
        		
        		g2.drawLine(startX, startY, x, y);
        		
        		isPreviewingLine = false;
        		repaint();
        	}
        }
    };
    
    public void updateColor(Color color) {

        if (canvas == null) initCanvas(backColor);
        
        if (color.equals(backColor)) {
            return;
        }
		
        backColor = color;
        penColor = ColorUtils.getContrastColor(color);
        
        label.setForeground(ColorUtils.getContrastColor(backColor));
  

        canvas = ImageUtils.invertColors(canvas, penColor);
        
        Stack<BufferedImage> bf = new Stack<>();
        while(!undo.isEmpty()) {
        	BufferedImage img = undo.pop();
            img = ImageUtils.invertColors(img, penColor);
            bf.push(img);
            
        }
        while(!bf.isEmpty()) {
        	BufferedImage img = bf.pop();
            img = ImageUtils.invertColors(img, penColor);
            undo.push(img);
        }
        
        while(!redo.isEmpty()) {
        	BufferedImage img = redo.pop();
            img = ImageUtils.invertColors(img, penColor);
            bf.push(img);
            
        }
        while(!bf.isEmpty()) {
        	BufferedImage img = bf.pop();
            img = ImageUtils.invertColors(img, penColor);
            redo.push(img);
        }
        

        g2 = canvas.createGraphics();
        g2.setStroke(new BasicStroke(3));
        g2.setColor(penColor);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        repaint();
    }

    public void clear() {
        initCanvas(backColor);
        repaint();
    }
}
