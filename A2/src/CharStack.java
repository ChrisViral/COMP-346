import CharStackExceptions.*;

import java.util.Arrays;

/**
 * CharStack object
 */
public class CharStack
{
    //region Constants
    /**
     * Minimum stack size
     */
    private static final int MIN_SIZE = 7;
    /**
     * Maximum stack size (size of English alphabet plus 6)
     */
    private static final int MAX_SIZE = 32;
    /**
     * Default stack size
     */
    private static final int DEFAULT_SIZE = 10;
    /**
     * Character representing an empty spot in the stack
     */
    private static final char EMPTY_CHAR = '$';
    //endregion

    //region Fields
    private int iSize = DEFAULT_SIZE;
    private int iTop = 3; // stack[0:9] with four defined values
    private char[] aCharStack = { 'a', 'b', 'c', 'd', EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR, EMPTY_CHAR };
    //endregion

    //region Getters
    public int getSize()
    {
        return this.iSize;
    }

    int getTop()
    {
        return this.iTop;
    }
    //endregion

    //region Constructors
    /**
     * Create a new CharStack
     */
    public CharStack() { }

    /***
     * Creates a new CharStack with the specified size
     * @param piSize Size of the Char stack
     * @throws CharStackInvalidSizeException If the stack size is invalid
     */
    public CharStack(int piSize) throws CharStackInvalidSizeException
    {
        if (piSize < MIN_SIZE || piSize > MAX_SIZE)  { throw new CharStackInvalidSizeException(piSize); }

        if (piSize != DEFAULT_SIZE)
        {
            this.aCharStack = new char[piSize];
            // Fill in with letters of the alphabet and keep
            // 6 free blocks
            for (int i = 0; i < piSize - 6; i++)
                this.aCharStack[i] = (char) ('a' + i);
            for (int i = 1; i <= 6; i++)
                this.aCharStack[piSize - i] = EMPTY_CHAR;
            this.iTop = piSize - 7;
            this.iSize = piSize;
        }
    }
    //endregion

    //region Methods
    /**
     * If the stack is empty
     * @return True when the stack has no elements, false otherwise
     */
    public boolean isEmpty()
    {
        return this.iTop == -1;
    }

    /**
     * If the stack is full
     * @return True if the stack is full, false otherwise
     */
    public boolean isFull()
    {
        return this.iTop == this.iSize - 1;
    }

    /**
     * Picks a value from the top without modifying the stack
     * @throws CharStackEmptyException If the stack is empty and no value can be returned
     */
    public char pick() throws CharStackEmptyException
    {
        if (isEmpty()) { throw new CharStackEmptyException(); }

        return this.aCharStack[this.iTop];
    }

    /**
     * Returns arbitrary value from the stack array
     * @param piPosition Position in the stack to get the value at
     * @throws CharStackInvalidAccessException If the supplied position is invalid
     */
    public char getAt(int piPosition) throws CharStackInvalidAccessException
    {
        if (piPosition < 0 || piPosition >= this.iSize) { throw new CharStackInvalidAccessException(); }

        return this.aCharStack[piPosition];
    }

    /**
     * Standard push operation
     * @param pcChar Character to push
     * @throws CharStackFullException If the stack is full and nothing can be pushed to it
     */
    public void push(char pcChar) throws CharStackFullException
    {
        if (isFull()) { throw new CharStackFullException(); }

        this.aCharStack[++this.iTop] = pcChar;
    }

    /**
     * Standard pop operation
     * @throws CharStackEmptyException If the stack is empty and nothing can be popped
     */
    public char pop() throws CharStackEmptyException
    {
        if (isEmpty()) { throw new CharStackEmptyException(); }

        char cChar = this.aCharStack[this.iTop];
        this.aCharStack[this.iTop--] = EMPTY_CHAR; // Leave prev. value undefined
        return cChar;
    }

    /**
     * Prints the current stack with the correct format
     * @return Printed version of the CharStack
     */
    @Override
    public String toString()
    {
        //Join correctly with the right format
        StringBuilder sb = new StringBuilder("Stack S = ([");
        sb.append(this.aCharStack[0]);
        for (int i = 1; i < this.iSize; i++)
        {
            sb.append("],[").append(this.aCharStack[i]);
        }
        sb.append("])");
        return sb.toString();
    }
    //endregion
}