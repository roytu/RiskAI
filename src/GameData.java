import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GameData {
	public List<Player> playerList;
	private Player whoseTurn;
	//Colors to initialize the first 6 players with, in order
	public static final Color[] playerColors = 
	{
		new Color(95,95,255),//blue
		new Color(255,95,95),//red
		new Color(95,255,95),//green
		new Color(255,255,0),//yellow
		new Color(255,127,0),//orange
		new Color(0xF0,0xF0,0xF0)//grey
	};
	//Text names of the colors for player identification
	public static final String[] playerColorNames =
	{
		"Blue",
		"Red",
		"Green",
		"Yellow",
		"Orange",
		"Grey"
	};

	/**
	 * Creates GameData object
	 * @param int Number of human players
	 * @param int Number of computer players 
	 */
	public GameData(int humanCount, int computerCount){
		playerList = new ArrayList<Player>();
		int secretPlayerNumber=0;//(int) (Math.random()*computerCount);
		for(int i=0;i<humanCount;i++)
		{
			Player player = new PlayerHuman(i);
			playerList.add(player);
		}
		for(int i=0;i<computerCount;i++)
		{
			Player player;
			if(i==secretPlayerNumber)
			{
				player = new PlayerComputerBetter(humanCount+i);
			}
			else
			{
				player = new PlayerComputer(humanCount+i);
			}
			playerList.add(player);
		}
	}

	public WinnerReturn gameRun(){
		int turnIndex=0;
		int totalturnnumber=0;
		while(!isGameOver())
		{
			whoseTurn=playerList.get(turnIndex);
			try{whoseTurn.turn();}
			catch(GameOverException e){}
			turnIndex=++turnIndex%playerList.size();
			totalturnnumber++;
			//if(totalturnnumber>500)break;
		}
		return new WinnerReturn(getWinner(),totalturnnumber);

	}
	private boolean isGameOver()
	{
		int numberOfAlivePlayers=0;
		for (Player p:playerList)
		{
			if(p.isAlive()) numberOfAlivePlayers++;
		}
		if(numberOfAlivePlayers==1) return true;
		//else
		return false;
	}

	private Player getWinner()
	{
		for(Player p:playerList)
		{
			if(p.isAlive()) return p;
		}
		return null;
	}

	public void setupGameboard(List<Territory> territoryList)
	{
		//List<List<Territory>> playerTerritoryLists = new ArrayList<List<Territory>>(playerList.size());
		List<Territory> tempTerritoryList=new ArrayList<Territory>(territoryList);
		int nextPlayerIndex=0;
		int numberOfPlayers=playerList.size();
		int random=0;
		while (tempTerritoryList.size()>0)
		{
			random=(int) (Math.random()*tempTerritoryList.size());
			tempTerritoryList.get(random).setOwner(getPlayer(nextPlayerIndex));
			tempTerritoryList.remove(random);
			nextPlayerIndex=++nextPlayerIndex%numberOfPlayers;			
		}
		for(Territory t:territoryList)
		{
			t.getOwner().reinforce(t, 3-t.getUnitCount());
		}
		/*
		while(true)
		{
			int playerIndex = indexOfTurn(); //indexOfTurn shifts turn to next player and returns index of present mover
			whoseTurn = playerList.get(playerIndex);
			whoseTurn.turn();
		}
		 */
	}

	private int indexOfTurn()
	{
		int present = playerList.indexOf(whoseTurn);
		present++;
		present %= playerList.size(); //restart if it was too high
		return present;
	}


	public Player getPlayer(int playerID)
	{
		return playerList.get(playerID);
	}

	public void onClick(Territory clickedOn)
	{
		if (whoseTurn instanceof PlayerHuman)
		{
			((PlayerHuman)whoseTurn).onClick(clickedOn);
		}
		RiskAI.gfx.repaint();
	}
	public void onKeyPress()
	{
		if (whoseTurn instanceof PlayerHuman)
			((PlayerHuman)whoseTurn).onKeyPress();
		RiskAI.gfx.repaint();
	}
}
