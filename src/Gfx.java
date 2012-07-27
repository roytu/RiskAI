import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


@SuppressWarnings("serial")
public class Gfx extends JFrame{
	//Graphics2D g2;
	BufferedImage backgroundImage;
	//public List<TerritoryGraphics> territoryGraphicsList;
	
	GfxThread gfxThread;
	
	BufferStrategy buffer;
	Graphics2D bufferGraphics;
	
	private static final int WIDTH = 1080;
	private static final int HEIGHT = 700;
	
	public Gfx()
	{
		setSize(WIDTH, HEIGHT);
		backgroundImage = getBackgroundImage();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Risk!");
		setVisible(true);
		createBufferStrategy(2);
		
		//buffer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		//bufferGraphics = (Graphics2D) buffer.createGraphics();
		buffer = getBufferStrategy();
		bufferGraphics = (Graphics2D) buffer.getDrawGraphics();
		
		gfxThread = new GfxThread(this);
		
		addMouseListener(RiskAI.clickHandler);
		addKeyListener(RiskAI.clickHandler);
	}
	
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
		//g2 = (Graphics2D) g;
		
		bufferGraphics.clearRect(0, 0, WIDTH, HEIGHT);
		bufferGraphics.drawImage(backgroundImage, 10, 40, this);
		
		for(Territory i : RiskAI.territoryData)
		{
			drawTerritoryGraphics(bufferGraphics, i.getTerritoryGraphic());
		}
		
		GuiMessages.draw(bufferGraphics);
		
		buffer.show();
	}
	
	public synchronized void updateGUI()
	{
		repaint();
	}
	
	/**
	 * Draws a TerritoryGraphics.
	 * More precisely, draws an oval in the territory's owner's color, then puts
	 * the number of armies in text on top of it.
	 */
	public void drawTerritoryGraphics(Graphics2D g, TerritoryGraphics icon)
	{
		g.setColor(icon.getColor());
		g.fill(icon.icon);
		g.setColor(Color.BLACK);
		String str = String.valueOf(icon.parent.getUnitCount());
		FontMetrics metrics = g.getFontMetrics(new Font("Arial", Font.PLAIN, 10));
		int stringWidth = metrics.stringWidth(str);
		int stringHeight = metrics.getHeight();
		g.drawString(str, icon.xCoord - stringWidth/2, icon.yCoord + stringHeight/2);
	}
}
