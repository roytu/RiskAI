import java.util.List;


public class Continent {
	String name;
	List<Territory> territories;
	int bonus;
	Continent()
	{
		this.name="INVALID CONTINENT";
	}
	Continent(String name, int bonus)
	{
		this.name=name;
		this.bonus=bonus;
	}
	

}
