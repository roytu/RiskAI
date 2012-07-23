import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class RiskAI {
	static RiskAI riskAI;
	GameData currentGame;
	static TerritoryData aaa= new TerritoryData();
	static List<Continent> continentData;
	static List<Territory> territoryData;
	public static void main(String args[])
	{
		continentData=new ArrayList<Continent>(ContinentData.init());
		//territoryData= new ArrayList<Territory>(TerritoryData.init(continentData));
		riskAI = new RiskAI();
	}
	public RiskAI()
	{
		currentGame = new GameData(4); //4 player game
	}
}