import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JFrame;

public class StevenAim
{
	
	private BufferedImage bi;
	private Graphics2D g;
	private Graphics2D g2;
	private Robot r;
	private final int width, height;
	
	public StevenAim() {
		width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	
	class AimClass
	{
	public int x = 6;	
		public AimClass(){}
		
	}
	
	
	private Cursor makeCustomCursor(boolean hide)
	{
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		BufferedImage cursorImage = new BufferedImage(16,16,2);
		Graphics2D drawCursor = cursorImage.createGraphics();
		drawCursor.setPaint(Color.YELLOW);
		int yellow = (255 << 24) | (255 << 16) | (255 << 8 ) | (0);
		int purple = (255 << 24) | (255 << 16) | ( 0 << 8) | 255;
		int transparentGreen = (0 << 24) | (0 << 16) | (255 << 8) | 0;
		if (hide == false)
		for (int r = 0; r < 16; r++)
		{
			for (int c = 0; c < 16; c++)
			{
				if (Math.sqrt(Math.pow(r-7,2) + Math.pow(c-8,2)) <= 5
						&&
						Math.sqrt(Math.pow(r-7,2) + Math.pow(c-8,2)) >= 4)
				{
					cursorImage.setRGB(c,r,purple);
				}
				else if (r == 7 && c == 8)
					cursorImage.setRGB(c,r,yellow);
				else
					cursorImage.setRGB(c,r,transparentGreen);
			}
		}
		drawCursor.drawImage(cursorImage,null,0,0);
		Point cursorHotSpot = new Point(7,7);
		Cursor customCursor = toolkit.createCustomCursor(cursorImage, cursorHotSpot, "Cursor");
		return customCursor;
	}
	class CircleChild {
		public int x, y;
		public long spawnTime;
		private boolean isDead;
		public CircleChild(int spawnX, int spawnY) {
			this.x = spawnX; this.y = spawnY;
			isDead = false;
			spawnTime = System.currentTimeMillis();
		}
		
		
		
		final int duration = 450;
		//pass the bufferedImage graphics object
		public void draw(Graphics2D g) {
			int remainderTime = (int)(System.currentTimeMillis() - spawnTime);
			System.out.println("remaindertime is " + remainderTime);
			if (remainderTime > duration) { remainderTime = duration; isDead = true;};
			int alpha = 255 - (int)(255*(remainderTime / 450f));
			System.out.println("alpha is " + alpha);
			g.setPaint(new Color(255,255,0,alpha));
			g.fillOval(x, y, 12, 12);
		}
		public boolean isDead() {
			return isDead;
		}
		
	}
	
	
	public transient ArrayList<CircleChild> circleChildren;
	public void begin()
	{
		try{
		r = new Robot();
		
		JFrame f = new JFrame();
		f.setSize(new Dimension(width-100,height-100));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		Cursor cursor = makeCustomCursor(true);
		f.setCursor(cursor);
		
		
		circleChildren = new ArrayList<>();
		
		
		final AimClass aim = new AimClass();
		aim.x = 5;
		f.setContentPane(new JApplet(){
			{
				
				System.out.println(aim.x);
				bi = new BufferedImage(width,height, 5);
				g = bi.createGraphics();
				//g2 = (Graphics2D)(this.getGraphics());
			}
			
			public void paint(Graphics g1)
			{
				g2 = (Graphics2D)(g1);

				
				g.setPaint(Color.BLACK);
				g.fillRect(0,0,getSize().width,getSize().height);
				g.setPaint(Color.RED);
				for (int i = 0; i < circleChildren.size(); i++) {
					circleChildren.get(i).draw(g);
					
					if (circleChildren.get(i).isDead()) {
						circleChildren.remove(i); i--; continue;
					}
				}
				g.setPaint(Color.RED);
				g.draw(new Line2D.Double(0,height/2,width,height/2));
				
				
				
				g2.drawImage(bi, null, 0, 0);
			}
		}
		);
		
		f.getContentPane().addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			int moddy = 0;
			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				moddy++;
				if (moddy%1 != 0) return;
				currentMouseX = arg0.getX();
				currentMouseY = arg0.getY();
				
			}
			
		});
		f.setFocusable(true);
		
		f.setVisible(true);
		CursorBoy boy = new CursorBoy(f.getSize().width/2,f.getSize().height/2);
		while(true) { 
			Thread.sleep(50);
			handleMouseUpdates(f,boy);
			
			f.getContentPane().repaint(); }
		
		}catch(Exception e){e.printStackTrace();};
		
	}
	private volatile int currentMouseX, currentMouseY;
	class CursorBoy {
		public int x, y;
		public CursorBoy(int x, int y) {
			this.x=x;this.y=y;
		}
		public void move(int offsetX, int offsetY) {
			
			this.x += offsetX;
			this.y += offsetY;
		}
	}
	private void handleMouseUpdates(JFrame f,CursorBoy boy) {
		int middleX = f.getSize().width/2;
		int middleY = f.getSize().height/2;
		
		//calculate the offset
		boy.move(currentMouseX-middleX,currentMouseY-middleY);
		System.out.println("x=" + boy.x + ",y=" + boy.y);
		
		if (f.hasFocus())
			{
			r.mouseMove(middleX, middleY);
			currentMouseX = middleX;
			currentMouseY = middleY;
			}
		circleChildren.add(new CircleChild(boy.x,boy.y));
	}
	
	public static void main(String[]args)
	{
		StevenAim s = new StevenAim();
		s.begin();
	}
}