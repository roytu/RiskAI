import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;


public class Gfx extends JFrame{
	Graphics2D g2;
	public Gfx()
	{
		setSize(400,400);
		setVisible(true);
	}

	/**
	 * @param args
	 */

	public void paint(Graphics g)
	{
		g2 = (Graphics2D) g;
		g2.draw(new Rectangle2D.Double(5,5,60,70));
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Gfx frame = new Gfx();
		frame.setVisible(true);
	}
	

}
