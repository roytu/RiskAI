import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class HandleClick implements MouseListener {
	
	static boolean mouseHasClicked = true;//this and line below
	static int territoryIndex = 0;
	static boolean allTerritoriesDone = false;

	public HandleClick()
	{
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		/*if(mouseHasClicked==false && allTerritoriesDone==false)
		{
			RiskAI.territoryData.get(territoryIndex).setCoordinates(e.getX(), e.getY());
			System.out.println("Coordinates for "+RiskAI.territoryData.get(territoryIndex).name+" set.");
			territoryIndex++;
			if(territoryIndex == RiskAI.territoryData.size())
				{
					allTerritoriesDone = true;
					for (Territory t : RiskAI.territoryData)
					{
						t.writeToFile();
					}
				}
			mouseHasClicked = true;
		}
		if(mouseHasClicked == true && allTerritoriesDone == false)
		{
			System.out.println(RiskAI.territoryData.get(territoryIndex).name);
			mouseHasClicked=false;
		}*/
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
