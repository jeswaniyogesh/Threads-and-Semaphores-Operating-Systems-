//package Project2;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

//Customers came for some work in Post office
public class Customer implements Runnable {
	
	public int num;
	public int cust_type;

	private Semaphore max_capacity;
	private Semaphore service_counter;
	private Semaphore cust_ready;
	private ArrayList<Semaphore> service_finished;
	private Semaphore leave_counter;
	private Semaphore mutex1;
	private Semaphore mutex2;
	private Semaphore mutex3;
	private Semaphore scales;
	
	//-- Constructor for constructor to initialize initial values
	Customer(int num, Semaphore max_capacity, Semaphore service_counter, Semaphore cust_ready,
		      ArrayList<Semaphore> service_finished, Semaphore leave_counter, Semaphore mutex1,
		       Semaphore mutex2,Semaphore mutex3,Semaphore scales){
	
	
	this.num=num;
	this.max_capacity=max_capacity;
	this.service_counter=service_counter;
	this.cust_ready=cust_ready;
	this.service_finished=service_finished;
	this.leave_counter=leave_counter;
	this.mutex1=mutex1;
	this.mutex2=mutex2;
	this.mutex3=mutex3;
	this.scales=scales;
	
	Random number=new Random();
	
	this.cust_type=number.nextInt(3)+1;
	
	System.out.println("Customer"+" "+num+" "+"created");
	
}

	// Customer Thread will start from here
	public void run() {
		try {
			max_capacity.acquire();
		} catch (InterruptedException e) {}
		
		try{
		Thread.sleep(Postoffice.serviceSleep(0)*(num+1));
		}catch (Exception e){}
		
		
		System.out.println("Customer"+" "+num+" "+"enters post office");
		
		try {
			service_counter.acquire();
		} catch (InterruptedException e) {}
			
		if(cust_type==3){
		scales.release();	
		
		System.out.println("Customer"+" "+num+" "+"asks Postal Worker "+" "+Postoffice.serviceDisplay(this.cust_type));
		
		try {
			scales.acquire();
		} catch (InterruptedException e) {}
			
		}else{
			System.out.println("Customer"+" "+num+" "+"asks Postal Worker "+" "+Postoffice.serviceDisplay(this.cust_type));
		}
		try {
			mutex2.acquire();
		} catch (InterruptedException e) {}
			
		
		try {
			mutex1.acquire();
		} catch (InterruptedException e) {}
			
		
		Postoffice.cust_id=this.num;
		Postoffice.cust_service=this.cust_type;
		
		cust_ready.release();
		
		mutex1.release();
		
		try {
			mutex3.acquire();
		} catch (InterruptedException e) {}
			
		
		mutex2.release();
		
		try {
			service_finished.get(this.num).acquire();
		} catch (InterruptedException e) {}
			
		
		System.out.println("Customer"+" "+num+" "+"finished"+" "+ Postoffice.serviceend(this.cust_type));
		
		leave_counter.release();
		
		System.out.println("Customer"+" "+num+" "+"leaves Post Office");
		
		max_capacity.release();
	}
}
