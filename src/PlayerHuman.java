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
	protected void placeReinforcements(int number) {
		// TODO Auto-generated method stub
		Territory territory = getRandomControlledTerritory();
		reinforce(territory, number);
	}
	
	public void doAttackPhase()
	{
		//TODO implement this
	}

}
