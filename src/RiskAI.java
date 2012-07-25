import java.util.ArrayList;
import java.util.List;

public class RiskAI {
	static RiskAI riskAI;
	static GameData currentGame;
	static List<Continent> continentData;
	static List<Territory> territoryData;
	
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
	public static GameData getCurrentGameData()
	{	
		return currentGame;
	}
	public static List<Player> getPlayerList()
	{
		return getCurrentGameData().playerList;
	}
}