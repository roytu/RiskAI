import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class RiskAI implements MouseListener{
	static RiskAI riskAI;
	static GameData currentGame;
	static List<Continent> continentData;
	static List<Territory> territoryData;
	static boolean mouseHasClicked=true;//this and line below
	static int territoryIndex=0;
	
	RiskAI()
	{
		Gfx testwindow=new Gfx();
		continentData=new ArrayList<Continent>(ContinentData.init());
		territoryData= new ArrayList<Territory>(TerritoryData.init(continentData));
	}
	
	
	
	public static void main(String[] args)
	{
		riskAI=new RiskAI();		
		/*for (Territory t : territoryData)
		{
			t.writeToFile();
		}*/
		//currentGame = new GameData(4); //4 player game by default, but atm all it does is infinite loop
	}
	
	public static GameData getCurrentGameData()
	{	
		return currentGame;
	}
	public static List<Player> getPlayerList()
	{
		return getCurrentGameData().playerList;
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		System.out.println("mouseClicked");
		if(mouseHasClicked=false)
		{
			territoryData.get(territoryIndex).setCoordinates(e.getX(), e.getY());
			mouseHasClicked=true;
		}
		if(mouseHasClicked==true)
		{
			System.out.println(territoryData.get(territoryIndex));
			mouseHasClicked=false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}