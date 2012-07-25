import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ContinentData {
	public static List<Continent> init()
	{
		List<Continent> continentList = new ArrayList<Continent>();
//		continentList.add(new Continent("North America", 5)); //hard continent code, now use
//		continentList.add(new Continent("South America", 2)); //	ContinentData.txt
//		continentList.add(new Continent("Europe", 5));
//		continentList.add(new Continent("Africa", 3));
//		continentList.add(new Continent("Asia", 7));
//		continentList.add(new Continent("Australia", 2));
		BufferedReader reader;
		try
		{
			reader = new BufferedReader(new FileReader("ContinentData.txt"));
		}
		catch(FileNotFoundException e)
		{
			throw new RuntimeException("Continent Data File Not Found");
		}
		String line=null;
		try {
			while((line= reader.readLine())!= null)
			{
				//DO FILE STUFF HERE
				if(line.charAt(0)!='#')//#=comment symbol
				{
					continentList.add(initContinent(line, continentList));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return continentList;
		
	}
	static public Continent findContinentByName(String continentName, List<Continent> continentList) //throws BadStringOperationException
	{
		for (Continent i:continentList)
		{
			if(i.name.equals(continentName)) return i;
		}
		return new Continent("BAD");
		//throw new BadStringOperationException(continentName);
	}
	
	public static Continent initContinent(String line, List<Continent> continentList)
	{
		//Format: name;bonus  (anything else you want, like comments)
		String[] parts = line.split(";");
		Continent newContinent = new Continent(parts[0],Integer.parseInt(parts[1].substring(0,1)));
		return newContinent;
	}

}
