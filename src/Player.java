import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;


public abstract class Player {
	public static final int COMPUTER_PLAYER_DELAY_MS = 3;
	//private List<Card> cardList;
	protected volatile Map<Territory, Integer> unitMap;
	protected boolean isHuman;
	protected int playerID;
	private Color color;
	protected String name;
	/*
	0.417 0.106 0.027
	0.754 0.363 0.206
	0.916 0.656 0.470
	*/
	//private static final double[][] averageArmyLoss = {{0.917902,0.338333},{1.221814,0.420921},{0.745345,0.584059}}; 
	//this is the average number of armies you would lose in a single attack with, if it's aAL[a][b], a attackers and b defenders
	//except for that it's reversed, attackers are 0:3, 1:2, 2:1, defenders are 0:2, 1:1, and I doubt it'll be useful 
	
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
	
	protected abstract void reinforcementPhase() throws GameOverException;
	protected abstract void attackPhase() throws GameOverException;
	protected abstract void tacticalMovePhase();
	
	public boolean isAlive()
	{
		if(getTerritoryMap().keySet().size()>0){
			return true;
		}
		return false;
	}
	
	/*public Map<Territory, Integer> getTerritoryMap()
	{
		Map<Territory, Integer> ownedTerritories = new HashMap<Territory, Integer>();
		for (Territory t:RiskAI.territoryData)
		{
			if(t.getOwner()==this) ownedTerritories.put(t, 0);
		}
		return ownedTerritories;
	}*/
	public Map<Territory, Integer> getTerritoryMap()
	{
		HashMap<Territory, Integer> territoryUnitMap = new HashMap<Territory, Integer>();
		for (Territory t:RiskAI.territoryData)
		{
			if(t.getOwner()==this) territoryUnitMap.put(t, 0);
		}
		return territoryUnitMap;
	}
	
	protected void turn() throws GameOverException
	{
		try
		{
			if(isAlive())
			{
				GuiMessages.addMessage(name+"'s turn begins");
			
				GuiMessages.addMessage("REINFORCEMENT PHASE BEGINS");
				GuiMessages.addMessage(name+" recieves "+calculateReinforcements()+" reinforcements.");
				reinforcementPhase();
				Thread.sleep(COMPUTER_PLAYER_DELAY_MS/3);
			
				GuiMessages.addMessage("ATTACK PHASE BEGINS");
				attackPhase();
				Thread.sleep(COMPUTER_PLAYER_DELAY_MS/3);
				GuiMessages.addMessage("TACTICAL MOVE PHASE BEGINS");
				tacticalMovePhase();
				Thread.sleep(COMPUTER_PLAYER_DELAY_MS/3);
			}
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected int calculateReinforcements()
	{
		//commented version is actual reinforcement counter, currently at automatically 3 for debug
		int reinforcements = 0;
//		List<Continent> ownedContinents = ownedContinents();
//		for (Continent c : ownedContinents) reinforcements+=c.getBonus();
		reinforcements+=Math.max(getTerritoryMap().keySet().size()/3,3);
		return reinforcements;
	}
	
	
	private List<Continent> ownedContinents()
	{
		List<Continent> owned = new ArrayList<Continent>();
		for (Continent c : RiskAI.continentData)
		{
			boolean isOwned = true;
			for (Territory t : c.territories())
			{
				if (!isOwnerOf(t)) isOwned = false;
			}
			if (isOwned) owned.add(c);
		}
		return owned;
	}
	//These 2 are for hypothetical territory states that the AI might think of. They refer to a group of territories rather
	//	than a player.
	protected int calculateReinforcements(Set<Territory> territories)
	{
		int reinforcements = 0;
		Set<Continent> ownedContinents = ownedContinents(territories);
		for (Continent c : ownedContinents) reinforcements+=c.getBonus();
		reinforcements+=Math.max(territories.size()/3,3);
		return reinforcements;
	}
	
	protected Set<Continent> ownedContinents(Set<Territory> territories)
	{
		Set<Continent> owned = new HashSet<Continent>();
		for (Continent c : RiskAI.continentData)
		{
			boolean isOwned = true;
			for (Territory t : c.territories())
			{
				if (!territories.contains(t)) isOwned = false;
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
		territory.addUnits(number);
		/*if(unitMap.containsKey(territory))
		{
			unitMap.put(territory, unitMap.get(territory) + number);
		}
		else
		{
			unitMap.put(territory, number);
		}*/
		if(number>0) GuiMessages.addMessage(territory.name+" reinforced");
	}
	
	public void move(Territory from, Territory to, int number)
	{
		from.addUnits(-number);
		to.addUnits(number);
		/*unitMap.put(from, unitMap.get(from)-number);
		unitMap.put(to, unitMap.get(to)+number);*/
	}
	
	public void attack(Territory from, Territory to) 
	{
		if(from.getUnitCount()<=1 || to.getUnitCount()<1 || from.getOwner() == to.getOwner())
			return;
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
		//System.out.println(diceFromList.toString());
		//System.out.println(diceToList.toString());
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
			//to.getOwner().unitMap.remove(to);
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
		Set<Territory> territories = getTerritoryMap().keySet();
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
		if(territory.getOwner()==this)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the number of units currently in the specified territory.
	 * Territory must be owned by the current player.
	 * @param territory
	 * @return int Number of units
	 */
	public int getUnitsInTerritory(Territory territory)
	{
		return territory.getUnitCount();
		/*if(unitMap.containsKey(territory))
		{
			return unitMap.get(territory);
		}
		else
		{
			return 0;
		}*/
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
	
	public double probabilityOfWinning(int attackingArmies, int defendingArmies)
	{
		//this is an approximation at the moment but it's actually pretty close, especially with larger armies
		double attackers = (attackingArmies-2)*1.1; //1.1 is because of 3v2 attacker advantage, -2 is b/c
		double defenders = defendingArmies; // low #s of armies don't have it
		double winProbApprox = (attackers-defenders)/defenders;
		return Math.min(Math.max(winProbApprox, 0), 1); //if it's outside 0-1, just put it as 0 or 1
	}
}