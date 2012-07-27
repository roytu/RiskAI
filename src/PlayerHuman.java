import java.awt.Color;

public class PlayerHuman extends Player {

	private int stage; //for synch
	private int armiesLeft;
	private Territory from; //For attacking and tactical moving
	private Territory to; //For attacking and tactical moving
	private boolean hasQuitPhase; //to tell when player has left attack/tacmove phase via keystroke

	public PlayerHuman(int playerID) {
		super(playerID);
		stage = 0;
	}

	public PlayerHuman(int playerID, Color color)
	{
		super(playerID,color);
		stage = 0;
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
			reinforcements(t);
			break;
		case 2: //attack phase
			attacks(t);
			break;
		case 3: //tactical move phase
			tacticalMoves(t);
			break;
		}

	}
	
	public void onKeyPress()
	{
		hasQuitPhase = true;
	}

	@Override
	protected void reinforcementPhase() {
		stage = 1;
		armiesLeft = calculateReinforcements();
		while(armiesLeft > 0); //Wait for armies left to be 0. onClick will deploy them.
	}

	@Override
	protected void attackPhase() {
		stage = 2;
		hasQuitPhase = false;
		while(true)
		{
			from = null;
			to = null;
			while (to == null) if (hasQuitPhase) return; //wait for both territories to be picked, if player quits stage break
			GuiMessages.addMessage(this.name+"attacks from " + from + " to " + to);
			attack(from, to);
		}
	}

	@Override
	protected void tacticalMovePhase() {
		stage = 3;
		hasQuitPhase = false;
		while(true)
		{
			from = null;
			to = null;
			while (to == null) if (hasQuitPhase) {stage = 0; return;} //wait for both territories to be picked, if player quits stage end turn
			System.out.println("fortifying from " + from + " to " + to);
			reinforce(from, -1);
			reinforce(to,1);
		}
	}

	private void reinforcements(Territory t)
	{
		if (t.getOwner() == this)
		{
			reinforce(t,1);
			armiesLeft--;
		}
	}

	private void attacks(Territory t)
	{
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
	}
	
	private void tacticalMoves(Territory t)
	{
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
	}
}
