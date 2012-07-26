import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.geom.*;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Gfx extends JFrame{
	Graphics2D g2;
	BufferedImage backgroundImage;
	public Gfx()
	{
		setSize(1080,700);
		
		backgroundImage=getBackgroundImage();
		//this.getContentPane().add(test);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Risk!");
		setVisible(true);
		//TerritoryNode.picture = this;
	}

	/**
	 * @param args
	 */
	
	private BufferedImage getBackgroundImage()
	{
		BufferedImage img = null;
		try{
			img = ImageIO.read(new File("BlankRiskMap.jpg"));
		}catch (IOException e){}
		return img;
	}

	public void paint(Graphics g)
	{
		g2 = (Graphics2D) g;
		g2.drawImage(backgroundImage, 10, 40, this);
		//g2.draw(new Rectangle2D.Double(5,5,60,70));		
	}
	/**
	 * Draws a TerritoryNode.
	 * More precisely, draws an oval in the node's owner's color, then puts
	 * the number of armies in text on top of it.
	 */
	/*public void drawTerritoryNode(TerritoryNode node) //RectangularShape is Rectangle2D, Ellipse2D, and 
								//RoundRectangle2D, also Arc2D
	{
		//At the moment this doesn't work because g2 is not initialized.
		//Once that is fixed uncomment this.
//		g2.setColor(node.parent.getOwner().getColor()); //implement player.getColor()
//		g2.drawOval(90,90,TerritoryNode.SIDE_LENGTH,TerritoryNode.SIDE_LENGTH);
			//at the moment there are no coords, these (90 and 90, that is) are placeholders
//		g2.setColor(Color.BLACK);
//		g2.drawString(String.valueOf(node.parent.getUnitCount()), 95, 95);
	}*/
	

}
