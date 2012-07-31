import java.awt.Color;
import java.util.HashSet;
import java.util.Set;


public class PlayerComputerB extends Player {
	
	public PlayerComputerB(int playerID) {
		super(playerID);
		// TODO Auto-generated constructor stub
	}
	
	public PlayerComputerB(int playerID, Color color)
	{
		super(playerID,color);
	}

	@Override
	public void reinforcementPhase()
	{
		//TODO: Reinforce strong heuristic countries
		Territory territory = getMostValuableTerritory(getOwnedTerritories());
		int number = calculateReinforcements();
		reinforce(territory, number);
		GuiMessages.addMessage("Player " + playerID + " reinforced " + territory.name);
	}

	@Override
	protected void attackPhase() {
		for(int i=0;i<20;i++)
		{
			//TODO Loop through linked enemy territories and check for things that are easy to hold
			Set<Territory> ownedEnemyTerritories = new HashSet<Territory>();
			for(Territory ownedTerritory : getOwnedTerritories())
			{
				for(Territory enemy : ownedTerritory.getadjacentEnemyTerritories())
				{
					ownedEnemyTerritories.add(enemy);
				}
			}
			
			Territory terrFrom = null;
			Territory terrTo = getMostValuableTerritory(ownedEnemyTerritories);
			Set<Territory> terrAdj = new HashSet<Territory>(terrTo.getadjacentTerritoryList());
			Set<Territory> terrFromSet = Territory.getStrongestTerritory(terrAdj);
			if(terrFromSet.size()>0)
			{
				for(Territory territory : terrFromSet)
				{
					terrFrom = territory;
				}
			}
		
			if(terrFrom != null && terrTo != null)
			{
				while(Territory.canAttack(terrFrom, terrTo))
				{
					attack(terrFrom, terrTo);
					GuiMessages.addMessage("Player " + playerID + " attacked from " + terrFrom.name + " to " + terrTo.name);
				}
			}
		
			/*
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
			 */
		}
	}

	@Override
	protected void tacticalMovePhase() {
		// TODO Auto-generated method stub
		//Move random
		Territory terrFrom = getRandomControlledTerritory();
		//Territory terrTo = terrFrom.getRandomLinkedOwnedTerritory(this);
		Territory terrTo = getMostValuableTerritory(getOwnedTerritories());
		if(terrTo != null && terrFrom.getUnitCount()>1)
		{
			//getTerritoryHeuristic(terrFrom);
			//getTerritoryHeuristic(terrTo);
			move(terrFrom, terrTo, (int) (Math.floor(terrFrom.getUnitCount()/2)-1));
		}
	}
	
	private int getTerritoryHeuristic(Territory territory)
	{
		int h = 0;
		//h += -territory.getNumberOfadjacentTerritories();
		h += -territory.getadjacentEnemyTerritories().size();
		h += -territory.getRelativeStrength() * 5;
		for(Territory enemy : territory.getadjacentEnemyTerritories())
		{
			h += territory.getOwner().probabilityOfWinning(territory.getUnitCount(), enemy.getUnitCount()) * 5;
			//enemy.getChanceOfSuccessfulAttack(territory.getUnitCount(), enemy.getUnitCount())
		}
		return h;
	}
	
	private Territory getMostValuableTerritory(Set<Territory> territoryList)
	{
		int largestUtility = -9999;
		Territory largestTerritory = null;
		for(Territory territory : territoryList)
		{
			int utility = getTerritoryHeuristic(territory);
			if(utility > largestUtility)
			{
				largestUtility = utility;
				largestTerritory = territory;
			}
		}
		return largestTerritory;
	}
}