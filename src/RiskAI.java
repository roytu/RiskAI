import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class RiskAI{
	static RiskAI riskAI;
	static GameData currentGame;
	static List<Continent> continentData;
	static List<Territory> territoryData;
	
	public static HandleClick clickHandler;
	
	public static void main(String[] args)
	{
		clickHandler = new HandleClick();
		
		riskAI=new RiskAI();
		Gfx testwindow=new Gfx();
		continentData=new ArrayList<Continent>(ContinentData.init());
		territoryData= new ArrayList<Territory>(TerritoryData.init(continentData));
	}
	
	public RiskAI()
	{

	}
	
	public static GameData getCurrentGameData()
	{	
		return currentGame;
	}
	public static List<Player> getPlayerList()
	{
		return getCurrentGameData().playerList;
	}
<<<<<<< HEAD
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		//System.out.println("mouseClicked");
		if(mouseHasClicked==false&&allTerritoriesDone==false)
		{
			territoryData.get(territoryIndex).setCoordinates(e.getX(), e.getY());
			System.out.println("Coordinates for "+territoryData.get(territoryIndex).name+" set.");
			territoryIndex++;
			if(territoryIndex==territoryData.size())
				{
					allTerritoriesDone=true;
					for (Territory t : territoryData)
					{
						t.writeToFile();
					}
				}
			mouseHasClicked=true;
		}
		if(mouseHasClicked==true&&allTerritoriesDone==false)
		{
			System.out.println(territoryData.get(territoryIndex).name);
			mouseHasClicked=false;
		}
		//System.out.println(e.getX()+"   "+e.getY());
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
=======
>>>>>>> 8a7fd1dec150508377811a857d3284ef5eecc691
}