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
	
	public PlayerComputer(int playerID, Color color)
	{
		super(playerID,color);
	}

	@Override
	public void reinforcementPhase()
	{
		//TODO: Be shitty and place everything in one territory randomly
		
		aiThinking();
		Territory territory = getRandomControlledTerritory();
		int number = calculateReinforcements();
		reinforce(territory, number);
		GuiMessages.addMessage("Player " + playerID + " reinforced " + territory.name);
	}

	@Override
	protected void attackPhase() {
		/*for(int i=0;i<20;i++)
		{*/
			Territory terrFrom = getRandomControlledTerritory();
			Territory terrTo = terrFrom.getRandomLinkedUnownedTerritory(this);
			if(terrTo != null)
			{
				attack(terrFrom, terrTo);
				GuiMessages.addMessage("Player " + playerID + " attacked from " + terrFrom.name + " to " + terrTo.name);
			}
		/*}*/
	}

	@Override
	protected void tacticalMovePhase() {
		// TODO Auto-generated method stub
		//Move random
		Territory terrFrom = getRandomControlledTerritory();
		Territory terrTo = terrFrom.getRandomLinkedOwnedTerritory(this);
		if(terrTo != null && terrFrom.getUnitCount()>1)
		{
			move(terrFrom, terrTo, terrFrom.getUnitCount()-1);
		}
	}
	
	private void aiThinking()//this is all TEMPOROARY. I will implemet it neater and better.
	{
		if(currentCluster == null) currentCluster = evaluateStartingPosition();
		territoriesToAttack = ajacentEnemyTerritoryHeuristic(currentCluster);
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
	
	private Map<Territory, Integer> ajacentEnemyTerritoryHeuristic(List<Territory> contiguousTerritories)
	{
		Map<Territory,Integer> ajacentEnemyTerritoryHeuristicMap = new HashMap<Territory, Integer>();
		for (Territory t:contiguousTerritories)
		{
			for (Territory u:t.getAjacentTerritoryList())
			{
				if(u.getOwner()!=this)
				{
					ajacentEnemyTerritoryHeuristicMap.put(u,numberOfAjacentEnemyTerritories(u));
				}
			}
		}
		return ajacentEnemyTerritoryHeuristicMap; 
	}
	
	private void floodFill(Territory startingTerritory, List<Territory> discoveredTerritories)
	{
		discoveredTerritories.add(startingTerritory);
		for (Territory t: startingTerritory.getAjacentTerritoryList())// ajacent To currentTerritory owned by this player&& not in mapping)
		{
			if(t.getOwner()!=this||discoveredTerritories.contains(t));//do nothing, it's someone else's territory or its already been mapped
			else
			{
				floodFill(t, discoveredTerritories);
			}
		}
	}
	
	private int numberOfAjacentEnemyTerritories(Territory territory)
	{
		int numberOfAjacentEnemyTerritories = 0;
		for (Territory t:territory.getAjacentTerritoryList())
		{
			if(t.getOwner()!=this) numberOfAjacentEnemyTerritories++;
		}
		return numberOfAjacentEnemyTerritories;
	}
	
	private Territory getOwnedTerritoryAjacentTo(Territory territoryToAttack)
	{//can add in reinforce territory with most/least number of troops
		for (Territory i:territoryToAttack.getAjacentTerritoryList())
		{
			if(i.getOwner()==this) return i;
		}
		return null;
	}
	private Territory getLowestCostTerritory(Map<Territory, Integer> territoryMap)
	{
		Territory currentLowestCostTerritory=null;
		for (Territory i:territoryMap.keySet())
		{
			if(currentLowestCostTerritory==null)currentLowestCostTerritory=i;//to prevent null pointer exception in next step
			if(territoryMap.get(i)>territoryMap.get(currentLowestCostTerritory))
				{
				currentLowestCostTerritory=i;
				}
		}
		return currentLowestCostTerritory;
	}



}