package task5;

// Import (aka include) some stuff.

import common.BaseThread;
import common.Semaphore;

/**
 * Class task1.BlockManager
 * Implements character block "manager" and does twists with threads.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca;
 * Inspired by previous code by Prof. D. Probst
 *
 * $Revision: 1.5 $
 * $Last Revision Date: 2019/02/02 $

 */
public class BlockManager
{
	/**
	 * The stack itself
	 */
	private static BlockStack soStack = new BlockStack();

	/**
	 * Number of threads dumping stack
	 */
	private static final int NUM_PROBERS = 4;

	/**
	 * Chris - Total number of threads
	 */
	private static final int NUM_THREADS = 10;

	/**
	 * Number of steps they take
	 */
	private static int siThreadSteps = 5;

	/**
	 * For atomicity
	 * Chris - Allow only 1 at a time
	 */
	private static Semaphore mutex = new Semaphore(1);

	/*
	 * For synchronization
	 */

	/**
	 * s1 is to make sure phase I for all is done before any phase II begins
	 * Chris - Wait for all ten threads to be ready, so wait for 10 signals
	 */
	private static Semaphore s1 = new Semaphore(1 - NUM_THREADS);
	/**
	 * Phase I done flag
	 */
	private static boolean s1Done = false;

	/**
     * Release all threads from phase I once it is complete
	 */
	private static void releaseAllPhaseI()
	{
		if (!s1Done)
		{
			s1Done = true;
			System.out.println("Phase I completed by all threads");
			for (int i = 0; i < NUM_THREADS - 1; i++)
			{
				s1.Signal();
			}
		}
	}

	/**
	 * s2 is for use in conjunction with Thread.turnTestAndSet() for phase II proceed
	 * in the thread creation order
	 */
	private static Semaphore s2 = new Semaphore(1 - NUM_THREADS);
	/**
	 * Chris - Amount of threads that have completed Phase II
	 */
	private static int phase2Complete = 0;
	private static void finishPhaseII()
	{
		if (++phase2Complete == NUM_THREADS)
		{
			System.out.println("Phase II has been completed by all threads");
		}
	}


	// The main()
	public static void main(String[] argv)
	{
		try
		{
			// Some initial stats...
			System.out.println("Main thread starts executing.");
			System.out.println("Initial value of top = " + soStack.getITop() + ".");
			System.out.println("Initial value of stack top = " + soStack.pick() + ".");
			System.out.println("Main thread will now fork several threads.");

			/*
			 * The birth of threads
			 */
			AcquireBlock ab1 = new AcquireBlock();
			AcquireBlock ab2 = new AcquireBlock();
			AcquireBlock ab3 = new AcquireBlock();

			System.out.println("main(): Three AcquireBlock threads have been created.");

			ReleaseBlock rb1 = new ReleaseBlock();
			ReleaseBlock rb2 = new ReleaseBlock();
			ReleaseBlock rb3 = new ReleaseBlock();

			System.out.println("main(): Three ReleaseBlock threads have been created.");

			// Create an array object first
			CharStackProber[] aStackProbers = new CharStackProber[NUM_PROBERS];

			// Then the CharStackProber objects
			for(int i = 0; i < NUM_PROBERS; i++)
				aStackProbers[i] = new CharStackProber();

			System.out.println("main(): CharStackProber threads have been created: " + NUM_PROBERS);

			/*
			 * Twist 'em all
			 */
			ab1.start();
			aStackProbers[0].start();
			rb1.start();
			aStackProbers[1].start();
			ab2.start();
			aStackProbers[2].start();
			rb2.start();
			ab3.start();
			aStackProbers[3].start();
			rb3.start();

			System.out.println("main(): All the threads are ready.");

			/*
			 * Wait by here for all forked threads to die
			 */
			ab1.join();
			ab2.join();
			ab3.join();

			rb1.join();
			rb2.join();
			rb3.join();

			for(int i = 0; i < NUM_PROBERS; i++)
				aStackProbers[i].join();

			// Some final stats after all the child threads terminated...
			System.out.println("System terminates normally.");
			System.out.println("Final value of top = " + soStack.getITop() + ".");
			System.out.println("Final value of stack top = " + soStack.pick() + ".");
			System.out.println("Final value of stack top-1 = " + soStack.getAt(soStack.getITop() - 1) + ".");
			System.out.println("Stack access count = " + soStack.getAccessCounter());

			System.exit(0);
		}
		catch(InterruptedException e)
		{
			System.err.println("Caught InterruptedException (internal error): " + e.getMessage());
			e.printStackTrace(System.err);
		}
		catch(Exception e)
		{
			reportException(e);
		}
		finally
		{
			System.exit(1);
		}
	} // main()


	/**
	 * Inner AcquireBlock thread class.
	 */
	static class AcquireBlock extends BaseThread
	{
		/**
		 * A copy of a block returned by pop().
         * @see BlockStack#pop()
		 */
		private char cCopy;

