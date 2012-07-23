import java.util.ArrayList;
import java.util.List;

import javax.management.BadStringOperationException;


public class ContinentData {
	public static List<Continent> init()
	{
		List<Continent> continentList = new ArrayList<Continent>();
		continentList.add(new Continent("North America", 5));
		continentList.add(new Continent("South America", 2));
		continentList.add(new Continent("Europe", 5));
		continentList.add(new Continent("Africa", 3));
		continentList.add(new Continent("Asia", 7));
		continentList.add(new Continent("Australia", 2));
		return continentList;
		
	}
	static public Continent findContinentByName(String continentName, List<Continent> continentList) //throws BadStringOperationException
	{
		for (Continent i:continentList)
		{
			if(i.name==continentName) return i;
		}
		return new Continent("BAD");
		//throw new BadStringOperationException(continentName);
	}

}
