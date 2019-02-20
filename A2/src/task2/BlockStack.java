package task2;

/**
 * Class task1.BlockStack
 * Implements character block stack and operations upon it.
 *
 * $Revision: 1.4 $
 * $Last Revision Date: 2019/02/02 $
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca;
 * Inspired by an earlier code by Prof. D. Probst

 */
class BlockStack
{
	/**
	 * Full stack exception
	 */
	class StackFullException extends Exception { }

	/**
	 * Empty stack exception
	 */
	class StackEmptyException extends Exception { }

	/**
	 * # of letters in the English alphabet + 2
	 */
	public static final int MAX_SIZE = 28;

	/**
	 * Default stack size
	 */
	public static final int DEFAULT_SIZE = 6;

	/**
	 * Current size of the stack
	 */
	private int iSize = DEFAULT_SIZE;
	public int getISize()
	{
		return this.iSize;
	}


	/**
	 * Current top of the stack
	 */
	private int iTop  = 3;
	public int getITop()
	{
		return this.iTop;
	}

	private int accessCounter;
	public int getAccessCounter()
	{
		return this.accessCounter;
	}

	public boolean isEmpty()
	{
		return this.iTop == -1;
	}


	/**
	 * stack[0:5] with four defined values
	 */
	private char[] acStack = new char[] { 'a', 'b', 'c', 'd', '$', '$' };
	//Chris - Added stack getter
	public char[] getAcStack()
	{
		return this.acStack;
	}

	/**
	 * Default constructor
	 */
	public BlockStack() { }

	/**
	 * Supplied size
	 */
	public BlockStack(final int piSize)
	{
		if(piSize != DEFAULT_SIZE)
		{
			this.acStack = new char[piSize];

			// Fill in with letters of the alphabet and keep
			// 2 free blocks
			for(int i = 0; i < piSize - 2; i++)
				this.acStack[i] = (char)('a' + i);

			this.acStack[piSize - 2] = this.acStack[piSize - 1] = '$';

			this.iTop = piSize - 3;
            this.iSize = piSize;
		}
	}

	/**
	 * Picks a value from the top without modifying the stack
	 * @return top element of the stack, char
	 */
	public char pick()
	{
		this.accessCounter++;
		return this.acStack[this.iTop];
	}

	/**
	 * Returns arbitrary value from the stack array
	 * @return the element, char
	 */
	public char getAt(final int piPosition)
	{
		this.accessCounter++;
		return this.acStack[piPosition];
	}

	/**
	 * Standard push operation
	 */
	public void push(final char pcBlock) throws StackFullException
	{
		this.accessCounter++;
		//Chris - Added full stack exception
		if (this.iTop == this.iSize)
		{
			throw new StackFullException();
		}
		//Chris - handled empty stack push
		this.acStack[++this.iTop] = isEmpty() ? 'a' : pcBlock;
		System.out.println("Successfully pushed to the stack");
	}

	/**
	 * Standard pop operation
	 * @return ex-top element of the stack, char
	 */
	public char pop() throws StackEmptyException
	{
		this.accessCounter++;
		//Chris - Added empty stack exception
		if (isEmpty())
		{
			throw new StackEmptyException();
		}
		char cBlock = this.acStack[this.iTop];
		this.acStack[this.iTop--] = '$'; // Leave prev. value undefined
		System.out.println("Successfully popped from the stack");
		return cBlock;
	}
}

// EOF