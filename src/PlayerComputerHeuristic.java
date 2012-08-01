import java.util.Random;
import java.util.Set;


public class PlayerComputerHeuristic extends Player {
	
	public PlayerComputerHeuristic(int playerID) {
		super(playerID);
		
	}

	@Override
	protected void reinforcementPhase() throws GameOverException {
		int reinforcements = calculateReinforcements();
		while(reinforcements > 0)
		{
			reinforce(getHighestHeuristic(getOwnedTerritories()),1);
			reinforcements--;
		}
		
	}

	private Territory getHighestHeuristic(Set<Territory> ownedTerritories) {
		double max = Double.MIN_VALUE;
		Territory maxTerr = null;
		for(Territory territory : ownedTerritories)
		{
			double h = getTerritoryHeuristic(territory);
			if(h > max)
			{
				max = h;
				maxTerr = territory;
			}
		}
		return maxTerr;
	}
	
	private Territory getLowestHeuristic(Set<Territory> ownedTerritories) {
		double min = Double.MAX_VALUE;
		Territory minTerr = null;
		for(Territory territory : ownedTerritories)
		{
			double h = getTerritoryHeuristic(territory);
			if(h < min)
			{
				min = h;
				minTerr = territory;
			}
		}
		return minTerr;
	}

	@Override
	protected void attackPhase() throws GameOverException {
		while(shouldAttack())
		{
			Territory terrTo = getHighestHeuristic(getAllLinkedEnemyTerritories());
			Territory terrFrom = getLowestHeuristic(terrTo.getAdjacentEnemyTerritories());
			attack(terrFrom, terrTo);
		}
	}

	@Override
	protected void tacticalMovePhase() {
		Set<Territory> island = getHighestDeltaDelta(getOwnedIslands());
		Territory terrFrom = getLowestDeltaUnits(island);
		Territory terrTo = getHighestDeltaUnits(island);
		if(Territory.canMove(terrFrom, terrTo))
		{
			move(terrFrom, terrTo, (int)Math.round((getOptimalDeltaUnits(island, terrFrom)+getOptimalDeltaUnits(island, terrTo))/2));
		}
	}
	
	private boolean shouldAttack()
	{
		//TODO do this
		return true;
	}
	
	private double getOptimalDeltaUnits(Set<Territory> territorySet, Territory territory)
	{
		return getOptimalUnits(territorySet, territory) - territory.getUnitCount();
	}
	private double getOptimalUnits(Set<Territory> territorySet, Territory territory)
	{
		int totalUnits = 0;
		double totalHeuristic = 0;
		for(Territory terr : territorySet)
		{
			totalUnits += terr.getUnitCount();
			totalHeuristic += getTerritoryHeuristic(terr);
		}
		return getTerritoryHeuristic(territory)/totalHeuristic * totalUnits;
	}
	
	private double getTerritoryHeuristic(Territory terr) {
		// TODO do this
		double h = 0;
		
		//0 : Continent borders
		if(terr.isOnContinentBorder())
		{
			h += RiskAI.params[0];
		}
		
		//1 : Neighbor count
		h += RiskAI.params[1] * terr.getNumberOfAdjacentTerritories();
		
		//2 : Vulnerability
		 
		return 0;
	}

	private Territory getHighestDeltaUnits(Set<Territory> territorySet)
	{
		double max = Double.MIN_VALUE;
		Territory terrMax = null;
		for(Territory terr : territorySet)
		{
			double dUnits = getOptimalDeltaUnits(territorySet, terr);
			if(dUnits > max)
			{
				max = dUnits;
				terrMax = terr;
			}
		}
		return terrMax;
	}
	
	private Territory getLowestDeltaUnits(Set<Territory> territorySet)
	{
		double min = Double.MAX_VALUE;
		Territory terrMin = null;
		for(Territory terr : territorySet)
		{
			double dUnits = getOptimalDeltaUnits(territorySet, terr);
			if(dUnits > min)
			{
				min = dUnits;
				terrMin = terr;
			}
		}
		return terrMin;
	}
	
	private Set<Territory> getHighestDeltaDelta(Set<Set<Territory>> islands)
	{
		double max = Double.MIN_VALUE;
		Set<Territory> maxIsland = null;
		for(Set<Territory> island : islands)
		{
			double deltaDelta = getTerritoryHeuristic(getHighestDeltaUnits(island)) - getTerritoryHeuristic(getLowestDeltaUnits(island));
			if(deltaDelta > max)
			{
				max = deltaDelta;
				maxIsland = island;
			}
		}
		return maxIsland;
	}
	
}