package Threading;

/**
 * Semaphore object used for synchronization
 */
public class Semaphore
{
    //region Fields
    private int value;
    //endregion

    //region Constructors
    /**
     * Initializes a new Semaphore with a value of zero
     */
    public Semaphore()
    {
        this(0);
    }

    /**
     * Initializes a new Semaphore with a given value
     * @param value Value of the new Semaphore
     */
    public Semaphore(int value)
    {
        //Make sure the value isn't negative
        this.value = Math.max(value, 0);
    }
    //endregion

    //region Methods
    /**
     * Wait operation of the Semaphore
     */
    public synchronized void Wait()
    {
        while (this.value <= 0)
        {
            try
            {
                wait();
            }
            catch(InterruptedException e)
            {
                System.out.println ("Semaphore::Wait() - caught InterruptedException: " + e.getMessage() );
                e.printStackTrace();
            }
        }
        this.value--;
    }

    /**
     * Signal operation of the Semaphore
     */
    public synchronized void Signal()
    {
        this.value++;
        notify();
    }

    /* Not really needed
    public synchronized void P()
    {
        this.Wait();
    }

    public synchronized void V()
    {
        this.Signal();
    }
    */
    //endregion
}