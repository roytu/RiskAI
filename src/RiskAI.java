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
		//Initialize system information
		clickHandler = new HandleClick();
		continentData = new ArrayList<Continent>(ContinentData.init());
		territoryData = new ArrayList<Territory>(TerritoryData.init(continentData));
		
		riskAI = new RiskAI();
	}
	
	public RiskAI()
	{
		Gfx testwindow = new Gfx();
		currentGame = new GameData(4,0);
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