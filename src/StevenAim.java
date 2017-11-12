import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import javax.swing.JApplet;
import javax.swing.JFrame;

public class StevenAim
{
	public StevenAim() { }
	private BufferedImage bi;
	private Graphics2D g;
	private Graphics2D g2;
	private Robot r;
	private final int width = 800, height = 800;
	
	class AimClass
	{
	public int x = 6;	
		public AimClass(){}
		
	}
	
	
	private Cursor makeCustomCursor()
	{
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		BufferedImage cursorImage = new BufferedImage(15,15,5);
		Graphics2D drawCursor = cursorImage.createGraphics();
		drawCursor.setPaint(Color.YELLOW);
		drawCursor.fillRect(0, 0, 15, 15);
		drawCursor.drawImage(cursorImage,null,0,0);
		Point cursorHotSpot = new Point(7,7);
		Cursor customCursor = toolkit.createCustomCursor(cursorImage, cursorHotSpot, "Cursor");
		return customCursor;
	}
	public void begin()
	{
		try{
		r = new Robot();
		
		JFrame f = new JFrame();
		f.setSize(new Dimension(width,height));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		f.setFocusable(true);
		
		Cursor cursor = makeCustomCursor();
		f.setCursor(cursor);
		
		
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

				g.setPaint(Color.RED);
				g.setBackground(Color.BLACK);
				
				g.draw(new Line2D.Double(0,height/2,width,height/2));
				
				
				
				g2.drawImage(bi, null, 0, 0);
			}
		}
		);
		
		
		}catch(Exception e){e.printStackTrace();};
	}
	
	public static void main(String[]args)
	{
		StevenAim s = new StevenAim();
		s.begin();
	}
}