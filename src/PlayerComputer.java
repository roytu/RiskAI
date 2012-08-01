import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class PlayerComputer extends Player {
	List<Territory> currentCluster;
	Map<Territory, Integer> territoriesToAttack;
	Territory territoryTargeted;
	
	public PlayerComputer(int playerID) {
		super(playerID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void reinforcementPhase()
	{
		//TODO: Be shitty and place everything in one territory randomly
		
		Territory territory = getRandomControlledTerritory();
		int number = calculateReinforcements();
		reinforce(territory, number);
		GuiMessages.addMessage(name + " reinforced " + territory.name);
	}

	@Override
	protected void attackPhase() {
		for(int i=0;i<10;i++)
		{
			Territory terrFrom = getRandomControlledTerritory();
			Territory terrTo = terrFrom.getRandomLinkedUnownedTerritory(this);
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
		Territory terrFrom = getRandomControlledTerritory();
		Territory terrTo = terrFrom.getRandomLinkedOwnedTerritory(this);
		if(terrTo != null && terrFrom.getUnitCount()>1)
		{
			//move(terrFrom, terrTo, terrFrom.getUnitCount()-1);
		}
	}
	
	private void aiThinking()//this is all TEMPOROARY. I will implemet it neater and better.
	{
		if(currentCluster == null) currentCluster = evaluateStartingPosition();
		territoriesToAttack = adjacentEnemyTerritoryHeuristic(currentCluster);
		territoryTargeted = getLowestCostTerritory(territoriesToAttack);
	}
	
	
	private List<Territory> evaluateStartingPosition()
	{
		Map<Territory, Integer> ownedTerritories = new HashMap<Territory, Integer>(unitMap);

		List<Territory> discoveredTerritories = new ArrayList<Territory>();
		List<Territory> bestTerritoryGroup = new ArrayList<Territory>();
		List<List<Territory> > contiguousTerritoryListList = new ArrayList<List<Territory>>();
		for(Territory t : ownedTerritories.keySet())
		{
			if(!discoveredTerritories.contains(t))
			{
				List<Territory> contiguousTerritories = new ArrayList<Territory>();
				floodFill(t, contiguousTerritories);
				discoveredTerritories.addAll(contiguousTerritories);
				contiguousTerritoryListList.add(contiguousTerritories);
			}
		}
		bestTerritoryGroup = contiguousTerritoryListList.get(0);
		for(List<Territory> t:contiguousTerritoryListList)
		{
			if(t.size()>bestTerritoryGroup.size()) bestTerritoryGroup= t ;
		}
		return bestTerritoryGroup;
	}
	
	private Map<Territory, Integer> adjacentEnemyTerritoryHeuristic(List<Territory> contiguousTerritories)
	{
		Map<Territory,Integer> adjacentEnemyTerritoryHeuristicMap = new HashMap<Territory, Integer>();
		for (Territory t:contiguousTerritories)
		{
			for (Territory u:t.getadjacentTerritoryList())
			{
				if(u.getOwner()!=this)
				{
					adjacentEnemyTerritoryHeuristicMap.put(u,numberOfadjacentEnemyTerritories(u));
				}
			}
		}
		return adjacentEnemyTerritoryHeuristicMap; 
	}
	
	private void floodFill(Territory startingTerritory, List<Territory> discoveredTerritories)
	{
		discoveredTerritories.add(startingTerritory);
		for (Territory t: startingTerritory.getadjacentTerritoryList())// adjacent To currentTerritory owned by this player&& not in mapping)
		{
			if(t.getOwner()!=this||discoveredTerritories.contains(t));//do nothing, it's someone else's territory or its already been mapped
			else
			{
				floodFill(t, discoveredTerritories);
			}
		}
	}
	
	private int numberOfadjacentEnemyTerritories(Territory territory)
	{
		int numberOfadjacentEnemyTerritories = 0;
		for (Territory t:territory.getadjacentTerritoryList())
		{
			if(t.getOwner()!=this) numberOfadjacentEnemyTerritories++;
		}
		return numberOfadjacentEnemyTerritories;
	}
	
	private Territory getOwnedTerritoryadjacentTo(Territory territoryToAttack)
	{//can add in reinforce territory with most/least number of troops
		for (Territory i:territoryToAttack.getadjacentTerritoryList())
		{
			if(i.getOwner()==this) return i;
		}
		throw new RuntimeException("no territory to attack from");
	}
	private Territory getLowestCostTerritory(Map<Territory, Integer> territoryMap)
	{
		Territory currentLowestCostTerritory=null;
		for (Territory i:territoryMap.keySet())
		{
			if(territoryMap.get(i)>territoryMap.get(currentLowestCostTerritory)) currentLowestCostTerritory=i;
		}
		return currentLowestCostTerritory;
	}
}