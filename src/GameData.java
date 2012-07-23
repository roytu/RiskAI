import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameData {
	private List<Player> playerList;
	public void initTerritories()
	{
		/*readfile of territory- name:continent:ajacentTerritories
		
		
		Middle East:5:6:Ukraine,Afghanistan,India,East Africa,Egypt,Southern Europe*/
	}
	public GameData(int playerCount)
	{
		playerList = new ArrayList<Player>(playerCount);
	}
	public void movePlayer(int player)
	{
		giveReinforcements(player);
		
		//TODO: Player attacks
		//TODO: Player fortifies
		//TODO: Player gets cards
	}
	private void giveReinforcements(int player)
	{
		//TODO: Give players number of units at beginning of turn
		//Give 3 for now
		player.placeReinforcements(3);
	}
}
