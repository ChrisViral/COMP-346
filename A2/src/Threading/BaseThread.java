package Threading;

/**
 * Consumer/Producer base tread
 */
public class BaseThread extends Thread
{
    //region Static fields
    private static int iNextTID = 1; // Preserves value across all instances
    //endregion

    //region Fields
    protected int iTID;
    //endregion

    //region Constructors
    /**
     * Creates a new BaseThread with the next associated Thread ID
     */
    public BaseThread()
    {
        this.iTID = iNextTID;
        iNextTID++;
    }

    /**
     * Creates a new BaseThread with the specified Thread ID
     * @param piTID Thread ID
     */
    public BaseThread(int piTID)
    {
        this.iTID = piTID;
    }
    //endregion
}
