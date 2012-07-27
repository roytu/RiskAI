import java.awt.Color;

public class PlayerHuman extends Player {

	private int stage; //for synch
	private int armiesLeft;
	private Territory from; //For attacking and tactical moving
	private Territory to; //For attacking and tactical moving
	private boolean hasQuit; //to tell when player has left attack/tacmove phase via keystroke

	public PlayerHuman(int playerID) {
		super(playerID);
		stage = 0;
	}

	public PlayerHuman(int playerID, Color color)
	{
		super(playerID,color);
		stage = 0;
	}

	protected void placeReinforcements(int number) {
		armiesLeft = number;
		while(armiesLeft > 0); //Wait for armies left to be 0. onClick will deploy them.
	}

	public void doAttackPhase()
	{
		hasQuit = false;
		while(true)
		{
			from = null;
			to = null;
			while (to == null) if (hasQuit) return; //wait for both territories to be picked, if player quits stage break
			System.out.println("attacking from " + from + " to " + to);
			attack(from, to);
		}
	}

	public void onClick(Territory t)
	{
		switch (stage) {
		case 1: //reinforcement phase
			if (t.getOwner() == this)
			{
				reinforce(t,1);
				armiesLeft--;
			}
			break;
		case 2: //attack phase
			if (from == null){
				if (t.getOwner() == this)
				{
					from = t;
					System.out.println("attacking from " + from);
				}
			}
			else if (to == null){
				if (t.getOwner() != this)
				{
					to = t;
					System.out.println("attacking from " + from + " to " + to);
				}
			}
			break;
		case 3: //tactical move phase
			if (from == null){
				if (t.getOwner() == this && getUnitsInTerritory(t) > 1)
				{
					from = t;
					System.out.println("fortifying from " + from);
				}
			}
			else if (to == null){
				if (t.getOwner() == this)
				{
					to = t;
					System.out.println("fortifying from " + from + " to " + to);
				}
			}
			break;
		}

	}
	
	public void onKeyPress()
	{
		hasQuit = true;
	}

	@Override
	protected void reinforcementPhase() {
		stage = 1;
		placeReinforcements(calculateReinforcements());

	}

	@Override
	protected void attackPhase() {
		stage = 2;
		doAttackPhase();
	}

	@Override
	protected void tacticalMovePhase() {
		stage = 3;
		hasQuit = false;
		while(true)
		{
			from = null;
			to = null;
			while (to == null) if (hasQuit) {stage = 0; return;} //wait for both territories to be picked, if player quits stage end turn
			System.out.println("fortifying from " + from + " to " + to);
			reinforce(from, -1);
			reinforce(to,1);
		}
	}

}
