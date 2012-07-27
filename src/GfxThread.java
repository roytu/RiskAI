
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
			gfx.updateGUI();
			try {
				thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
