import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;


public class PlayerComputer extends Player {
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
		Territory territory = getRandomControlledTerritory();
		int number = calculateReinforcements();
		reinforce(territory, number);
		GuiMessages.addMessage("Player " + playerID + " reinforced " + territory.name);
	}

	@Override
	protected void attackPhase() {
		for(int i=0;i<20;i++)
		{
			Territory terrFrom = getRandomControlledTerritory();
			Territory terrTo = terrFrom.getRandomLinkedUnownedTerritory(this);
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
		Territory terrFrom = getRandomControlledTerritory();
		Territory terrTo = terrFrom.getRandomLinkedOwnedTerritory(this);
		if(terrTo != null && terrFrom.getUnitCount()>1)
		{
			move(terrFrom, terrTo, terrFrom.getUnitCount()-1);
		}
	}
	private void evaluateStartingPosition()
	{
		//not quite sure what i'm doing here.
		Map<Territory, Integer> ownedTerritories = new HashMap<Territory, Integer>(unitMap);

		List<Territory> discoveredTerritories = new ArrayList<Territory>();
		List<Territory> bestTerritory = new ArrayList<Territory>()
	List<List<Territory> > contiguousTerritoryListList = new ArrayList<List<Territory> >;
		for(Territory t : ownedTerritories.keySet())
		{
			if(!discoveredTerritories.contains(t))
			{
				List<Territory> contiguousTerritories = new ArrayList<Territory>;
				floodFill(t, contiguousTerritories));
				discoveredTerritories.add(contiguousTerritories);
				/*if(ownedTerritories.get(t)>ownedTerritories.get(bestTerritory))
					bestTerritory=t;*/
			}
		}
		bestTerritory = contiguousTerritoryListList.get(0);
		for(List<Territory> lt:contiguousTerritoryListList)
		{
			if(lt.size()>bestTerritory.size()) bestTerritory= lt;
		}
		return bestTerritory;
	}
	private Map<Territory, Integer> ajacentEnemyTerritoryHeuristic(List<Territory> contiguousTerritories)
	{
		Map<Terrigtory,Integer> ajacentEnemyTerritoryHeuristicMap = new HashMap<Territory, Integer>();
		for (Territory t:contiguousTerritories)
		{
			for (Territory u:t.getAjacentTerritoryList())
			{
				if(u.getOwner()!=this)
				{
					ajacentEnemyTerritoryHeuristicMap.put(u,ajacentEnemyTerritories(u))
				}
			}
		}
		return ajacentEnemyTerritoryHeuristicMap; 
	}
	
	private void floodFill(Territory startingTerritory, List<Territory> discoveredTerritories)
	{
		discoveredTerritories.add(startingTerritory);
		for (Territory t: startingTerritory.getAjacentTerritories())// ajacent To currentTerritory owned by this player&& not in mapping)
		{
			if(t.getOwner()!=this||discoveredTerritories.contains(t));//do nothing, it's someone else's territory or its already been mapped
			else
			{
				floodFill(t, discoveredTerritories);
			}
		}
	}
	private void getLargestValue
	{
		for(Territory t:ownedTerritories
	}
	
	private int numberOfAjacentEnemyTerritories(Territory territory)
	{
		int numberOfAjacentEnemiyTerritories = 0;
		for (Territory t:territory.getAjacentTerritoryList)
		{
			if(t.getOwner()!=this) numberOfAjacentEnemyTerritories++;
		}
	}
	



}
