import java.util.ArrayList;
import java.util.List;

import javax.management.BadStringOperationException;


public class ContinentData {
	public List<Continent> continentList = new ArrayList<Continent>();
	public void init()
	{
		continentList.add(new Continent("North America", 5));
		continentList.add(new Continent("South America", 2));
		continentList.add(new Continent("Europe", 5));
		continentList.add(new Continent("Africa", 3));
		continentList.add(new Continent("Asia", 7));
		continentList.add(new Continent("Australia", 2));
		
	}
	public Continent findContinentByName(String continentName) throws BadStringOperationException
	{
		for (Continent i:continentList)
		{
			if(i.name==continentName) return i;
		}
		throw new BadStringOperationException(continentName);
	}

}
