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
		for(int i=0;i<20;i++)
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
}