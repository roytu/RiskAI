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
	}

	@Override
	protected void attackPhase() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void tacticalMovePhase() {
		// TODO Auto-generated method stub
		
	}
}
