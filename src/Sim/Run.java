package Sim;

// An example of how to build a topology and starting the simulation engine

public class Run {
	public static void main (String [] args)
	{
 		//Creates two links

		Link link1 = new Link();
 		Link link2 = new Link();
 		Link link3 = new Link();
 		Link link4 = new Link();
 		Link link5 = new Link();

		
		// Create two end hosts that will be
		// communicating via the router
		Node host1 = new Node(1,100, new Normal(10,2));
		Node host2 = new Node(2,1, new Normal(10,2));
		
		//set time from which move to RS is sent
		host1.setRSTime(60);
		
		//Connect links to hosts
		host1.setPeer(link2);
		host2.setPeer(link3);
		
		// Creates as router and connect
		// links to it. Information about 
		// the host connected to the other
		// side of the link is also provided
		// Note. A switch is created in same way using the Switch class
		Router r1 = new Router(1,10);
		Router r2 = new Router(2,10);
		Router r3 = new Router(3,10);
		
		//setup RA how long and interval;
		r1.setupRA(200, 35);
		r2.setupRA(200, 50);
		r3.setupRA(200, 45);
		
		//Setup switches after routers.
		//Switch s1 = new Switch(5);
		//Switch s2 = new Switch(5);
		
		//setup HA for nodes
		host1.setHA(r1);
		host2.setHA(r2);
		
		//setup connection between routers.
		r1.connectInterface(0, link1, r2);
		r1.connectInterface(1, link2, host1);
		r1.connectInterface(2, link5, r3);
		r2.connectInterface(1, link3, host2);
		r2.connectInterface(0, link1, r1);
		r2.connectInterface(2, link4, r3);
		r3.connectInterface(0, link4, r2);
		r3.connectInterface(1, link5, r1);
		
		// Generate some traffic
		// host1 will send 3 messages with time interval 5 to network 2, node 1. Sequence starts with number 1
		host1.StartSending(2, 1, 0, 1); 
		// host2 will send 2 messages with time interval 10 to network 1, node 1. Sequence starts with number 10
		host2.StartSending(1, 100, 30, 100); 
		
		//Triggers a moveEvent to router after specified time.
		host1.moveMobileNodeAfterTime(r3, 100);
		
		// Start the simulation engine and of we go!
		Thread t=new Thread(SimEngine.instance());
	
		t.start();
		
		try
		{
			t.join();
		}
		catch (Exception e)
		{
			System.out.println("The motor seems to have a problem, time for service?");
		}		
		
		
		System.out.println("****************STATS**************");
		host2.printStatistics();
		host1.printStatistics();


	}
}
