import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class PlayerComputer extends Player {
	@Override
	public void placeReinforcements(int number)
	{
		//TODO: Be shitty and place everything in one territory randomly
		Territory territory = getRandomControlledTerritory();
		addToTerritory(territory, number);
	}

	@Override
	public void attack(Territory from, Territory to)
	{
		int unitsFrom = from.getUnitCount();
		int unitsTo = to.getUnitCount();
		
		int countDiceFrom = Math.min(unitsFrom-1, 3);
		int countDiceTo = Math.min(unitsTo, 2);
		
		List<Integer> diceFromList = new ArrayList<Integer>();
		List<Integer> diceToList = new ArrayList<Integer>();
		
		Random random = new Random();
		for(int i=0;i<countDiceFrom;++i)
		{
			//TODO: Roll attacking dice
			diceFromList.add(random.nextInt(6)+1);
		}
		for(int i=0;i<countDiceTo;++i)
		{
			//TODO: Roll defending dice
			diceToList.add(random.nextInt(6)+1);
		}
		
		Collections.sort(diceFromList);
		Collections.reverse(diceFromList);
		Collections.sort(diceToList);
		Collections.reverse(diceToList);
		
		for(int i=0;i<Math.min(countDiceFrom, countDiceTo);++i)
		{
			if(countDiceFrom > countDiceTo)
			{
				//attacker wins
				to.getOwner().addToTerritory(to, -1);
			}
			else
			{
				//defender wins
				addToTerritory(from, -1);
				from.getOwner().addToTerritory(from, -1);
			}
		}
		
	}
}
