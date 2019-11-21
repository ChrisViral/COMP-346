import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Class Monitor 
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca  
 */
public class Monitor   
{
	//region Fields
	private final int count;
	private final boolean[] chopsticks;
	private final ArrayDeque<Integer> talkingQueue;
	private boolean anyTalking = false;
	//endregion

	//region Constructors
	/**
	 * Creates the Monitor
	 * @param piNumberOfPhilosophers Number of dining philosophers
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		this.count = piNumberOfPhilosophers;
		this.chopsticks = new boolean[piNumberOfPhilosophers];
		this.talkingQueue = new ArrayDeque<>(count);
	}
	//endregion

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		//Decide which chopsticks must be picked up
		//The first should always be the one with the lowest ID to avoid starvation
		int first, second;
		//If the last philosopher is calling
		if (piTID == this.count)
		{
			first = 0;
			second = piTID - 1;
		}
		//Any other philosopher
		else
		{
			first = piTID - 1;
			second = piTID;
		}

		//Try to pickup the chopsticks
		try
		{
			//Wait until the first chopstick is free (false)
			while (chopsticks[first])
			{
				wait();
			}
			//Pick it up
			chopsticks[first] = true;

			//Wait until the second chopstick is free, then pick it up as well
			while (chopsticks[second])
			{
				wait();
			}
			chopsticks[second] = true;
		}
		catch (InterruptedException e)
		{
			System.err.println("Monitor.pickup():");
			DiningPhilosophers.reportException(e);
			System.exit(1);
		}
	}

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{
		//Put down the chopsticks
		chopsticks[piTID - 1] = false;
		chopsticks[piTID % this.count] = false;
		//Notify all waiting philosophers so they can see if they can now eat
		notifyAll();
	}

	/**
	 * Only one philosopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk(final int piTID)
	{
		//Check if any philosopher is talking
		if (this.anyTalking)
		{
			//If one is, just ask to be the next to talk
			this.talkingQueue.push(piTID);
			try
			{
				//Else, wait until it is this philosopher's time to speak
				//noinspection ConstantConditions
				do
				{
					wait();
				}
				while (this.talkingQueue.peek() == piTID);
				//Remove itself from the queue and carry on to talk
				this.talkingQueue.pop();
			}
			catch (InterruptedException e)
			{
				System.err.println("Monitor.requestTalk():");
				DiningPhilosophers.reportException(e);
				System.exit(1);
			}
		}
		//If not, just start talking
		else
		{
			this.anyTalking = true;
		}
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk(final int piTID)
	{
		//If no one is waiting to talk, just stop talking
		if (this.talkingQueue.isEmpty())
		{
			this.anyTalking = false;
		}
		//Otherwise, notify all waiting philosophers so that the one whose turn it is can start speaking
		else
		{
			notifyAll();
		}
	}
}

// EOF
