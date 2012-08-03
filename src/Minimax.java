import java.util.HashMap;
import java.util.Map;


public class Minimax {
	final double MMX_PRUNE_WEIGHT=0.1;//prune at 10% probability
	PlayerComputerBetter owner;
	Heuristics heuristics;
	int numberofPlayers;
	//players are based on playerIDs(player 0, player 1, etc.).
	
	public Minimax(PlayerComputerBetter player,int numberOfPlayers)
	{
		this.owner=player;
		this.numberofPlayers=numberOfPlayers;
		this.heuristics= new Heuristics(owner);
	}
	public void testPossibilities()
	{
		Map<Territory,Double> mmxTerritoryWeights = new HashMap<Territory,Double>();
		round();
	}
	
	
	void round()
	{
		for(int i=0;i<numberOfPlayers;i++)
		{//this player- heuristics(), then split(3)into 3 top choices: repeat 2x. at end, choose best one.
			int currentPlayerIndex = owner.playerID;
			virtualTurn(currentPlayerIndex);
			currentPlayerIndex=++currentPlayerIndex%GameData.numberOfPlayers;
		}
	}


	
	public void enemyTurn()
	{
		
	}
	

	protected void virtualTurn(int playerID) throws GameOverException
	{
		heuristics.setOwner(playerID);
		Map<Territory, Double> tempmap= heuristics.aiThinking();
		virtualReinforcementPhase();
		virtualAttackPhase();
		virtualTacticalMovePhase();
	}
	protected void virtualAttackPhase(int index)
	{

	}
	
	
	
	private void virtualAttack()
	{
		
	}

}
