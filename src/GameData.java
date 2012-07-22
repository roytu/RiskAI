import java.util.ArrayList;
import java.util.List;

public class GameData {
	private List<Player> playerList;
	public GameData(int playerCount)
	{
		playerList = new ArrayList<Player>(playerCount);
	}
	public void movePlayer(int player)
	{
		giveReinforcements(player);
	}
	private void giveReinforcements(int player)
	{
		
	}
	
}
