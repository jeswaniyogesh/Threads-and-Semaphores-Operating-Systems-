//package Project2;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;


//-- This class will Initialize Threads for Customer and Postal Workers. It will create threads, Initialize 
//-- Constructors and semaphores for each thread. 
public class Postoffice{
	
	
	public static int cust_id;
	public static int cust_service;
	
	    public static void main(String args[]){
		
		final int No_customers=50;
		final int No_max_capacity=10;
		final int No_postal_workers=3;
		int i=0;
		
		Semaphore max_capacity=new Semaphore(No_max_capacity,true);
		Semaphore service_counter=new Semaphore(No_postal_workers,true);
		Semaphore cust_ready=new Semaphore(0,true);
		Semaphore scales=new Semaphore(1,true);
		
		ArrayList<Semaphore> service_finished=new ArrayList<Semaphore>();
		
		
		// Initializing customers to its initial value.
		for(int j=0;j<No_customers;j++){
			service_finished.add(j, new Semaphore(0,true));
			}
		Semaphore leave_counter=new Semaphore (0,true);
		
		Semaphore mutex1=new Semaphore(3,true);
		Semaphore mutex2=new Semaphore(1,true);
		Semaphore mutex3=new Semaphore(0,true);
		
		//-- Starting Post office
		System.out.println("Simulating PostOffice with "+" "+ No_customers+" "+ " and "+" "+No_postal_workers+" "+"Postal workers");
		
		
		PostalWorker pw[]=new PostalWorker[No_postal_workers];
		Thread PostalThreads[]=new Thread[No_postal_workers];
		
		Customer cth[]=new Customer[No_customers];
		Thread customerThreads[]=new Thread[No_customers];
		
		//-- Initializing threads for postal workers
		for(i=0;i<No_postal_workers;++i){
			
			pw[i]=new PostalWorker(i, max_capacity, service_counter, cust_ready, service_finished, leave_counter,mutex1,mutex3);
			
			PostalThreads[i]=new Thread(pw[i]);
			PostalThreads[i].start();
		}
		
		// Initializing Threads for Customers
		for(i=0;i<No_customers;++i){
			
			cth[i]=new Customer(i,max_capacity, service_counter, cust_ready, service_finished, leave_counter,mutex1,mutex2,mutex3,scales);
			customerThreads[i]=new Thread(cth[i]);
			customerThreads[i].start();
		}
		
		//Terminating Customers after getting served
		for(i=0;i<No_customers;++i){
			
			try {
				customerThreads[i].join();
				System.out.println("Joined Customer"+i+".");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.exit(0);
		
		
		
	}
	
	//-- used while printing 
	public static String serviceDisplay(int s)
	{
		
			switch(s)
			{
				case 1: return "to buy stamps";
				
				case 2: return "to mail a letter";
				
				case 3: return "to mail a package";
				
				
			}
			return null;	
	
	

}
	
	//-- Used while printing after being served.
	public static String serviceend(int s)
	{
		
			switch(s)
			{
				case 1: return "buying stamps";
				
				case 2: return "mailing a letter";
				
				case 3: return "mailing a package";
				
				
			}
			return null;	
	
	

}
	
	//-- It will sleep after each task served.
	public static int serviceSleep(int s)
	{
		switch(s)
		{
			case 0: return 500;  // This is the arrival rate of customers.
			
			case 1: return 1000; // This is buying stamps.
				
			case 2: return 1500; // This is mailing a letter.
			
			case 3: return 2000; // This is mailing a package.
			
		}
		
		return 10000; // This is the default sleep which will portray an error in the system.
	}
	
	}