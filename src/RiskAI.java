import java.util.List;

public class RiskAI{
	public static GameData currentGame;
	public static List<Continent> continentData;
	public static List<Territory> territoryData;

	public static HandleClick clickHandler;
	public static Gfx gfx;
	
	public static final int PLAYERS_HUMAN = 0;
	public static final int PLAYERS_COMP = 3;
	
	public static final boolean DEBUG_ENABLED=false;
	//DEBUG
	public static aiFactors[] fac = new aiFactors[100];
	public static aiFactors[] fac2 = new aiFactors[20];
	public static void main(String[] args)
	{
		//Initialize system information
		init();
		//
//		BEST FACTORS:
//		2 players: 0.9,0.1,0.0 14 turns //0.8 , 0.1 , 0.1 15 turns
//		3 players: 0.2 , 0.7 , 0.1  //0.5 , 0.1 , 0.4  52 turns //0.6, 0.3, 0.1 49 turns //0.1 , 0.0 , 0.9 54 turns

		//need to implement value_limit
		if(DEBUG_ENABLED)
		{
			for(int q=0;q<1;q++)
			{
				fac2[q]=new aiFactors();
				double f2=-1;
				for(int i=0;i<33;i++)
				{
					fac[i]=new aiFactors();
					if(i%10==0)f2++;
					fac[i].initStep((i%10)*0.1,f2*0.1);
					float numberOfGames=0;
					float totalTurns=0;
					int goodwins=0;
					while(numberOfGames<100)
					{
						currentGame.setupGameboard(territoryData);
						setAIFactors(i);
						WinnerReturn winner=currentGame.gameRun();
						totalTurns+=winner.numberOfTurns;
						numberOfGames++;
						//System.out.println(numberOfGames+" Games finished. "+winner.winner);
						if(winner.winner instanceof PlayerComputerBetter) goodwins++;//if betterComputer is beaten, DON"T USE THOSE WEIGHTS!!

					}
					//System.out.println(totalTurns/numberOfGames);
					System.out.println(goodwins/numberOfGames);
					fac[i].goodWinFraction=goodwins/numberOfGames;
					//fac[i].numberOfTurns=totalTurns/numberOfGames;
					System.out.println((i+1)+"% completed");
					//fac[i].displayFactors();

				}
				fac2[q]=getBestFactors();
			}
			System.out.println("finished");
		}
		else// (!DEBUG_ENABLED)//28 turns:.2,.5,.3:3 plyaer     -----2 
		{
			float numberOfGames=0;
			float totalTurns=0;
			while(numberOfGames<20)
			{
				currentGame.setupGameboard(territoryData);
				WinnerReturn winner=currentGame.gameRun();
				totalTurns+=winner.numberOfTurns;
				numberOfGames++;
				System.out.println(numberOfGames+" Games finished. "+winner.winner);
			}
			System.out.println(totalTurns/numberOfGames);
		}

	}
	private static void setAIFactors(int instance)
	{
		for(int i=0;i<currentGame.playerList.size();i++)//randomize goodAI factors
		{
			if(currentGame.playerList.get(i) instanceof PlayerComputerBetter)
			{
				PlayerComputerBetter goodai = ((PlayerComputerBetter)(currentGame.playerList.get(i)));
				goodai.adjacentEnemyTerritoryFactor=fac[instance].factor1;
				goodai.conquerProbabilityFactor=fac[instance].factor2;
				goodai.reinforcementsFactor=fac[instance].factor3;
			}
		}
	}

	private static aiFactors getBestFactors()
	{
		double goodWins=0.6;
		aiFactors bestFactor = null;
		for(int i=0;i<100;i++)
		{
			if(fac[i].goodWinFraction>goodWins)
			{
				System.out.print(fac[i]);
				goodWins=0.6;
				bestFactor=fac[i];
			}
		}
		return bestFactor;
	}

	private static void init()
	{
		clickHandler = new HandleClick();
		currentGame = new GameData(PLAYERS_HUMAN, PLAYERS_COMP);
		continentData = ContinentData.init();
		territoryData = TerritoryData.init(continentData);
		gfx = new Gfx();
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