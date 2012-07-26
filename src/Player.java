import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public abstract class Player {
	//private List<Card> cardList;
	//need a color for identification
	private Map<Territory, Integer> unitMap;
	protected boolean isHuman;
	private int playerID;
	
	public Player(int playerID)
	{
		this.playerID = playerID;
		
		//cardList = new ArrayList<Card>();
		unitMap = new HashMap<Territory, Integer>();
	}
	protected abstract void placeReinforcements(int number);
	protected abstract void attack(Territory from, Territory to);
	
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
	 * Adds a number of units into a territory.  Units are friendly to the player
	 * currently controlling the specified territory.
	 * @param territory Territory to add units to
	 * @param number Number of units to add
	 */
	public void addToTerritory(Territory territory, int number)
	{
		unitMap.put(territory, unitMap.get(territory)+number);
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
		return unitMap.get(territory);
	}
	
	/**
	 * Gets the current color of the player
	 * @return Color
	 */
	public Color getColor()
	{
		return Color.WHITE;
	}
}