import common.BaseThread;
import java.util.Random;

/**
 * Class Philosopher.
 * Outlines main subroutines of our virtual philosopher.
 * 
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca  
 */
public class Philosopher extends BaseThread
{
	//region Constants
	/**
	 * Max time an action can take (in milliseconds)
	 */
	private static final long TIME_TO_WASTE = 1000L;
	/**
	 * Random number generator
	 */
	private static final Random RNG = new Random();
	//endregion

	//region Fields
	/**
	 * Phrases the philosopher can say (moved out of the method to avoid recomputing all the time)
	 */
	private final String[] phrases =
	{
		"Eh, it's not easy to be a philosopher: eat, think, talk, eat...",
		"You know, true is false and false is true if you think of it",
		"2 + 2 = 5 for extremely large values of 2...",
		"If thee cannot speak, thee must be silent",
		"My number is " + getTID()
	};
	//endregion

	//region Methods
	/**
	 * The act of eating.
	 * - Print the fact that a given phil (their TID) has started eating.
	 * - Then sleep() for a random interval.
	 * - The print that they are done eating.
	 */
	public void eat()
	{
		try
		{
			//Print before and after waiting
			System.out.println("The Philosopher " + getTID() + " has started eating...");
			sleep((long)(RNG.nextDouble() * TIME_TO_WASTE));
			System.out.println("The Philosopher " + getTID() + " is done eating.");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.eat():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of thinking.
	 * - Print the fact that a given phil (their TID) has started thinking.
	 * - Then sleep() for a random interval.
	 * - The print that they are done thinking.
	 */
	public void think()
	{
		//Same idea has eat(), but with a different print statement
		try
		{
			//Print before and after waiting
			System.out.println("The Philosopher " + getTID() + " has started thinking...");
			sleep((long)(RNG.nextDouble() * TIME_TO_WASTE));
			System.out.println("The Philosopher " + getTID() + " is done thinking.");
		}
		catch(InterruptedException e)
		{
			System.err.println("Philosopher.think():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * The act of talking.
	 * - Print the fact that a given phil (their TID) has started talking.
	 * - Say something brilliant at random
	 * - The print that they are done talking.
	 */
	public void talk()
	{
		//Print before and after talking
		System.out.println("The Philosopher " + getTID() + " has started talking...");
		saySomething();
		System.out.println("The Philosopher " + getTID() + " is done talking...");
	}

	/**
	 * Prints out a phrase from the array of phrases at random.
	 * Feel free to add your own phrases.
	 */
	private void saySomething()
	{
		System.out.println ("Philosopher " + getTID() + " says: " + this.phrases[(int)(RNG.nextFloat() * this.phrases.length)]);
	}

	/**
	 * No, this is not the act of running, just the overridden Thread.run()
	 */
	public void run()
	{
		for(int i = 0; i < DiningPhilosophers.DINING_STEPS; i++)
		{
			//Get access through the monitor before eating, then free it up.
			DiningPhilosophers.soMonitor.pickUp(getTID());
			eat();
			DiningPhilosophers.soMonitor.putDown(getTID());
			think();

			/*
			 * TODO:
			 * A decision is made at random whether this particular
			 * philosopher is about to say something terribly useful.
			 */
			if(RNG.nextBoolean()) //Just generate a random boolean to decide
			{
				//Get access through the monitor before talking, then free it up.
				DiningPhilosophers.soMonitor.requestTalk(getTID());
				talk();
				DiningPhilosophers.soMonitor.endTalk(getTID());
			}
		}
	}
	//endregion
}