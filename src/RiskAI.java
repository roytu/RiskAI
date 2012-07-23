import java.util.ArrayList;
import java.util.List;

public class RiskAI {
	static RiskAI riskAI;
	static GameData currentGame;
	static List<Continent> continentData;
	static List<Territory> territoryData;
	public static void main(String[] args)
	{
		continentData=new ArrayList<Continent>(ContinentData.init());
		territoryData= new ArrayList<Territory>(TerritoryData.init(continentData));
		currentGame = new GameData(4); //4 player game by default
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