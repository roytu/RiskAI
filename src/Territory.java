import java.util.*;

public class Territory {
	private List<Territory> linkedTerritories;
	public String name;
	private Continent continent;
	private List<String> linkedTerritoryNames;
	public Territory()
	{
		linkedTerritories = new ArrayList<Territory>();
	}
	public Territory(String name, Continent continent, List<String> ajacentTerritoryNames)
	{
		linkedTerritoryNames= new LinkedList<String>(ajacentTerritoryNames);
		this.name=name;
		this.continent=continent;
	}
	public static void initializeTerritory()
	{
		
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
}
