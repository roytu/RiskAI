import java.util.Set;


public class PlayerComputerHeuristic extends Player {

	public PlayerComputerHeuristic(int playerID) {
		super(playerID);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void reinforcementPhase() throws GameOverException {
		// TODO Auto-generated method stub
		int reinforcements = calculateReinforcements();
		while(reinforcements > 0)
		{
			reinforce(getHighestHeuristic(getOwnedTerritories()),1);
			reinforcements--;
		}
		
	}

	private Territory getHighestHeuristic(Set<Territory> ownedTerritories) {
		// TODO Auto-generated method stub
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

	@Override
	protected void attackPhase() throws GameOverException {
		// TODO Auto-generated method stub
		while(shouldAttack())
		{
			Territory terrTo = getHighestHeuristic(getAllLinkedEnemyTerritories());
			Territory terrFrom = getLowestHeuristic(terrTo.getLinkedTerritories());
			attack(terrFrom, terrTo);
		}
	}

	@Override
	protected void tacticalMovePhase() {
		// TODO Auto-generated method stub
		Set<Territory> island = getHighestDeltaDelta(getOwnedIslands());
		Territory terrFrom = getLowestDeltaUnits(island);
		Territory terrTo = getHighestDeltaUnits(island);
		if(canMove(terrFrom, terrTo))
		{
			move(terrFrom, terrTo, (int)Math.round((getOptimalDeltaUnits(island, terrFrom)+getOptimalDeltaUnits(island, terrTo))/2));
		}
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
		// TODO Auto-generated method stub
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