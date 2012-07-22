import java.util.*;

public class Territory {
	protected List<Territory> linkedTerritories;
	public String name;
	private Continent continent;
	public Territory()
	{
		linkedTerritories = new ArrayList<Territory>();
	}
	public Territory(String name, Continent continent, List<Territory> ajacentTerritories)
	{
		linkedTerritories=ajacentTerritories;
		this.name=name;
		this.continent=continent;
	}
	public static void initializeTerritories()
	{
		
	}
}
