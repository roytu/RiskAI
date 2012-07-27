import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;


public class HandleClick implements MouseListener, KeyListener 
{
	
//	public static enum ClickStates {
//		EDIT_MAP,
//		MOVE_HUMAN,
//		MOVE_COMPUTER
//	};
//	
//	private static ClickStates clickState;
	
	/*
	static boolean mouseHasClicked = true;//this and line below
	static int territoryIndex = 0;
	static boolean allTerritoriesDone = false;
	*/

	public HandleClick()
	{
		//clickState = ClickStates.MOVE_HUMAN;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
//		switch(clickState)
//		{
//		case EDIT_MAP:
//			/*
//			if(mouseHasClicked==false && allTerritoriesDone==false)
//			{
//				RiskAI.territoryData.get(territoryIndex).setCoordinates(e.getX(), e.getY());
//				System.out.println("Coordinates for "+RiskAI.territoryData.get(territoryIndex).name+" set.");
//				territoryIndex++;
//				if(territoryIndex == RiskAI.territoryData.size())
//				{
//					allTerritoriesDone = true;
//					for (Territory t : RiskAI.territoryData)
//					{
//						t.writeToFile();
//					}
//				}
//			mouseHasClicked = true;
//			}
//			if(mouseHasClicked == true && allTerritoriesDone == false)
//			{
//				System.out.println(RiskAI.territoryData.get(territoryIndex).name);
//				mouseHasClicked=false;
//			}
//			 */
//			break;
//		case MOVE_HUMAN:
//			
//			break;
//		}
		Territory clickedOn = findTerritoryByPosition(e.getX(),e.getY());
		if (clickedOn != null) RiskAI.currentGame.onClick(clickedOn); //implement this method
	}
	
	private Territory findTerritoryByPosition(int x, int y)
	{
		List<Territory> territoryData = RiskAI.territoryData;
		for (Territory t : territoryData)
		{
			TerritoryGraphics graphic = t.getTerritoryGraphic();
			if (graphic.icon.contains(x, y)) return t;
		}
		return null;
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

	public void keyPressed(KeyEvent arg0) {
		System.out.println("awesome");
		RiskAI.currentGame.onKeyPress();
		
	}

	public void keyReleased(KeyEvent arg0) {
		System.out.println("awesome2");
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent arg0) {
		System.out.println("awesome3");
		// TODO Auto-generated method stub
		
	}

}
