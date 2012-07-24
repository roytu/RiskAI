import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class Gfx extends JFrame{
	
	private static final int SCREEN_WIDTH = 1200;
	private static final int SCREEN_HEIGHT = 582;
	
	Graphics2D g2;
	public Gfx()
	{
		setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		setVisible(true);
	}

	/**
	 * @param args
	 */

	public void paint(Graphics g)
	{
		ImageIcon img = new ImageIcon(this.getClass().getResource("bkg.jpg")).getImage();
		
		g2 = (Graphics2D) g;
		g2.drawImage(new BufferedImage("bkg.jpg"), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Gfx frame = new Gfx();
		frame.setVisible(true);
	}
	

}
