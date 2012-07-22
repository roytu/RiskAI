public class RiskAI {
	static RiskAI riskAI;
	GameData currentGame;
	public static void main(String args[])
	{
		riskAI = new RiskAI();
	}
	public RiskAI()
	{
		currentGame = new GameData();
	}
}