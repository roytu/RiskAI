import java.util.*;

public class Territory {
	private List<Territory> linkedTerritories;
	public String name;
	//private Continent continent;
	private List<String> linkedTerritoryNames;
	public Territory()
	{
		linkedTerritories = new ArrayList<Territory>();
	}
	public Territory(String name, Continent continent, List<String> ajacentTerritoryNames)
	{
		linkedTerritoryNames= new LinkedList<String>(ajacentTerritoryNames);
		this.name=name;
		//this.continent=continent;
	}
	public List<String> getAjacentTerritoryNameList()
	{
		return new LinkedList<String>(linkedTerritoryNames);
	}
	public void link(Territory territory)
	{
		linkedTerritories.add(territory);
	}
	public int getNumberOfAjacentTerritories()
	{
		return linkedTerritories.size();
	}
	public Territory getRandomLinkedTerritory()
	{
		Random random = new Random();
		return linkedTerritories.get(random.nextInt(linkedTerritories.size()));
	}
	public void attackTerritory(Territory to)
	{
		int unitsFrom = getUnitCount();
		int unitsTo = to.getUnitCount();
		
		int countDiceFrom = Math.min(unitsFrom-1, 3);
		int countDiceTo = Math.min(unitsTo, 2);
		
		List<Integer> diceFromList = new ArrayList<Integer>();
		List<Integer> diceToList = new ArrayList<Integer>();
		
		Random random = new Random();
		for(int i=0;i<countDiceFrom;++i)
		{
			//TODO: Roll attacking dice
			diceFromList.add(random.nextInt(6)+1);
		}
		for(int i=0;i<countDiceTo;++i)
		{
			//TODO: Roll defending dice
			diceToList.add(random.nextInt(6)+1);
		}
		
		Collections.sort(diceFromList);
		Collections.reverse(diceFromList);
		Collections.sort(diceToList);
		Collections.reverse(diceToList);
		
		for(int i=0;i<Math.min(countDiceFrom, countDiceTo);++i)
		{
			if(countDiceFrom > countDiceTo)
			{
				//attacker wins
				to.getOwner().addToTerritory(to, -1);
			}
			else
			{
				//defender wins
				getOwner().addToTerritory(this, -1);
			}
		}
	}
	private int getUnitCount()
	{
		return getOwner().getUnitsInTerritory(this);
	}
	public Player getOwner()
	{
		for (Player player : RiskAI.getPlayerList())
		{
			if(player.isOwnerOf(this))
			{
				return player;
			}
		}
		return null; //no owner
	}
}
