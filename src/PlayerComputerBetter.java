import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class PlayerComputerBetter extends Player {
	List<Territory> currentCluster;
	Map<Territory, Double> territoriesToAttack;
	Territory territoryTargeted;
	double cost_limit = 5;
	public PlayerComputerBetter(int playerID) {
		super(playerID);
		// TODO Auto-generated constructor stub
	}
	
	public PlayerComputerBetter(int playerID, Color color)
	{
		super(playerID,color);
	}

	@Override
	public void reinforcementPhase()
	{
		//TODO: Be shitty and place everything in one territory
		
		aiThinking();
		Territory territory = getOwnedTerritoryWithHighestUnitCountAjacentTo(territoryTargeted);
		int number = calculateReinforcements();
		reinforce(territory, number);
		GuiMessages.addMessage("Player " + playerID + " reinforced " + territory.name);
	}

	@Override
	protected void attackPhase() {
		for(int i=0;i<5;i++)
		{
			aiThinking();
			if (getLowestCost(territoriesToAttack) > cost_limit) territoryTargeted=null;
			Territory terrFrom = getOwnedTerritoryWithHighestUnitCountAjacentTo(territoryTargeted);
			Territory terrTo = territoryTargeted;
			if(terrTo != null)
			{
				attack(terrFrom, terrTo);
				GuiMessages.addMessage("Player " + playerID + " attacked from " + terrFrom.name + " to " + terrTo.name);
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
	
	private void aiThinking()//this is all TEMPORARY. I will implemet it neater and better.
	{
		/*if(currentCluster == null)*/ currentCluster = evaluateStartingPosition();
		territoriesToAttack = getTerritoryAttackList();
		territoryTargeted = getLowestCostTerritory(territoriesToAttack);
		if (getLowestCost(territoriesToAttack) > cost_limit) territoryTargeted=null;
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
		throw new RuntimeException("no owned territory for " + name + " to attack " + territoryToAttack + " from");
	}
	private Territory getOwnedTerritoryWithHighestUnitCountAjacentTo(Territory targetTerritory)
	{
		int currentHighestTroopCount=-1;
		Territory currentHighestTroopTerritory=null;
		for (Territory i:targetTerritory.getAjacentTerritoryList())
		{
			if(i.getOwner()==this)
			{
				if(i.getUnitCount()>currentHighestTroopCount)
				{
					currentHighestTroopCount=i.getUnitCount();
					currentHighestTroopTerritory=i;
				}
			}
		}
		if(currentHighestTroopTerritory==null) throw new RuntimeException("Computer screwed up: no owned territories ajacent to selected territory");
		return currentHighestTroopTerritory;
	}
	
	private Territory getLowestCostTerritory(Map<Territory, Double> territoryMap)
	{
		Territory currentLowestCostTerritory = null;
		for (Territory i:territoryMap.keySet())
		{
			if(currentLowestCostTerritory==null)currentLowestCostTerritory=i;//to prevent null pointer exception in next step
			if(territoryMap.get(i)<territoryMap.get(currentLowestCostTerritory)) currentLowestCostTerritory=i;		}
		return currentLowestCostTerritory;
	}
	private Double getLowestCost(Map<Territory, Double> territoryMap)
	{
		double currentLowestCost=99999;
		for (Territory i:territoryMap.keySet())
		{
			if(territoryMap.get(i)<currentLowestCost) currentLowestCost=territoryMap.get(i);
		}
		return currentLowestCost;
	}


	private Map<Territory, Double> getTerritoryAttackList()
	{
		Map<Territory, Integer> ajacentEnemyTerritoryList = ajacentEnemyTerritoryHeuristic(currentCluster);
		double ajacentEnemyTerritoryFactor = 1.0;
		/*
		Map<Territory, Integer> HeuristicList2 = Heuristic2(currentCluster);
		double HeuristicFactor = 1.0;
		
		etc.
		 */
		/*return ListA*factorA+ListB*FactorB;*/
		Map<Territory, Double> ajacentEnemyWeightedList =multiplyListWeights(ajacentEnemyTerritoryList,ajacentEnemyTerritoryFactor);
		return addTerritoryWeights(ajacentEnemyWeightedList);
	}
	private Map<Territory, Double> multiplyListWeights(Map<Territory, Integer> mapping, double factor)
	{
		Map<Territory, Double> doubleMap= new HashMap<Territory, Double>();
		for(Territory t:mapping.keySet())
		{
			int valueT=mapping.get(t);
			doubleMap.put(t,valueT*factor);
		}
		return doubleMap;
	}
	//use this as soon as we implement another heuristic.
	/*private Map<Territory, Double> addTerritoryWeights(Map<Territory, Double>[] territoryListArray)
	{
		Map<Territory, Double> compositeMap= new HashMap<Territory, Double>();
		for(Map<Territory, Double> mapInArray : territoryListArray)
		{
			for(Territory t:mapInArray.keySet())
			{   //SUMMARY OF THIS FOR LOOP: compositeMap[key]+=mapInArray[key]
				double mappedTerritoryValue= mapInArray.get(t);
				double currentCompositeTerritoryValue = compositeMap.get(t);
				compositeMap.put(t,currentCompositeTerritoryValue+mappedTerritoryValue);
			}
		}			
		return compositeMap;
	}*/
	private Map<Territory, Double> addTerritoryWeights(Map<Territory, Double> territoryListArray)
	{
		Map<Territory, Double> compositeMap= new HashMap<Territory, Double>();
			for(Territory t:territoryListArray.keySet())
			{   //SUMMARY OF THIS FOR LOOP: compositeMap[key]+=mapInArray[key]
				double mappedTerritoryValue= territoryListArray.get(t)==null?0:territoryListArray.get(t);//arr. tertiary to avoid nulls.
				double currentCompositeTerritoryValue = compositeMap.get(t)==null?0:compositeMap.get(t);
				compositeMap.put(t,currentCompositeTerritoryValue+mappedTerritoryValue);
			}
		return compositeMap;
	}
}