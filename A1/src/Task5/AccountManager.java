package Task5;
/**
 * Class AccountManager
 * Implements account manager that twists depositor and withdrawal threads.
 *
 * @author Malek Barhoush, mbarhoush@hotmail.com;  
 * 
 *
 * $Revision: 1.0 $
 * $Last Revision Date: 2012/09/03    
 */

@SuppressWarnings("DuplicatedCode")
public class AccountManager
{
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

		Account[] account = new Account[10];
		Depositor[] deposit = new Depositor[10];
		Withdrawer[] withdraw = new Withdrawer[10];

		// The birth of  10 accounts
		account[0] = new Account(1234, "Mike", 1000);
		account[1] = new Account(2345, "Adam", 2000);
		account[2] = new Account(3456, "Linda", 3000);
		account[3] = new Account(4567, "John", 4000);
		account[4] = new Account(5678, "Rami", 5000);
		account[5] = new Account(6789, "Lee", 6000);
		account[6] = new Account(7890, "Tom", 7000);
		account[7] = new Account(8901, "Lisa", 8000);
		account[8] = new Account(9012, "Sam", 9000);
		account[9] = new Account(4321, "Ted", 10000);

		// The birth of 10 depositors
		//Seriously, use loops guys
		for (int i = 0; i < 10; i++)
		{
			deposit[i] = new Depositor(account[i]);
		}

		// The birth of  10 withdraws

		for (int i = 0; i < 10; i++)
		{
			withdraw[i] = new Withdrawer(account[i]);
		}

		System.out.println("Print initial account balances");
		// Print initial account balances
		for (int i = 0; i < 10; i++)
			System.out.println(account[i]);

		// Get start time in milliseconds 
		long start = System.currentTimeMillis();

		System.out.println("Depositor and Withdrawal threads have been created");
		/*
		 * Interleave all threads
		 */
		for (int i = 0; i < 10; i++)
		{
			deposit[i].start();
			withdraw[i].start();
		}


		for (int i = 0; i < 10; i++)
		{
			try
			{
				deposit[i].join();
				withdraw[i].join();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// Get elapsed time in milliseconds 
		long elapsedTimeMillis = System.currentTimeMillis() - start;

		System.out.println("Print final account balances after all the child thread terminated...");
		// Print final account balances after all the child thread terminated...	
		for (int i = 0; i < 10; i++)
			System.out.println(account[i]);
		// Get elapsed time in seconds 
		float elapsedTimeSec = elapsedTimeMillis / 1000F;

		System.out.println("Elapsed time in milliseconds " + elapsedTimeMillis);
		System.out.println("Elapsed time in seconds is " + elapsedTimeSec);

		//  Get elapsed time in minutes 
		float elapsedTimeMin = elapsedTimeMillis / (60 * 1000F);
		// Get elapsed time in hours 
		float elapsedTimeHour = elapsedTimeMillis / (60 * 60 * 1000F);
		// Get elapsed time in days 
		float elapsedTimeDay = elapsedTimeMillis / (24 * 60 * 60 * 1000F);

	}
}