import CharStackExceptions.*;
import Threading.*;

/**
 * CharStack manager class and Program entry point
 */
public class StackManager
{
    //region Inner classes
    /**
     * Consumer thread class
     */
    static class Consumer extends BaseThread
    {
        //region Fields
        private char copy; //A copy of a block returned by pop()
        //endregion

        //region Methods
        /**
         * Runs the thread
         */
        @Override
        public void run()
        {
            System.out.println("Consumer thread [TID=" + this.iTID + "] starts executing.");
            for (int i = 0; i < iThreadSteps; i++)
            {
                //Insert your code in the following:
                mutex.Wait();
                try
                {
                    this.copy = stack.pop();
                    System.out.println("Consumer thread [TID=" + this.iTID + "] pops character =" + this.copy);
                }
                catch (CharStackEmptyException e)
                {
                    System.out.println("CharStackException was caught: " + e.getMessage());
                    e.printStackTrace();
                }
                mutex.Signal();
                //End inserted code
            }
            System.out.println("Consumer thread [TID=" + this.iTID + "] terminates.");
        }
        //endregion
    }

    /**
     * Producer thread class
     */
    static class Producer extends BaseThread
    {
        //region Fields
        private char block; //Block to be returned
        //endregion

        //region Methods
        /**
         * Runs the thread
         */
        @Override
        public void run()
        {
            System.out.println("Producer thread [TID=" + this.iTID + "] starts executing.");
            for (int i = 0; i < iThreadSteps; i++)
            {
                //Insert your code in the following:
                mutex.Wait();
                try
                {
                    this.block = stack.pick();
                    stack.push(++this.block);
                    System.out.println("Producer thread [TID=" + this.iTID + "] pushes character =" + this.block);
                }
                catch (Exception e)
                {
                    System.out.println(e.getClass().toString() + " was caught: " + e.getMessage());
                    e.printStackTrace();
                }
                mutex.Signal();
                //End inserted code
            }
            System.out.println("Producer thread [TID=" + this.iTID + "] terminates.");
        }
        //endregion
    }

    /*
     * CharStackProber thread class
     */
    static class CharStackProber extends BaseThread
    {
        //region Methods
        /**
         * Runs the thread
         */
        @Override
        public void run()
        {
            System.out.println("CharStackProber thread [TID=" + this.iTID + "] starts executing.");
            for (int i = 0; i < 2 * iThreadSteps; i++)
            {
                // Insert your code in the following. Note that the stack state must be
                mutex.Wait();
                System.out.println(stack.toString());
                mutex.Signal();
            }
        }
        //endregion
    }
    //endregion

    //region Constants
    private static final int NUM_ACQREL = 4; // Number of Producer/Consumer threads
    private static final int NUM_PROBERS = 1; // Number of threads dumping stack
    //endregion

    //region Static fields
    private static CharStack stack = new CharStack();
    private static int iThreadSteps = 3; // Number of steps they take
    // Semaphore declarations. Insert your code in the following:
    private static Semaphore mutex = new Semaphore(1);
    //End semaphore declarations
    //endregion

    //region Main
    /**
     * Main method, program entry point
     * @param argv Program arguments, unused
     */
    public static void main(String[] argv)
    {
        // Some initial stats...
        try
        {
            System.out.println("Main thread starts executing.");
            System.out.println("Initial value of top = " + stack.getTop() + ".");
            System.out.println("Initial value of stack top = " + stack.pick() + ".");
            System.out.println("Main thread will now fork several threads.");
        }
        catch (CharStackEmptyException e)
        {
            System.out.println("Caught exception: StackCharEmptyException");
            System.out.println("Message : " + e.getMessage());
            System.out.println("Stack Trace : ");
            e.printStackTrace();
        }
        //The birth of threads
        Consumer ab1 = new Consumer();
        Consumer ab2 = new Consumer();
        System.out.println("Two Consumer threads have been created.");
        Producer rb1 = new Producer();
        Producer rb2 = new Producer();
        System.out.println("Two Producer threads have been created.");
        CharStackProber csp = new CharStackProber();
        System.out.println("One CharStackProber thread has been created.");
        //start executing
        ab1.start();
        rb1.start();
        ab2.start();
        rb2.start();
        csp.start();
        //Wait by here for all forked threads to die
        try
        {
            ab1.join();
            ab2.join();
            rb1.join();
            rb2.join();
            csp.join();
            // Some final stats after all the child threads terminated...
            System.out.println("System terminates normally.");
            System.out.println("Final value of top = " + stack.getTop() + ".");
            System.out.println("Final value of stack top = " + stack.pick() + ".");
            System.out.println("Final value of stack top-1 = " + stack.getAt(stack.getTop() - 1) + ".");
        }
        catch (InterruptedException e)
        {
            System.out.println("Caught InterruptedException: " + e.getMessage());
            System.exit(1);
        }
        catch (Exception e)
        {
            System.out.println("Caught exception: " + e.getClass().getName());
            System.out.println("Message : " + e.getMessage());
            System.out.println("Stack Trace : ");
            e.printStackTrace();
        }
    }
    //endregion
}