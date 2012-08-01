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
	double value_limit = 0.1;
	double adjacentEnemyTerritoryFactor = .5;
	double conquerProbabilityFactor = .4;
	double reinforcementsFactor = .1;
	public PlayerComputerBetter(int playerID) {
		super(playerID);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void reinforcementPhase() throws GameOverException
	{
		//TODO: Be shitty and place everything in one territory
		
		aiThinking();
		Territory territory = getOwnedTerritoryWithHighestUnitCountAdjacentTo(territoryTargeted);
		int number = calculateReinforcements();
		reinforce(territory, number);
		GuiMessages.addMessage(name + " reinforced " + territory.name);
	}

	@Override
	protected void attackPhase() throws GameOverException {
		for(int i=0;i<20;i++)
		{
			aiThinking();
			Territory terrFrom = getOwnedTerritoryWithHighestUnitCountAdjacentTo(territoryTargeted);
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
	
	private void aiThinking() throws GameOverException//this is all TEMPORARY. I will implemet it neater and better. I think.
	{
		/*if(currentCluster == null)*/ currentCluster = evaluateStartingPosition();
		territoriesToAttack = getTerritoryAttackList();
		territoryTargeted = getHighestValueTerritory(territoriesToAttack);
		if (getHighestValue(territoriesToAttack) < value_limit) territoryTargeted=null;
		if(territoriesToAttack.isEmpty()) throw new GameOverException();
		//TODO Insert conquer prob heuristic somewhere in here with weights later
		territoryTargeted = getHighestValueTerritory(territoriesToAttack);
	}
	private Territory getHighestValueTerritory(Map<Territory, Double> territoryMap)
	{
		Territory currentHighestValueTerritory = null;
		for (Territory i:territoryMap.keySet())
		{
			if(currentHighestValueTerritory==null)currentHighestValueTerritory=i;//to prevent null pointer exception in next step
			if(territoryMap.get(i)>territoryMap.get(currentHighestValueTerritory)) currentHighestValueTerritory=i;		}
		return currentHighestValueTerritory;
	}
	private Double getHighestValue(Map<Territory, Double> territoryMap)
	{
		double currentHighestValue=0;
		for (Territory i:territoryMap.keySet())
		{
			if(territoryMap.get(i)>currentHighestValue) currentHighestValue=territoryMap.get(i);
		}
		return currentHighestValue;
	}
	
	private List<Territory> evaluateStartingPosition()
	{
		Map<Territory, Integer> ownedTerritories = new HashMap<Territory, Integer>(this.getTerritoryMap());

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
		for (Territory t: startingTerritory.getAdjacentTerritoryList())// ajacent To currentTerritory owned by this player&& not in mapping)
		{
			if(t.getOwner()!=this||discoveredTerritories.contains(t));//do nothing, it's someone else's territory or its already been mapped
			else
			{
				floodFill(t, discoveredTerritories);
			}
		}
	}
	////////
	//Heuristics Combination
	////////
	private Map<Territory, Double> getTerritoryAttackList()
	{
		Map<Territory, Double> adjacentEnemyTerritoryList = adjacentEnemyTerritoryHeuristic(currentCluster);
		
		Map<Territory, Double> conquerProbList = conquerProbabilityHeuristic(currentCluster);
		
		Map<Territory, Double> reinforcementsList = reinforcementsHeuristic(currentCluster);
		
		/*
		Map<Territory, Integer> OtherHeuristicList = OtherHeuristic(currentCluster);
		double OtherHeuristicFactor = 1.0;
		
		etc.
		 */
		Map<Territory, Double> adjacentEnemyWeightedList =multiplyListWeights(adjacentEnemyTerritoryList,adjacentEnemyTerritoryFactor);
		Map<Territory, Double> conquerProbWeightedList = multiplyListWeights(conquerProbList,conquerProbabilityFactor);
		Map<Territory, Double> reinforcementsWeightedList = multiplyListWeights(reinforcementsList, reinforcementsFactor);
		List<Map<Territory,Double>> lists = new ArrayList<Map<Territory,Double>>();
		lists.add(adjacentEnemyWeightedList);
		lists.add(conquerProbWeightedList);
		lists.add(reinforcementsWeightedList);
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
		for (Map<Territory,Double> m : territoryListArray) {if (m == null) throw new IllegalArgumentException("null list");}
		Map<Territory, Double> compositeMap= new HashMap<Territory, Double>();
		for(Map<Territory, Double> mapInArray : territoryListArray)
		{
			for(Territory t:mapInArray.keySet())
			{   //SUMMARY OF THIS FOR LOOP: compositeMap[key]+=mapInArray[key]
				double mappedTerritoryValue= mapInArray.get(t);
				double currentCompositeTerritoryValue = compositeMap.get(t)==null?0:compositeMap.get(t);;
				compositeMap.put(t,currentCompositeTerritoryValue+mappedTerritoryValue);
			}
		}			
		return compositeMap;
	}
	
	////////
	//Individual Heuristics
	////////
	private Map<Territory, Double> adjacentEnemyTerritoryHeuristic(List<Territory> contiguousTerritories)
	{
		Map<Territory,Double> adjacentEnemyTerritoryHeuristicMap = new HashMap<Territory, Double>();
		for (Territory t:contiguousTerritories)
		{
			for (Territory u:t.getAdjacentTerritoryList())
			{
				if(u.getOwner()!=this)
				{
					adjacentEnemyTerritoryHeuristicMap.put(u,(double)numberOfAdjacentEnemyTerritories(u));
				}
			}
		}
		//                 (map to normalize,         min value=0, max value=6, invert)
		return normalizeMap(adjacentEnemyTerritoryHeuristicMap,0,6,true); 
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
			for (Territory u:t.getAdjacentTerritoryList())
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
	/**
	 * @param contiguousTerritories
	 * @return a map mapping each takeable territory to the fraction of total armies obtained by taking it
	 */
	private Map<Territory, Double> reinforcementsHeuristic(List<Territory> contiguousTerritories)
	{
		Map<Territory,Double> reinforcementHeuristicMap = new HashMap<Territory, Double>();
		for (Territory t:contiguousTerritories)
		{
			for (Territory u:t.getAdjacentTerritoryList())
			{
				if(u.getOwner()!=this)
				{
					//map u to fraction of total armies owned by the player
					Set<Territory> owned = new HashSet<Territory>(this.getTerritoryMap().keySet());
					owned.add(u);
					List<Set<Territory>> totalOwneds = new ArrayList<Set<Territory>>();
					for (int i = 0; i < RiskAI.PLAYERS_COMP+RiskAI.PLAYERS_HUMAN; i++) //for each player total
					{
						Set<Territory> otherOwned = RiskAI.currentGame.getPlayer(i).getTerritoryMap().keySet();
						otherOwned.remove(u); //This will not do anything if it doesn't contain u, so it will not give exceptions.
						totalOwneds.add(otherOwned);
					}
					reinforcementHeuristicMap.put(u, reinforcementFraction(owned, totalOwneds));
				}
			}
		}
		return reinforcementHeuristicMap;
	}
	/**Note: Owned must be total territories owned, otherOwned must be the list of lists of territories owned by all players*/
	private double reinforcementFraction(Set<Territory> owned, List<Set<Territory>> totalOwneds)
	{
		double received = (double) calculateReinforcements(owned);
		double total = 0.0;
		for (Set<Territory> aPlayer : totalOwneds)
		{
			total+=(double)calculateReinforcements(aPlayer);
		}
		return received/total;
	}
	
	////////
	//Territory Functions
	////////
	private int numberOfAdjacentEnemyTerritories(Territory territory)
	{
		int numberOfAdjacentEnemyTerritories = 0;
		for (Territory t:territory.getAdjacentTerritoryList())
		{
			if(t.getOwner()!=this) numberOfAdjacentEnemyTerritories++;
		}
		return numberOfAdjacentEnemyTerritories;
	}
	private Territory getOwnedTerritoryAdjacentTo(Territory territoryToAttack)
	{//can add in reinforce territory with most/least number of troops
		for (Territory i:territoryToAttack.getAdjacentTerritoryList())
		{
			if(i.getOwner()==this) return i;
		}
		throw new RuntimeException("no owned territory for " + name + " to attack " + territoryToAttack + " from");
	}
	private Territory getOwnedTerritoryWithHighestUnitCountAdjacentTo(Territory targetTerritory) throws GameOverException
	{
		int currentHighestTroopCount=-1;
		Territory currentHighestTroopTerritory=null;
		for (Territory i:targetTerritory.getAdjacentTerritoryList())
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
		if(currentHighestTroopTerritory==null)
		{
			throw new GameOverException();
		}
		return currentHighestTroopTerritory;
	}
	
	////////
	//Misc. Functions
	////////
	private Map<Territory, Double> normalizeMap(Map<Territory, Double> mapping, float minValue, float maxValue, boolean invert)
	{
		for (Territory t : mapping.keySet())
		{
			double currentValue = mapping.get(t);
			if(invert)
			{
				mapping.put(t, 1-(currentValue/maxValue-minValue));
			}
			else
			{
				mapping.put(t, currentValue/maxValue-minValue);
			}
		}
		return mapping;
	}
}