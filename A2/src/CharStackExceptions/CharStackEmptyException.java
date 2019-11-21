package CharStackExceptions;

/**
 * Stack empty exception
 */
public class CharStackEmptyException extends Exception
{
    //region Constructors
    /**
     * Creates a new CharStackEmptyException
     */
    public CharStackEmptyException()
    {
        super("Char Stack is empty.");
    }
    //endregion
}
