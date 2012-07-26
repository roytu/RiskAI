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
		
		riskAI = new RiskAI();
		continentData = new ArrayList<Continent>(ContinentData.init());
		territoryData = new ArrayList<Territory>(TerritoryData.init(continentData));		
		Gfx testwindow = new Gfx();
		
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
}