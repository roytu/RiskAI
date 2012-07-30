import java.awt.Color;
import java.util.List;
import java.util.Map;
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
		Territory territory = getMostValuableTerritory(RiskAI.territoryData);
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
	
	private int getTerritoryHeuristic(Territory territory)
	{
		int h = 0;
		h += 10-territory.getNumberOfAjacentTerritories();
		if(territory.getNumberOfAjacentTerritories() > 0)
		{
			System.out.println("HI");
			h += 100;
		}
		return h;
	}
	
	private Territory getMostValuableTerritory(List<Territory> territoryList)
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