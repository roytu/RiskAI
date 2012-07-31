import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Continent {
	String name;
	List<Territory> territories;
	int bonus;
	Continent(String name){
		this.name=name;
		territories = new ArrayList<Territory>();
	}
	Continent(String name, int bonus){
		this.name=name;
		this.bonus=bonus;
		territories = new ArrayList<Territory>();
	}
	public int getBonus()
	{
		return bonus;
	}
	public List<Territory> territories()
	{
		return Collections.unmodifiableList(territories);
	}
	public void addTerritory(Territory territory)
	{
		territories.add(territory);
	}
}
