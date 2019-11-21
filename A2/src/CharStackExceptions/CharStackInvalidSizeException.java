package CharStackExceptions;

/**
 * Stack size invalid exception
 */
public class CharStackInvalidSizeException extends Exception
{
    //region Constructors
    /**
     * Creates a new CharStackInvalidSizeException
     */
    public CharStackInvalidSizeException()
    {
        super("Invalid stack size specified.");
    }

    /**
     * Creates a new CharStackInvalidSizeException with the specified stack size
     * @param piStackSize Stack size
     */
    public CharStackInvalidSizeException (int piStackSize)
    {
        super ("Invalid stack size specified: " + piStackSize);
    }
    //endregion
}
