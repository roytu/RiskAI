import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PlayerComputerBetter extends Player {
	List<Territory> currentCluster;
	Map<Territory, Double> territoriesToAttack;
	Territory territoryTargeted;
	Heuristics heuristics;
	double value_limit = 0.4;
	double adjacentEnemyTerritoryFactor = .2;
	double conquerProbabilityFactor = .4;
	double reinforcementsFactor = .4;
	
	double vulnerabilityFactor = 0.6; //loss for overly aggressive moves, independent of other factors because it is subtracted
	
//	double value_limit = 0.3; // Michael's less effective weights
//	double adjacentEnemyTerritoryFactor = .5;
//	double conquerProbabilityFactor = .2;
//	double reinforcementsFactor = .3;
	public PlayerComputerBetter(int playerID) {
		super(playerID);
		heuristics = new Heuristics(this);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void reinforcementPhase() throws GameOverException
	{
		//TODO: Be shitty and place everything in one territory
		
		aiThinking();
		Territory territory = heuristics.getOwnedTerritoryWithHighestUnitCountAdjacentTo(territoryTargeted);
		int number = calculateReinforcements();
		reinforce(territory, number);
		GuiMessages.addMessage(name + " reinforced " + territory.name);
	}

	@Override
	protected void attackPhase() throws GameOverException {
		for(int i=0;i<20;i++)
		{
			heuristics.aiThinking();
			Territory terrFrom = heuristics.getOwnedTerritoryWithHighestUnitCountAdjacentTo(territoryTargeted);
			if (getHighestValue(territoriesToAttack) < value_limit) territoryTargeted=null;
			Territory terrTo = territoryTargeted;
			if(terrTo != null)
			{
				attack(terrFrom, terrTo);
				GuiMessages.addMessage(name + " attacked from " + terrFrom.name + " to " + terrTo.name);
			}
		}
	}

	@Override
	protected void tacticalMovePhase() {
		// TODO Auto-generated method stub
		//Move random
		/*Territory terrFrom = getRandomControlledTerritory();
		Territory terrTo = terrFrom.getRandomLinkedOwnedTerritory(this);
		if(terrTo != null && terrFrom.getUnitCount()>1)
		{
			move(terrFrom, terrTo, terrFrom.getUnitCount()-1);
		}*/
	}
	
	
	
	
	
	
	
	
	
	
	
}