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
		Territory territory = getOwnedTerritoryWithHighestUnitCountAdjacentTo(territoryTargeted);
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
			Territory terrFrom = getOwnedTerritoryWithHighestUnitCountAdjacentTo(territoryTargeted);
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
		//TODO Insert conquer prob heuristic somewhere in here with weights later
		territoryTargeted = getLowestCostTerritory(territoriesToAttack);
		if (getLowestCost(territoriesToAttack) > cost_limit) territoryTargeted=null;
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
	
	////////
	//Heuristics
	////////
	private Map<Territory, Double> adjacentEnemyTerritoryHeuristic(List<Territory> contiguousTerritories)
	{
		Map<Territory,Double> ajacentEnemyTerritoryHeuristicMap = new HashMap<Territory, Double>();
		for (Territory t:contiguousTerritories)
		{
			for (Territory u:t.getAjacentTerritoryList())
			{
				if(u.getOwner()!=this)
				{
					ajacentEnemyTerritoryHeuristicMap.put(u,(double)numberOfAjacentEnemyTerritories(u));
				}
			}
		}
		return ajacentEnemyTerritoryHeuristicMap; 
	}
	
	/**
	 * Note: a territory that can be attacked from multiple places will just use the highest value, it does not merge
	 * the probabilities from the various attackers.
	 */
	private Map<Territory, Double> conquerProbabilityHeuristic(List<Territory> contiguousTerritories)
	{
		Map<Territory,Double> conquerProbabilityHeuristicMap = new HashMap<Territory, Double>();
		for (Territory t:contiguousTerritories)
		{
			for (Territory u:t.getAjacentTerritoryList())
			{
				if(u.getOwner()!=this)
				{
					if (!conquerProbabilityHeuristicMap.containsKey(t))
						conquerProbabilityHeuristicMap.put(u,probabilityOfWinning(t.getUnitCount(),u.getUnitCount()));
					else
					{
						double winningProb = probabilityOfWinning(t.getUnitCount(),u.getUnitCount());
						conquerProbabilityHeuristicMap.put(u, Math.max(winningProb, conquerProbabilityHeuristicMap.get(t)));
					}
				}
			}
		}
		return conquerProbabilityHeuristicMap;
	}
	
	private int numberOfadjacentEnemyTerritories(Territory territory)
	{
		int numberOfadjacentEnemyTerritories = 0;
		for (Territory t:territory.getAjacentTerritoryList())
		{
			if(t.getOwner()!=this) numberOfadjacentEnemyTerritories++;
		}
		return numberOfadjacentEnemyTerritories;
	}
	
	private Territory getOwnedTerritoryadjacentTo(Territory territoryToAttack)
	{//can add in reinforce territory with most/least number of troops
		for (Territory i:territoryToAttack.getAjacentTerritoryList())
		{
			if(i.getOwner()==this) return i;
		}
		throw new RuntimeException("no owned territory for " + name + " to attack " + territoryToAttack + " from");
	}
	private Territory getOwnedTerritoryWithHighestUnitCountadjacentTo(Territory targetTerritory)
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
		if(currentHighestTroopTerritory==null) throw new RuntimeException("Computer screwed up: no owned territories adjacent to selected territory");
		return currentHighestTroopTerritory;
	}

	private Map<Territory, Double> getTerritoryAttackList()
	{
		Map<Territory, Double> ajacentEnemyTerritoryList = adjacentEnemyTerritoryHeuristic(currentCluster);
		double ajacentEnemyTerritoryFactor = 1.0;
		Map<Territory, Double> conquerProbList = conquerProbabilityHeuristic(currentCluster);
		double conquerProbabilityFactor = 5.0; //This has to be much higher since it ranges from 0 to 1
		//rather than from 1 to 6 like the prior one.
		/*
		Map<Territory, Integer> OtherHeuristicList = OtherHeuristic(currentCluster);
		double OtherHeuristicFactor = 1.0;
		
		etc.
		 */
		Map<Territory, Double> ajacentEnemyWeightedList =multiplyListWeights(ajacentEnemyTerritoryList,ajacentEnemyTerritoryFactor);
		Map<Territory, Double> conquerProbWeightedList = multiplyListWeights(conquerProbList,conquerProbabilityFactor);
		List<Map<Territory,Double>> lists = new ArrayList<Map<Territory,Double>>();
		lists.add(ajacentEnemyWeightedList);
		lists.add(conquerProbWeightedList);
		return addTerritoryWeights(lists);
	}
	private Map<Territory, Double> multiplyListWeights(Map<Territory, Double> mapping, double factor)
	{
		Map<Territory, Double> doubleMap= new HashMap<Territory, Double>();
		for(Territory t:mapping.keySet())
		{
			double valueT=mapping.get(t);
			doubleMap.put(t,valueT*factor);
		}
		return doubleMap;
	}
	private Map<Territory, Double> addTerritoryWeights(List<Map<Territory, Double>> territoryListArray)
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
	}

	////////
	//Territory Functions
	////////
	private int numberOfAjacentEnemyTerritories(Territory territory)
	{
		int numberOfAdjacentEnemyTerritories = 0;
		for (Territory t:territory.getAjacentTerritoryList())
		{
			if(t.getOwner()!=this) numberOfAdjacentEnemyTerritories++;
		}
		return numberOfAdjacentEnemyTerritories;
	}
	private Territory getOwnedTerritoryAjacentTo(Territory territoryToAttack)
	{//can add in reinforce territory with most/least number of troops
		for (Territory i:territoryToAttack.getAjacentTerritoryList())
		{
			if(i.getOwner()==this) return i;
		}
		throw new RuntimeException("no owned territory for " + name + " to attack " + territoryToAttack + " from");
	}
	private Territory getOwnedTerritoryWithHighestUnitCountAdjacentTo(Territory targetTerritory)
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
	
	////////
	//Misc. Functions
	////////
	private Map<Territory, Double> normalizeMap(Map<Territory, Double> mapping, float minValue, float maxValue)
	{
		for (Territory t : mapping.keySet())
		{
			double currentValue = mapping.get(t);
			mapping.put(t, currentValue/maxValue-minValue);
		}
		return mapping;
	}
}