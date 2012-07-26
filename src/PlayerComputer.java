import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class PlayerComputer extends Player {
	public PlayerComputer(int playerID) {
		super(playerID);
		// TODO Auto-generated constructor stub
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
