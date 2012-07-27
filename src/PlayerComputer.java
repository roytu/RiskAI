import java.awt.Color;


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
		int number = 3; //TODO temporary
		reinforce(territory, number);
		GuiMessages.addMessage("Player " + playerID + " reinforced " + territory.name);
	}

	@Override
	protected void attackPhase() {
		Territory terrFrom = getRandomControlledTerritory();
		Territory terrTo = terrFrom.getRandomLinkedUnownedTerritory(this);
		if(terrTo != null)
		{
			attack(terrFrom, terrTo);
			GuiMessages.addMessage("Player " + playerID + " attacked from " + terrFrom.name + " to " + terrTo.name);
		}
	}

	@Override
	protected void tacticalMovePhase() {
		// TODO Auto-generated method stub
		
	}
}
