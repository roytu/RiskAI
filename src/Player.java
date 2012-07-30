import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public abstract class Player {
	public static final int COMPUTER_PLAYER_DELAY_MS = 500;
	//private List<Card> cardList;
	protected Map<Territory, Integer> unitMap;
	protected boolean isHuman;
	protected int playerID;
	private Color color;
	protected String name;
	
	
	public Player(int playerID)
	{
		this.playerID = playerID;
		
		//cardList = new ArrayList<Card>();
		unitMap = new HashMap<Territory, Integer>();
	}
	public Player(int playerID, Color color)
	{
		this(playerID);
		this.color = color;
		this.name=this.getColor().toString();
	}
	
	protected abstract void reinforcementPhase();
	protected abstract void attackPhase();
	protected abstract void tacticalMovePhase();
	
	protected void turn()
	{
		try
		{
			GuiMessages.addMessage(name+"'s turn begins");
			
			GuiMessages.addMessage("REINFORCEMENT PHASE BEGINS");
			GuiMessages.addMessage(name+" recieves "+calculateReinforcements()+" reinforcements.");
			reinforcementPhase();
			//Thread.sleep(1000);
			
			GuiMessages.addMessage("ATTACK PHASE BEGINS");
			attackPhase();
			//Thread.sleep(1000);
			GuiMessages.addMessage("TACTICAL MOVE PHASE BEGINS");
			tacticalMovePhase();
			Thread.sleep(COMPUTER_PLAYER_DELAY_MS);
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected int calculateReinforcements()
	{
		//commented version is actual reinforcement counter, currently at automatically 3 for debug
		//int reinforcements = 0;
		//List<Continent> ownedContinents = ownedContinents();
		//for (Continent c : ownedContinents) reinforcements+=c.getBonus();
		//reinforcements+=Math.max(unitMap.keySet().size()/3,3);
		//return reinforcements;
		return 3;
	}
	
	private List<Continent> ownedContinents()
	{
		List<Continent> owned = new ArrayList<Continent>();
		for (Continent c : RiskAI.continentData)
		{
			boolean isOwned = true;
			for (Territory t : c.territories())
			{
				if (t.getOwner() != this) isOwned = false;
			}
			if (isOwned) owned.add(c);
		}
		return owned;
	}
	
	/**
	 * Adds a number of units into a territory.  Units are friendly to the player
	 * currently controlling the specified territory.
	 * @param territory Territory to add units to
	 * @param number Number of units to add
	 */
	public void reinforce(Territory territory, int number)
	{
		if(unitMap.containsKey(territory))
		{
			unitMap.put(territory, unitMap.get(territory) + number);
		}
		else
		{
			unitMap.put(territory, number);
		}
		if(number>0) GuiMessages.addMessage(territory.name+" reinforced");
	}
	
	public void move(Territory from, Territory to, int number)
	{
		unitMap.put(from, unitMap.get(from)-number);
		unitMap.put(to, unitMap.get(to)+number);
	}
	
	public void attack(Territory from, Territory to) 
	{
		int unitsFrom = from.getUnitCount();
		int unitsTo = to.getUnitCount();
		
		int countDiceFrom = Math.min(unitsFrom-1, 3);
		int countDiceTo = Math.min(unitsTo, 2);
		
		List<Integer> diceFromList = new ArrayList<Integer>();
		List<Integer> diceToList = new ArrayList<Integer>();
		
		//NEXT LINE DEBUG
		//System.out.println(from.name+" attacks "+to.name);
		
		Random random = new Random();
		for(int i=0;i<countDiceFrom;++i)
		{
			//Roll attacking dice
			diceFromList.add(random.nextInt(6)+1);
		}
		for(int i=0;i<countDiceTo;++i)
		{
			//Roll defending dice
			diceToList.add(random.nextInt(6)+1);
		}
		
		Collections.sort(diceFromList);
		Collections.reverse(diceFromList);
		Collections.sort(diceToList);
		Collections.reverse(diceToList);
		//DEBUG
		System.out.println(diceFromList.toString());
		System.out.println(diceToList.toString());
		//END DEBUG
		for(int i=0;i<Math.min(countDiceFrom, countDiceTo);++i)
		{
			if(diceFromList.get(i) > diceToList.get(i))
			{
				//attacker wins
				to.getOwner().reinforce(to, -1);
				//NEXT LINE DEBUG
				//System.out.println("attacker wins");
			}
			else
			{
				//defender wins
				from.getOwner().reinforce(from, -1);
				//NEXT LINE DEBUG
				//System.out.println("defender wins");
			}
		}
		if (to.getUnitCount() == 0) //if it was conquered
		{
			int movedArmies = from.getUnitCount()-1;
			from.getOwner().reinforce(from, -movedArmies);
			to.getOwner().unitMap.remove(to);
			to.setOwner(from.getOwner());
			to.getOwner().reinforce(to, movedArmies);
		}
	}
	
	
	
	/**
	 * Gets a random territory in which the player has units.
	 * Use only if you want your AI to be awful.
	 * @return Random territory owned by player
	 */
	public Territory getRandomControlledTerritory()
	{
		Set<Territory> territories = unitMap.keySet();
		Random random = new Random();
		int territoryID = random.nextInt(territories.size());
		int i = 0;
		for(Territory terr : territories)
		{
			if(i == territoryID)
			{
				return terr;
			}
			++i;
		}
		throw new RuntimeException("wat");
	}


	/**
	 * Gets whether the territory is owned by the player
	 * @param territory
	 * @return whether or not the territory is owned by the player
	 */
	public boolean isOwnerOf(Territory territory)
	{
		return unitMap.containsKey(territory);
	}
	
	/**
	 * Gets the number of units currently in the specified territory.
	 * Territory must be owned by the current player.
	 * @param territory
	 * @return int Number of units
	 */
	public int getUnitsInTerritory(Territory territory)
	{
		if(unitMap.containsKey(territory))
		{
			return unitMap.get(territory);
		}
		else
		{
			return 0;
		}
	}
	
	/**
	 * Gets the current color of the player
	 * @return Color
	 */
	public Color getColor()
	{
		if (color == null) return Color.WHITE;
		return color;
	}
}