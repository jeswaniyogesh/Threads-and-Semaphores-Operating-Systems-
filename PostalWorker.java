//package Project2;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

//-- Postal worker class to serve customers
public class PostalWorker implements Runnable {
	
	private int num;
	
	private Semaphore max_capacity;
	private Semaphore service_counter;
	private Semaphore cust_ready;
	private ArrayList<Semaphore> service_finished;
	private Semaphore leave_counter;
	private Semaphore mutex1;
	private Semaphore mutex3;
	
	private int customer_id;
	private int customer_service;
	
	//-- constructor of postal worker
	PostalWorker(int num, Semaphore max_capacity, Semaphore service_counter, Semaphore cust_ready,
			      ArrayList<Semaphore> service_finished, Semaphore leave_counter, Semaphore mutex1,Semaphore mutex3
			       ){
		
		
		this.num=num;
		this.max_capacity=max_capacity;
		this.service_counter=service_counter;
		this.cust_ready=cust_ready;
		this.service_finished=service_finished;
		this.leave_counter=leave_counter;
		this.mutex1=mutex1;
		this.mutex3=mutex3;
		
		System.out.println("Postal worker"+" "+num+" "+"created");
		
		}

    //-- Thread will start from here
	public void run() {
		while(true){
			
			try {
				cust_ready.acquire();
			} catch (InterruptedException e) {}
			
			try {
				mutex1.acquire();
			} catch (InterruptedException e) {}
				
				this.customer_id=Postoffice.cust_id;
				this.customer_service=Postoffice.cust_service;
				System.out.println("Postal Worker"+" "+num+" "+"Serving customer"+" "+ this.customer_id);
				mutex3.release();
				mutex1.release();
				
				try{
					Thread.sleep(Postoffice.serviceSleep(this.customer_service));
				}catch(Exception e){
					e.printStackTrace();
				}
				
				System.out.println("Postal Worker"+" "+ num+" "+"finished Serving Customer"+" "+this.customer_id);
				
				service_finished.get(this.customer_id).release();
				
				try {
					leave_counter.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				service_counter.release();
		}
		
	}
	
}
