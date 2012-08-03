import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Heuristics {
	PlayerComputerBetter owner;

	Heuristics(PlayerComputerBetter owner)
	{
		this.owner=owner;
	}

	
	public void aiThinking() throws GameOverException//this is all TEMPORARY. I will implemet it neater and better. I think.
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


	public List<Territory> evaluateStartingPosition()
	{
		Map<Territory, Integer> ownedTerritories = new HashMap<Territory, Integer>(owner.getTerritoryMap());

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
			if(t.getOwner()!=owner||discoveredTerritories.contains(t));//do nothing, it's someone else's territory or its already been mapped
			else
			{
				floodFill(t, discoveredTerritories);
			}
		}
	}
	////////
	//Heuristics Combination
	////////
	public Map<Territory, Double> getTerritoryAttackList()
	{
		Map<Territory, Double> adjacentEnemyTerritoryList = adjacentEnemyTerritoryHeuristic(owner.currentCluster);
		
		Map<Territory, Double> conquerProbList = conquerProbabilityHeuristic(owner.currentCluster);
		
		Map<Territory, Double> continentBonusList = continentBonusHeuristic(owner.currentCluster);
		
		Map<Territory, Double> vulnerabilityList = vulnerabilityHeuristic(owner.currentCluster);
		
		/*
		Map<Territory, Integer> OtherHeuristicList = OtherHeuristic(currentCluster);
		double OtherHeuristicFactor = 1.0;
		
		etc.
		 */
		Map<Territory, Double> adjacentEnemyWeightedList =multiplyListWeights(adjacentEnemyTerritoryList, owner.adjacentEnemyTerritoryFactor);
		Map<Territory, Double> conquerProbWeightedList = multiplyListWeights(conquerProbList, owner.conquerProbabilityFactor);
		Map<Territory, Double> reinforcementsWeightedList = multiplyListWeights(continentBonusList, owner.reinforcementsFactor);
		Map<Territory, Double> vulnerabilityWeightedList = multiplyListWeights(vulnerabilityList, owner.vulnerabilityFactor);
		List<Map<Territory,Double>> lists = new ArrayList<Map<Territory,Double>>();
		lists.add(adjacentEnemyWeightedList);
		lists.add(conquerProbWeightedList);
		lists.add(reinforcementsWeightedList);
		lists.add(vulnerabilityWeightedList);
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
				if(u.getOwner()!=owner)
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
				if(u.getOwner()!=owner)
				{
					if (!conquerProbabilityHeuristicMap.containsKey(t))
						conquerProbabilityHeuristicMap.put(u,owner.probabilityOfWinning2(t.getUnitCount(),u.getUnitCount()));
					else
					{
						double winningProb = owner.probabilityOfWinning2(t.getUnitCount(),u.getUnitCount());
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
	private Map<Territory, Double> continentBonusHeuristic( List<Territory> contiguousTerritories)
	{
		Map<Territory,Double> continentBonusHeuristicMap = new HashMap<Territory, Double>();
		for (Territory t:contiguousTerritories)
		{
			for (Territory u:t.getAdjacentEnemyTerritories())
			{
				double percent=getPercentageOfContinentOwned(u.getContinent());
				//MAGIC EQUATION: 1/(1+e^-(10x-5))- nice logistic curve --  determined by experimentation
				double magicWeight=1/(1+Math.pow(Math.E, -(10*percent-5)));
				continentBonusHeuristicMap.put(u, magicWeight);
			}
		}
		return continentBonusHeuristicMap;
	}
	
	private Map<Territory, Double> continentBorderReinforceHeuristic(List<Territory> contiguousTerritories)
	{
		Map<Territory, Double> borderReinforceHeuristicList = new HashMap<Territory, Double>();
		for(Territory t:contiguousTerritories)
		{
			boolean isBorderTerritory=false;
			for(Territory u:t.getAdjacentEnemyTerritories())
			{
				if(u==null)break;//no adjacent enemy territories
				if(t.getContinent()!=u.getContinent())
					{
						isBorderTerritory=true;
						break;//it is a border territory, no need to look through the rest of enemy adjacent territories
					}
			}
			if(isBorderTerritory)
			{
				borderReinforceHeuristicList.put(t,.5);//TODO
			}
			else
			{
				borderReinforceHeuristicList.put(t,0.0);
			}
		}
		return borderReinforceHeuristicList;
	}
	/**
	 * Note: This heuristic gives values ranging from -1 to 0. 0 is best, -1 is worst. It is intended to have a factor close to 1
	 * which is not counted in making everything else add up to 1.
	 */
	private Map<Territory, Double> vulnerabilityHeuristic( List<Territory> contiguousTerritories)
	{
		Map<Territory, Double> vulnerabilityOf = new HashMap<Territory, Double>();
		int baseSpareArmies = computeSpareArmies(contiguousTerritories);
		for (Territory t:contiguousTerritories)
		{
			for (Territory u:t.getAdjacentTerritoryList())
			{
				if(u.getOwner()!=owner)
				{
					if (!vulnerabilityOf.containsKey(t))
					{
						double spareArmies = (double) baseSpareArmies - u.getUnitCount();
						double borderTerritories = (double) borderTerritoryNumber(contiguousTerritories, u);
						double value = (Math.min(spareArmies / borderTerritories,10.) - 10.)/10.; //from -1 for no spare to 0 for 10 spare
						vulnerabilityOf.put(u, value);
					}
					else
					{
						double spareArmies = (double) baseSpareArmies - u.getUnitCount();
						double borderTerritories = (double) borderTerritoryNumber(contiguousTerritories, u);
						double value = (Math.min(spareArmies / borderTerritories,10.) - 10.)/10.; //from -1 for no spare to 0 for 10 spare
						vulnerabilityOf.put(u, Math.max(value, vulnerabilityOf.get(t)));
					}
				}
			}
		}
		return vulnerabilityOf;
	}
	
	private int computeSpareArmies(List<Territory> contiguousTerritories)
	{
		int totalArmies = 0;
		for (Territory t : contiguousTerritories)
		{
			totalArmies+=t.getUnitCount();
		}
		totalArmies-=contiguousTerritories.size(); //so that it does not count the last army on each territory
		return totalArmies;
	}
	
	private int borderTerritoryNumber(List<Territory> contiguousTerritories, Territory add)
	{
		int borders = 0;
		for (Territory t : contiguousTerritories)
		{
			Set<Territory> adjacent = t.getAdjacentEnemyTerritories();
			if (!(adjacent.isEmpty() || 
					(adjacent.size() == 1 && adjacent.contains(add)))) //if there are adjacent enemy territories other than add
			{
				borders++;
			}
		}
		if (!add.getAdjacentEnemyTerritories().isEmpty()) borders++; //also check to see if add would be a border territory
		return borders;
	}
	////////
	//Territory Functions
	////////
	private int numberOfAdjacentEnemyTerritories( Territory territory)
	{
		int numberOfAdjacentEnemyTerritories = 0;
		for (Territory t:territory.getAdjacentTerritoryList())
		{
			if(t.getOwner()!=owner) numberOfAdjacentEnemyTerritories++;
		}
		return numberOfAdjacentEnemyTerritories;
	}
	private Territory getOwnedTerritoryAdjacentTo( Territory territoryToAttack)
	{//can add in reinforce territory with most/least number of troops
		for (Territory i:territoryToAttack.getAdjacentTerritoryList())
		{
			if(i.getOwner()==owner) return i;
		}
		throw new RuntimeException("no owned territory for " + owner.name + " to attack " + territoryToAttack + " from");
	}
	Territory getOwnedTerritoryWithHighestUnitCountAdjacentTo( Territory targetTerritory) throws GameOverException
	{
		int currentHighestTroopCount=-1;
		Territory currentHighestTroopTerritory=null;
		for (Territory i:targetTerritory.getAdjacentTerritoryList())
		{
			if(i.getOwner()==owner)
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
	
	private double getPercentageOfContinentOwned( Continent continent)
	{
		int numberOfOwnedTerritories=0;
		for(Territory t:continent.territories())
		{
			if(t.getOwner()==owner) numberOfOwnedTerritories++;
		}
		double test = (double)(continent.territories().size());
		return numberOfOwnedTerritories/test;
	}
	
	private double reinforcementFraction( Set<Territory> owned, List<Set<Territory>> totalOwneds)
	{
		double received = (double) owner.calculateReinforcements(owned);
		double total = 0.0;
		for (Set<Territory> aPlayer : totalOwneds)
		{
			total+=(double)owner.calculateReinforcements(aPlayer);
		}
		return received/total;
	}
	private Map<Territory, Double> normalizeMap(Map<Territory, Double> mapping, float minValue, float maxValue, boolean invert)
	{
		for (Territory t : mapping.keySet())
		{
			double currentValue = mapping.get(t);
			if(invert)
			{
				mapping.put(t, 1-((currentValue-minValue)/(maxValue-minValue)));
			}
			else
			{
				mapping.put(t, (currentValue-minValue)/(maxValue-minValue));
			}
		}
		return mapping;
	}

}
