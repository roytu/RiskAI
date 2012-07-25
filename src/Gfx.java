import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;


public class Gfx extends JFrame{
	
	private static final int SCREEN_WIDTH = 1200;
	private static final int SCREEN_HEIGHT = 582;
	
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
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Gfx frame = new Gfx();
		frame.setVisible(true);
	}
	

}
