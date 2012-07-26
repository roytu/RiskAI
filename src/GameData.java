import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class GameData {
	public List<Player> playerList;
	//Colors to initialize the first 6 players with, in order
	private static final Color[] playerColors = {Color.BLUE,Color.RED,Color.GREEN,Color.ORANGE,Color.YELLOW,Color.LIGHT_GRAY};
	
	/**
	 * Creates GameData object
	 * @param int Number of human players
	 * @param int Number of computer players 
	 */
	public GameData(int humanCount, int computerCount)
	{
		playerList = new ArrayList<Player>();
		for(int i=0;i<humanCount;i++)
		{
			Player player = new PlayerHuman(i,playerColors[i]);
			playerList.add(player);
		}
		for(int i=0;i<computerCount;i++)
		{
			Player player = new PlayerComputer(i,playerColors[i]);
			playerList.add(player);
		}
	}
	public void movePlayer(Player player)
	{
		giveReinforcements(player);
		doAttack(player);
		//TODO: Player fortifies
		//TODO: Player gets cards
	}
	private void giveReinforcements(Player player)
	{
		//TODO: Give players number of units at beginning of turn
		//Give 3 for now
		player.placeReinforcements(3);
	}
	private void doAttack(Player player)
	{
		Territory terrFrom = player.getRandomControlledTerritory();
		Territory terrTo = terrFrom.getRandomLinkedTerritory();
		player.attack(terrFrom,terrTo);
		player.attack(terrFrom, terrTo);
	}
	public Player getPlayer(int playerID)
	{
		return playerList.get(playerID);
	}
}
