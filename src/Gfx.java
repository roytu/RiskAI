import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class Gfx extends JFrame{
	Graphics2D g2;
	BufferedImage backgroundImage;
	public Gfx()
	{
		setSize(1080,700);
		
		backgroundImage=getBackgroundImage();
		//this.getContentPane().add(test);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
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
	

}
