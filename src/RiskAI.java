import java.util.List;

public class RiskAI{
	static RiskAI riskAI;
	static GameData currentGame;
	static List<Continent> continentData;
	static List<Territory> territoryData;
	
	public static HandleClick clickHandler;
	
	private static final int PLAYERS_HUMAN = 3;
	private static final int PLAYERS_COMP = 0;
	
	public static void main(String[] args)
	{
		//Initialize system information
		init();
		currentGame.setupGameboard(territoryData);
		
		/*terrName("Ural").setOwner(currentGame.getPlayer(1));
		terrName("Afghanistan").setOwner(currentGame.getPlayer(2));
		currentGame.getPlayer(1).reinforce(terrName("Ural"),3);
		currentGame.getPlayer(2).reinforce(terrName("Afghanistan"),3);
		currentGame.getPlayer(1).attack(terrName("Ural"),terrName("Afghanistan"));		*/
		
	}
	private static void init()
	{
		clickHandler = new HandleClick();
		riskAI = new RiskAI();
		currentGame = new GameData(PLAYERS_HUMAN, PLAYERS_COMP);
		continentData = ContinentData.init();
		territoryData = TerritoryData.init(continentData);
		Gfx testwindow = new Gfx();
	}
	public RiskAI()
	{
	}
	
	private static Territory terrName(String name)
	{
		return TerritoryData.findTerritoryByName(name, territoryData);
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