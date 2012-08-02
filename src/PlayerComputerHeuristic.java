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
		double max = -Double.MAX_VALUE;
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
		if(Territory.canMove(terrFrom, terrTo, Math.min(terrFrom.getUnitCount()-1, 3)))
		{
			move(terrFrom, terrTo, (int)Math.round((getOptimalDeltaUnits(island, terrFrom)+getOptimalDeltaUnits(island, terrTo))/2));
		}
	}
	
	private boolean shouldAttack()
	{
		//TODO do this
		Random random = new Random();
		return random.nextInt(2)==0;
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
		if(totalHeuristic == 0)
		{
			System.out.print("HL");
		}
		double r = getTerritoryHeuristic(territory)/totalHeuristic * totalUnits;
		if(Double.isNaN(r))
		{
			System.out.print("WB");
		}
		return r;
	}
	
	private double getTerritoryHeuristic(Territory terr) {
		// TODO do this
		double h = 0;
		
		//0 : Continent borders
		if(terr.isOnContinentBorder())
		{
			h += RiskAI.params[0];
		}
		System.out.println(h);
		
		//1 : Neighbor count
		h += RiskAI.params[1] * terr.getNumberOfAdjacentTerritories();
		System.out.println(h);
		
		//2 : Vulnerability
		h += RiskAI.params[2] * terr.getVulnerability();
		System.out.println(h);
		
		//3 : Outpost
		h += RiskAI.params[3] * terr.getOutpost();
		System.out.println(h);
		
		//4 : Continent bonus
		h += RiskAI.params[4] * terr.getContinent().getBonus();
		System.out.println(h);
		
		//5 : Size of continent
		h += RiskAI.params[5] * terr.getContinent().territories.size();
		System.out.println(h);
		
		//6 : Countries left for continent bonus
		int countriesLeft = 0;
		for(Territory territory : terr.getContinent().territories)
		{
			if(territory.getOwner() != this)
			{
				countriesLeft++;
			}
		}
		h += RiskAI.params[6] * 1/(countriesLeft + 1);
		
		if(Double.isInfinite(h))
		{
			System.out.println(h);
		}
		 
		return h;
	}

	private Territory getHighestDeltaUnits(Set<Territory> territorySet)
	{
		double max = 0;
		Territory terrMax = null;
		for(Territory terr : territorySet)
		{
			double dUnits = Math.abs(getOptimalDeltaUnits(territorySet, terr));
			if(dUnits >= max)
			{
				max = dUnits;
				terrMax = terr;
			}
			if(terrMax == null)
			{
				System.out.print("HI");
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
			if(dUnits < min)
			{
				min = dUnits;
				terrMin = terr;
			}
		}
		return terrMin;
	}
	
	private Set<Territory> getHighestDeltaDelta(Set<Set<Territory>> islands)
	{
		double max = -Double.MAX_VALUE;
		Set<Territory> maxIsland = null;
		for(Set<Territory> island : islands)
		{
			double deltaH = getTerritoryHeuristic(getHighestDeltaUnits(island));
			double deltaL = - getTerritoryHeuristic(getLowestDeltaUnits(island));
			double deltaDelta = deltaH + deltaL;
			if(deltaDelta > max)
			{
				max = deltaDelta;
				maxIsland = island;
			}
		}
		return maxIsland;
	}
	
}