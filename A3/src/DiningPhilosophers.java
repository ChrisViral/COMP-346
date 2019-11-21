/**
 * Class DiningPhilosophers  
 * The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca     
 */
public class DiningPhilosophers 
{
	/*
	 * ------------
	 * Data members   
	 * ------------
	 */

	/**
	 * This default may be overridden from the command line
	 */
	private static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;

	/**
	 * Dining "iterations" per philosopher thread
	 * while they are socializing there
	 */
	public static final int DINING_STEPS = 10;

	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * -------
	 * Methods
	 * -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] args)
	{
		try
		{
			//Set it to the default to start with
			int iPhilosophers = DEFAULT_NUMBER_OF_PHILOSOPHERS;
			if (args.length > 0)
			{
				//If there are supplied args, try to use the first one as a set number of philosophers
				try
				{
					iPhilosophers = Integer.parseInt(args[0]);
					//Make sure a valid amount if used
					if (iPhilosophers <= 0)
					{
						System.out.println("\"" + iPhilosophers + "\" is not an integer within the accepted range (larger than zero). Default (" + DEFAULT_NUMBER_OF_PHILOSOPHERS + ") will be used.");
						iPhilosophers = DEFAULT_NUMBER_OF_PHILOSOPHERS;
					}
				}
				//If the parse fails, display an error message
				catch (NumberFormatException e)
				{
					System.out.println("\"" + args[0] + "\" is not a valid integer. Default (" + iPhilosophers + ") will be used.");
				}
			}

			// Make the monitor aware of how many philosophers there are
			soMonitor = new Monitor(iPhilosophers);

			// Space for all the philosophers
			Philosopher aoPhilosophers[] = new Philosopher[iPhilosophers];

			// Let 'em sit down
			for(int j = 0; j < iPhilosophers; j++)
			{
				aoPhilosophers[j] = new Philosopher();
				aoPhilosophers[j].start();
			}

			System.out.println
			(
				iPhilosophers +
				" philosopher(s) came in for a dinner."
			);

			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for(int j = 0; j < iPhilosophers; j++)
				aoPhilosophers[j].join();

			System.out.println("All philosophers have left. System terminates normally.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	} // main()

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
}

// EOF
