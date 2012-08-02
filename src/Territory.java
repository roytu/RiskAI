import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Territory {
	private List<Territory> linkedTerritories;
	public String name;
	private Continent continent; //Only for data retrieval
	private List<String> linkedTerritoryNames;
	private Player owner;
	private int numberOfUnits;
	private TerritoryGraphics graphic;
	private static BufferedWriter territoryDataWriter;
	
//	private static final double[][] attackChart = {
//		{0.417, 0.106, 0.027},
//		{0.754, 0.363, 0.206},
//		{0.916, 0.656, 0.470},
//	}; //D vs. A
	
	public Territory(int x, int y)
	{
		linkedTerritories = new ArrayList<Territory>();
		try {			
			territoryDataWriter = new BufferedWriter(new PrintWriter(new FileWriter("TerritoryWriteData.txt")));
		} catch (IOException e) {
			throw new RuntimeException("WTF ECLIPSE");
		}
		//graphic = new TerritoryNode(this);
		//graphic.setCoords(100, 100);
		graphic = new TerritoryGraphics(x,y,this);
	}
	public Territory(String name, Continent continent, List<String> adjacentTerritoryNames, int x, int y)
	{
		this(x,y);
		linkedTerritoryNames= new LinkedList<String>(adjacentTerritoryNames);
		this.name=name;
		this.continent=continent;
		continent.addTerritory(this);
	}
	public List<String> getAdjacentTerritoryNameList()
	{
		return new LinkedList<String>(linkedTerritoryNames);
	}
	public List<Territory> getAdjacentTerritoryList()
	{
		return new LinkedList<Territory>(linkedTerritories);
	}
	public void link(Territory territory)
	{
		linkedTerritories.add(territory);
	}
	public int getNumberOfAdjacentTerritories()
	{
		return linkedTerritories.size();
	}
	public Territory getRandomLinkedTerritory()
	{
		Random random = new Random();
		return linkedTerritories.get(random.nextInt(linkedTerritories.size()));
	}
	public Continent getContinent()
	{
		return this.continent;
	}
	
	public Territory getRandomLinkedUnownedTerritory(Player player)
	{
		List<Territory> territoryList = new ArrayList<Territory>();
		for(int i=0;i<linkedTerritories.size();i++)
		{
			territoryList.add(linkedTerritories.get(i));
		}
		Collections.shuffle(territoryList);
		for(Territory territory : territoryList)
		{
			if(territory.getOwner() != player)
			{
				return territory;
			}
		}
		return null; //no attackable countries
	}
	
	/**
	 * Returns the number of troops on a territory minus the number of enemy
	 * troops on surrounding territories
	 * @return int
	 */
	public int getRelativeStrength()
	{
		int enemyTroops = 0;
		for(Territory territory : getAdjacentEnemyTerritories())
		{
			enemyTroops += territory.getUnitCount();
		}
		return getUnitCount() - enemyTroops;
	}
	
	/**
	 * Returns a set of adjacent owned territories
	 * @return Set<Territory>
	 */
	public Set<Territory> getAdjacentOwnedTerritories()
	{
		Set<Territory> adjacentTerritories = new HashSet<Territory>();
		for (Territory t : getAdjacentTerritoryList())
		{
			if(t.getOwner()==getOwner())
				adjacentTerritories.add(t);
		}
		return adjacentTerritories;
	}
	
	/**
	 * Returns a set of adjacent enemy territories
	 * @return Set<Territory>
	 */
	public Set<Territory> getAdjacentEnemyTerritories()
	{
		Set<Territory> adjacentTerritories = new HashSet<Territory>();
		for (Territory t : getAdjacentTerritoryList())
		{
			if(t.getOwner()!=getOwner())
				adjacentTerritories.add(t);
		}
		return adjacentTerritories;
	}
	
	public Territory getRandomLinkedOwnedTerritory(Player player)
	{
		List<Territory> territoryList = new ArrayList<Territory>();
		for(int i=0;i<linkedTerritories.size();i++)
		{
			territoryList.add(linkedTerritories.get(i));
		}
		Collections.shuffle(territoryList);
		for(Territory territory : territoryList)
		{
			if(territory.getOwner() == player)
			{
				return territory;
			}
		}
		return null; //no neighboring friendly countries
	}
	
	public int getUnitCount()
	{
		return numberOfUnits;
		/*return owner!=null ? owner.getUnitsInTerritory(this) : 0;*/
	}
	public void addUnits(int unitsToAdd)
	{
		numberOfUnits+=unitsToAdd;
	}
	
	public Player getOwner()
	{
		
		return this.owner;
	}

	public void setOwner(Player owner)
	{
		this.owner=owner;
	}
	
	public String toString()
	{
		/*String namePart = name + ", ";
		String linkedPart = "linked to ";
		for (String str : linkedTerritoryNames)
		{
			linkedPart = linkedPart + str + " and ";
		}
		String continentPart = "on continent " + continent.name;
		String ownerPart = ", with " + units + " armies on it";
		return namePart + linkedPart + continentPart + ownerPart;*/
		
		return name;
	}
	
	public String semicolonForm()
	{
		String line = name + ";";
		line += continent.name + ";";
		line+=linkedTerritoryNames.size() + ";";
		for (String str : linkedTerritoryNames)
		{
			line += str + ",";
		}
		line += ";";
		line += String.valueOf(graphic.xCoord) + ";";
		line += String.valueOf(graphic.yCoord) + ";";
		return line;
	}
	
	public void writeToFile()
	{
		try {
			territoryDataWriter.write(semicolonForm() + System.getProperty("line.separator"));
			territoryDataWriter.flush();
		} catch (IOException e) {
			throw new RuntimeException("WTF JAVA");
		}
	}
	
	public TerritoryGraphics getTerritoryGraphic()
	{
		return this.graphic;
	}

	/*
	public double getChanceOfSuccessfulAttack(Territory from, Territory to)
	{
		if(canAttack(from, to))
		{
			return attackChart[to.getUnitCount()-1][from.getUnitCount()-1];
		}
		throw new RuntimeException("Can't attack");
	}
	*/
	
	public static Set<Territory> getStrongestTerritory(Set<Territory> territorySet)
	{
		int max = 0;
		Set<Territory> set = new HashSet<Territory>();
		for(Territory territory : territorySet)
		{
			int unitCount = territory.getUnitCount();
			if(unitCount > max)
			{
				set.clear();
				set.add(territory);
				max = unitCount;
			}
			else if(unitCount == max)
			{
				set.add(territory);
			}
		}
		return set;
	}
	
	public static Set<Territory> getWeakestTerritory(Set<Territory> territorySet)
	{
		int min = Integer.MAX_VALUE;
		Set<Territory> set = new HashSet<Territory>();
		for(Territory territory : territorySet)
		{
			int unitCount = territory.getUnitCount();
			if(unitCount < min)
			{
				set.clear();
				set.add(territory);
				min = unitCount;
			}
			else if(unitCount == min)
			{
				set.add(territory);
			}
		}
		return set;
	}
	
	public static Territory chooseRandomFromSet(Set<Territory> territorySet)
	{
		Random random = new Random();
		int select = random.nextInt(territorySet.size());
		int i = 0;
		for(Territory territory : territorySet)
		{
			if(i==select)
			{
				return territory;
			}
			i++;
		}
		return null;
	}
	
	public static boolean canAttack(Territory from, Territory to)
	{
		if(from.owner != to.owner && from.getUnitCount()>1 && from.getAdjacentTerritoryList().contains(to) && to.getUnitCount() >= 1)
		{
			return true;
		}
		return false;
	}
	
	public Set<Territory> getCluster()
	{
		return getClusterHelper(this, new HashSet<Territory>());
	}
	private Set<Territory> getClusterHelper(Territory around, Set<Territory> seen)
	{
		seen.add(around);
		Set<Territory> cluster = new HashSet<Territory>();
		cluster.add(around);
		for (Territory t: around.getAdjacentTerritoryList())
		{
			if(t.getOwner()==around.getOwner() && !seen.contains(t))
			{
				cluster.addAll(getClusterHelper(t, seen));
			}
		}
		return cluster;
	}
	
	public static boolean canMove(Territory from, Territory to, int number)
	{
		if(from.owner == to.owner && from.isLinked(to) && from.getUnitCount() > 1)
		{
			return true;
		}
		return false;
	}
	
	public boolean isLinked(Territory to)
	{
		if(getOwnedLinks(this).contains(to))
		{
			return true;
		}
		return false;
	}
	
	public static Set<Territory> getOwnedLinks(Territory territory)
	{
		return getOwnedLinksRecursive(territory, new HashSet<Territory>());
	}
	
	private static Set<Territory> getOwnedLinksRecursive(Territory territory, Set<Territory> exclude)
	{
		exclude.add(territory);
		Set<Territory> links = new HashSet<Territory>();
		links.add(territory);
		for(Territory link : territory.getAdjacentOwnedTerritories())
		{
			if(exclude.contains(link))
				continue;
			Set<Territory> linkedLinks = getOwnedLinksRecursive(link, exclude);
			for(Territory linkedLink : linkedLinks)
			{
				links.add(linkedLink);
			}
		}
		return links;
	}
	
	public boolean isOnContinentBorder()
	{
		for(Territory terrNeighbor : getAdjacentTerritoryList())
		{
			if(terrNeighbor.getContinent() != getContinent())
			{
				return true;
			}
		}
		return false;
	}
	
	public double getVulnerability()
	{
		int defenders = getUnitCount();
		int attackers = 0;
		for(Territory enemy : this.getAdjacentEnemyTerritories())
		{
			attackers += enemy.getUnitCount();
		}
		return Player.probabilityOfWinning2(attackers, defenders);
	}
	
	public double getVulnerability(int units)
	{
		int defenders = units;
		int attackers = 0;
		for(Territory enemy : this.getAdjacentEnemyTerritories())
		{
			attackers += enemy.getUnitCount();
		}
		double r = Player.probabilityOfWinning2(attackers, defenders);
		if(r == 0)
		{
			System.out.print("OP");
		}
		return r;
	}
	
	public double getOutpost()
	{
		int attackers = Math.min(getUnitCount()-1, 3);
		if(attackers < 1)
		{
			return 0;
		}
		int defenders = Integer.MAX_VALUE;
		Territory targetTerritory = null;
		for(Territory enemy : getAdjacentEnemyTerritories())
		{
			int minDefenders = enemy.getUnitCount();
			if(minDefenders < defenders)
			{
				defenders = minDefenders;
				targetTerritory = enemy;
			}
		}
		if(targetTerritory == null)
		{
			return 0;
		}
		double r1 = Player.probabilityOfWinning2(attackers, defenders);
		double r2 =  targetTerritory.getVulnerability(attackers);
		double r3 = getVulnerability(getUnitCount() - attackers);
		if(Double.isInfinite(r1) || Double.isInfinite(r2) || Double.isInfinite(r3))
		{
			System.out.println("GW");
		}
		double r = r1 / r2 / r3;
		if(Double.isInfinite(r))
		{
			System.out.println("GT");
		}
		return r; //TODO 
	}
}
