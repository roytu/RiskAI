import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class RiskAI implements MouseInput{
	static RiskAI riskAI;
	static GameData currentGame;
	static List<Continent> continentData;
	static List<Territory> territoryData;
	static boolean mouseHasClicked=true;//this and line below
	static int territoryIndex=0;
	
	
	
	public static void main(String[] args)
	{
		Gfx testwindow=new Gfx();
		continentData=new ArrayList<Continent>(ContinentData.init());
		territoryData= new ArrayList<Territory>(TerritoryData.init(continentData));
		
		
		
		
		
		
		/*for (Territory t : territoryData)
		{
			t.writeToFile();
		}*/
		//currentGame = new GameData(4); //4 player game by default, but atm all it does is infinite loop
	}
	public static void mouseClicked(MouseEvent e)
	{
		if(mouseHasClicked=false)
		{
			territoryData.get(territoryIndex).setCoordinates(e.getX(), e.getY());
		}
		if(mouseHasClicked==true)
		{
			System.out.println(territoryData.get(territoryIndex));
		}
	}
	
	public static GameData getCurrentGameData()
	{	
		return currentGame;
	}
	public static List<Player> getPlayerList()
	{
		return getCurrentGameData().playerList;
	}
}