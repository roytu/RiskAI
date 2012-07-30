import java.util.Collections;
import java.util.List;


public class Continent {
	String name;
	List<Territory> territories;
	int bonus;
	Continent(String name){
		this.name=name;
	}
	Continent(String name, int bonus){
		this.name=name;
		this.bonus=bonus;
	}
	public int getBonus()
	{
		return bonus;
	}
	public List<Territory> territories()
	{
		return Collections.unmodifiableList(territories);
	}
}
