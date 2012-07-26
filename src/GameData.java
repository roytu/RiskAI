import java.util.ArrayList;
import java.util.List;

public class GameData {
	public List<Player> playerList;
	public void initTerritories()
	{
		
	}
	public GameData(int playerCount)
	{
		playerList = new ArrayList<Player>(playerCount);
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
