import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TerritoryData {
	public void initTerritoryData()
	{
		BufferedReader reader;
		try
		{
			reader = new BufferedReader(new FileReader("TerritoryData.txt"));
		}
		catch(FileNotFoundException e)
		{
			throw new RuntimeException("File Not Found");
		}
		String line=null;
		try {
			while((line= reader.readLine())!= null)
			{
				//DO FILE STUFF HERE
				initTerritory(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Territory initTerritory(String territoryData)
	{
		char currentChar;
		int currentDataIndex=0;
		String name="";
		String continent="";
		int numberOfAjacentTerritories=0;
		List<String> ajacentTerritoryNames = new ArrayList<String>();
		String tempAjacentName="";
		for(int i=0;i<territoryData.length();i++)
		{
			if((currentChar=territoryData.charAt(i))!=';')
			{
				if(currentDataIndex==0)name+=currentChar;
				if(currentDataIndex==1)continent+=currentChar;
				if(currentDataIndex==2)numberOfAjacentTerritories+=(int)currentChar;
				if(currentDataIndex==3)
				{
					if(currentChar!=',')
					{
						tempAjacentName+=currentChar;
					}
					else
					{
						ajacentTerritoryNames.add(tempAjacentName);
						tempAjacentName=new String("");
					}
				}				
			}
			else
			{
				currentDataIndex=++currentDataIndex%4;
			}
			
		}
		return new Territory(name, continent, ajacentTerritoryNames);
	}
	

}
