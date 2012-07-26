public class PlayerHuman extends Player {

	public PlayerHuman(int playerID) {
		super(playerID);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void placeReinforcements(int number) {
		// TODO Auto-generated method stub
		Territory territory = getRandomControlledTerritory();
		addToTerritory(territory, number);
	}
	
	public void doAttackPhase()
	{
		//TODO implement this
	}

}
