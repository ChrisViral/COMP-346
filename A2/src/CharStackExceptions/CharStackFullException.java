package CharStackExceptions;

/**
 * Stack full exception
 */
public class CharStackFullException extends Exception
{
    //region Constructors
    /**
     * Creates a new CharStackFullException
     */
    public CharStackFullException()
    {
        super ("Char Stack has reached its capacity of CharStack.MAX_SIZE.");
    }
    //endregion
}
