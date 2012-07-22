import java.util.*;

public class Territory {
	protected List<Territory> linkedTerritories;
	public String name;
	public Territory()
	{
		linkedTerritories = new ArrayList<Territory>();
	}
	public static void initializeTerritories()
	{
		new Territory();
	}
}
