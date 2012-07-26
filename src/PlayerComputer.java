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
	public void placeReinforcements(int number)
	{
		//TODO: Be shitty and place everything in one territory randomly
		Territory territory = getRandomControlledTerritory();
		addToTerritory(territory, number);
	}

	public void doAttackPhase()
	{
		//TODO implement this
	}
}
