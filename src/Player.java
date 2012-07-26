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
	
	public Player()
	{
		//cardList = new ArrayList<Card>();
		unitMap = new HashMap<Territory, Integer>();
	}
	protected abstract void placeReinforcements(int number);
	protected abstract void attack(Territory from, Territory to);
	
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
	public void addToTerritory(Territory territory, int number)
	{
		unitMap.put(territory, unitMap.get(territory)+number);
	}
	public boolean isOwnerOf(Territory territory)
	{
		return unitMap.containsKey(territory);
	}
	public int getUnitsInTerritory(Territory territory)
	{
		return unitMap.get(territory);
	}
	public Color getColor()
	{
		return Color.WHITE;
	}
}