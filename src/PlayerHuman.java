import java.awt.Color;

public class PlayerHuman extends Player {

	public PlayerHuman(int playerID) {
		super(playerID);
	}
	
	public PlayerHuman(int playerID, Color color)
	{
		super(playerID,color);
	}

	@Override
	protected void reinforcementPhase() {
		// TODO Auto-generated method stub
		Territory territory = getRandomControlledTerritory();
		int number = 3; //TODO actual reinforcement count
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
