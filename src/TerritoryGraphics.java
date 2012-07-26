
/**
 * This class is the small picture showing the color and number of armies that a territory shows.
 * It's supposed to show itself with display() but that needs to be fixed. (See Gfx.draw().)
 * It also requires a set of coordinates to display at, we don't have those in territory data yet.
 * That needs the mouse click thing, probably for Michael to do.
 * @author Ben Huchley
 *
 */
public class TerritoryGraphics
{
	public Territory parent; //the territory it is showing
	public int xCoord; //coords
	public int yCoord;
	public static final int SIDE_LENGTH = 20; //The length of the sides of the rectangle enclosing the oval.
	public static Gfx picture; //The Gfx to draw with. Making a Gfx will automatically assign it here. 
	public TerritoryGraphics(int xCoord,int yCoord,Territory parent)
	{
		this.parent = parent;
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		display();
	}
	public TerritoryGraphics(Territory parent)
	{
		this.parent = parent;
	}
	public void setCoords(int xCoord,int yCoord)
	{
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		display();
	}
	private void display()
	{
		picture.drawTerritoryGraphics(this);
		
	}
}
