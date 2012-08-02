
public class aiFactors {
	public double factor1=0,factor2=0,factor3=0;
	public double numberOfTurns;
	public double goodWinFraction=0;
	
	public void initRandom()
	{
		factor1=Math.random();
		factor2=Math.random();
		factor3=Math.random();
	}
	public void displayFactors()
	{
		
	}
	public void initStep(double d, double e)
	{
		factor1=d;
		factor2=e;
		factor3=1-(d+e);
	}
	
	public String toString()
	{
		return goodWinFraction+" , "+factor1+" , "+factor2+" , "+factor3+"\n";
	}
}
