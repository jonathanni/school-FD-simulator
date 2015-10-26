package scifair;

public class SHandler implements Runnable
{

	// This is volatile because it should close as soon as it finishes
	private volatile boolean isRunning;

	// Preprocessing
	SSchool demo;

	// Time
	private long millis = 0L;
	private static final long INCREMENT = 100L;

	// GUI
	private SGUI gui;

	public SHandler(int perclassroom)
	{
		demo = new SSampleSchool("Sample School", 60, 40, perclassroom);
		setGui(new SGUI(this, demo, 1));
	}

	public SHandler(int perclassroom, SSchool school)
	{
		demo = new SSchool(school);
		setGui(new SGUI(this, demo, 1));
	}

	@Override
	public void run()
	{

		// Start running the program
		isRunning = true;

		// Get the line up time from an anonymous classroom
		millis += Math.round((double) SClassroom.getStudentsPerClass()
				* SClassroom.getACLPPP()) * 1000;

		while (isRunning)
		{

			gui.repaint();

			for (SPerson person : demo.getAllPeople())
				if (!person.isSafe())
					person.move(demo, getGui());

			if (demo.getSafePeopleNum() == demo.getAllPeople().size())
				isRunning = false;

			millis += INCREMENT;
			// sleep((long) ((10.0 / (double)
			// SClassroom.getStudentsPerClass())
			// * 80));
			// sleep(1);

		}
	}

	public static void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public boolean isRunning()
	{
		return isRunning;
	}

	public void setIsRunning(boolean is)
	{
		isRunning = is;
	}

	public long getMillis()
	{
		return this.millis;
	}

	public void setMillis(long millis)
	{
		this.millis = millis;
	}

	public static long getIncrement()
	{
		return INCREMENT;
	}

	public SGUI getGui()
	{
		return gui;
	}

	public void setGui(SGUI gui)
	{
		this.gui = gui;
	}
}
