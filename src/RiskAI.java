import java.awt.Color;
import java.util.List;

public class RiskAI{
	static RiskAI riskAI;
	static GameData currentGame;
	static List<Continent> continentData;
	static List<Territory> territoryData;
	
	public static HandleClick clickHandler;
	public static Gfx gfx;
	
	public static final int PLAYERS_HUMAN = 0;
	public static final int PLAYERS_COMP = 2;
	//public static final boolean DEBUG_ENABLED=false;
	//DEBUG
	public static aiFactors[] fac = new aiFactors[100];
	
	public static void main(String[] args)
	{
		//Initialize system information
		init();
		
		//need to implement value_limit
		double f2=-1;
		for(int i=0;i<100;i++)
		{
			fac[i]=new aiFactors();
			if(i%10==0)f2++;
			fac[i].initStep((i%10)*0.1,f2*0.1);
			float numberOfGames=0;
			float totalTurns=0;
			while(numberOfGames<20)
			{
				currentGame.setupGameboard(territoryData);
				setAIFactors(i);
				WinnerReturn winner=currentGame.gameRun();
				totalTurns+=winner.numberOfTurns;
				numberOfGames++;
				System.out.println(numberOfGames+" Games finished.");
				if(winner.winner instanceof PlayerComputer) totalTurns+=1000;//if blue is beaten, DON"T USE THOSE WEIGHTS!!

			}
			System.out.println(totalTurns/numberOfGames);
			fac[i].numberOfTurns=totalTurns/numberOfGames;
			//fac[i].displayFactors();
			
		}
		getBestFactors();
		
	}
	private static void setAIFactors(int instance)
	{
		for(int i=0;i<GameData.playerList.size();i++)//randomize goodAI factors
		{
			if(GameData.playerList.get(i) instanceof PlayerComputerBetter)
			{
				PlayerComputerBetter goodai = ((PlayerComputerBetter)(GameData.playerList.get(i)));
				goodai.adjacentEnemyTerritoryFactor=fac[instance].factor1;
				goodai.conquerProbabilityFactor=fac[instance].factor2;
				goodai.reinforcementsFactor=fac[instance].factor3;
			}
		}
	}

	private static void getBestFactors()
	{
		double bestTurns=9999;
		for(int i=0;i<100;i++)
		{
			if(fac[i].numberOfTurns<bestTurns)
			{
				System.out.println(fac[i]);
				bestTurns=fac[i].numberOfTurns;
			}
		}
	}
	
	private static void init()
	{
		clickHandler = new HandleClick();
		riskAI = new RiskAI();
		currentGame = new GameData(PLAYERS_HUMAN, PLAYERS_COMP);
		continentData = ContinentData.init();
		territoryData = TerritoryData.init(continentData);
		gfx = new Gfx();
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