		public void run()
		{
			System.out.println("AcquireBlock thread [TID=" + this.iTID + "] starts executing.");


			phase1();

			//Chris - Phase I signal completion, wait for other threads, then release all threads
			s1.Signal();
			s1.Wait();
			releaseAllPhaseI();

			try
			{
				//Chris - Add Mutex wait
				mutex.Wait();

				System.out.println("AcquireBlock thread [TID=" + this.iTID + "] requests Ms block.");

				this.cCopy = soStack.pop();

				System.out.println
				(
					"AcquireBlock thread [TID=" + this.iTID + "] has obtained Ms block " + this.cCopy +
					" from position " + (soStack.getITop() + 1) + "."
				);


				System.out.println
				(
					"Acq[TID=" + this.iTID + "]: Current value of top = " +
					soStack.getITop() + "."
				);

				System.out.println
				(
					"Acq[TID=" + this.iTID + "]: Current value of stack top = " +
					soStack.pick() + "."
				);

				//Chris - Add mutex release
				mutex.Signal();
			}
			catch(Exception e)
			{
				reportException(e);
				System.exit(1);
			}

			//Chris - Wait for all threads to Reach Phase II before starting them, else the output is garbage
			s2.Signal();
			s2.Wait();

			//Chris while not next to start, send signal to next thread and print message
			while (!turnTestAndSet())
			{
				s2.Signal();
				System.out.println("Thread [" + this.iTID + "] has attempted to start it's Phase II but must wait for the correct order.");
				s2.Wait();
			}

			phase2();

			//Chris - Signal for next thread after Phase II has been completed for clarity's sake
			s2.Signal();
			finishPhaseII();


			System.out.println("AcquireBlock thread [TID=" + this.iTID + "] terminates.");
		}
	} // class AcquireBlock


	/**
	 * Inner class ReleaseBlock.
	 */
	static class ReleaseBlock extends BaseThread
	{
		/**
		 * Block to be returned. Default is 'a' if the stack is empty.
		 */
		private char cBlock = 'a';

		public void run()
		{
			System.out.println("ReleaseBlock thread [TID=" + this.iTID + "] starts executing.");


			phase1();

			//Chris - Phase I signal completion, wait for other threads, then release all threads
			s1.Signal();
			s1.Wait();
			releaseAllPhaseI();

			try
			{
				//Chris - Add mutex wait
				mutex.Wait();

				if(!soStack.isEmpty())
					this.cBlock = (char)(soStack.pick() + 1);


				System.out.println
				(
					"ReleaseBlock thread [TID=" + this.iTID + "] returns Ms block " + this.cBlock +
					" to position " + (soStack.getITop() + 1) + "."
				);

				soStack.push(this.cBlock);

				System.out.println
				(
					"Rel[TID=" + this.iTID + "]: Current value of top = " +
					soStack.getITop() + "."
				);

				System.out.println
				(
					"Rel[TID=" + this.iTID + "]: Current value of stack top = " +
					soStack.pick() + "."
				);

				//Chris - Add mutex release
				mutex.Signal();
			}
			catch(Exception e)
			{
				reportException(e);
				System.exit(1);
			}

			//Chris - Wait for all threads to Reach Phase II before starting them, else the output is garbage
			s2.Signal();
			s2.Wait();

			//Chris while not next to start, send signal to next thread and print message
			while (!turnTestAndSet())
			{
				s2.Signal();
				System.out.println("Thread [" + this.iTID + "] has attempted to start it's Phase II but must wait for the correct order.");
				s2.Wait();
			}

			phase2();

			//Chris - Signal for next thread after Phase II has been completed for clarity's sake
			s2.Signal();
			finishPhaseII();

			System.out.println("ReleaseBlock thread [TID=" + this.iTID + "] terminates.");
		}
	} // class ReleaseBlock


	/**
	 * Inner class CharStackProber to dump stack contents.
	 */
	static class CharStackProber extends BaseThread
	{
		public void run()
		{
			phase1();

			//Chris - Phase I signal completion, wait for other threads, then release all threads
			s1.Signal();
			s1.Wait();
			releaseAllPhaseI();

			try
			{
				//Chris - Add mutex wait
				mutex.Wait();

				for(int i = 0; i < siThreadSteps; i++)
				{
					System.out.print("Stack Prober [TID=" + this.iTID + "]: Stack state: ");

					// [s] - means ordinay slot of a stack
					// (s) - current top of the stack
					for(int s = 0; s < soStack.getISize(); s++)
					{
						char value = soStack.getAt(s);
						System.out.print
						(
							(s == soStack.getITop() ? "(" : "[") +
							(value == '$' ? '*' : value) +
							(s == soStack.getITop() ? ")" : "]")
						);
					}
					System.out.println(".");
				}

				//Chris - Add mutex release
				mutex.Signal();
			}
			catch(Exception e)
			{
				reportException(e);
				System.exit(1);
			}

			//Chris - Wait for all threads to Reach Phase II before starting them, else the output is garbage
			s2.Signal();
			s2.Wait();

			//Chris while not next to start, send signal to next thread and print message
			while (!turnTestAndSet())
			{
				s2.Signal();
				System.out.println("Thread [" + this.iTID + "] has attempted to start it's Phase II but must wait for the correct order.");
				s2.Wait();
			}

			phase2();

			//Chris - Signal for next thread after Phase II has been completed for clarity's sake
			s2.Signal();
			finishPhaseII();

		}
	} // class CharStackProber


	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	private static void reportException(Exception poException)
	{
		//Chris - Added checks to which type of exception has been caught and send correct message for custom exceptions
		if (poException instanceof BlockStack.StackFullException)
		{
			System.err.println("Full Stack !!!");
		}
		else if (poException instanceof  BlockStack.StackEmptyException)
		{
			System.err.println("Empty Stack !!!");
		}

		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
} // class task1.BlockManager

// EOF
