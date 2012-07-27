


public class GfxThread implements Runnable {

	Gfx gfx;
	Thread thread;
	
	public GfxThread(Gfx gfx)
	{
		this.gfx = gfx;
		thread = new Thread(this);
		thread.start();
	}
	@Override
	public void run()
	{
		while(true)
		{
			try {
				gfx.updateGUI();
				Thread.sleep(1000);
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}
}
