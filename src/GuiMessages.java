import java.awt.Graphics;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;


public class GuiMessages {
	private static final int MAX_MESSAGES = 10;
	
	private static Queue<String> messageQueue = new LinkedList<String>();
	
	public static void addMessage(String m)
	{
		messageQueue.offer(m);
		if(messageQueue.size()>MAX_MESSAGES)
		{
			messageQueue.poll();
		}
	}
	
	public static void draw(Graphics g)
	{
		Iterator<String> it = messageQueue.iterator();
		int i = 0;
		while(it.hasNext())
		{
			g.drawString(it.next(), 20, i * 20 + 400);
			++i;
		}
	}
}
