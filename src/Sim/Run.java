package Sim;

// An example of how to build a topology and starting the simulation engine

public class Run {
	public static void main (String [] args)
	{
 		//Creates two links

		Link link1 = new Link();
 		Link link2 = new Link();
 		Link link3 = new Link();

		
		// Create two end hosts that will be
		// communicating via the router
		Node host1 = new Node(1,1, new Poisson(10));
		Node host2 = new Node(2,1, new Normal(100,10));

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
		
		
		r1.connectInterface(0, link1, r2);
		r1.connectInterface(1, link2, host1);
		r2.connectInterface(1, link3, host2);
		r2.connectInterface(0, link1, r1);
		//Change to interface 5 after 10 messages
		host2.changeInterfaceAfterRecvMsgs(10, 7);
		
		// Generate some traffic
		// host1 will send 3 messages with time interval 5 to network 2, node 1. Sequence starts with number 1
		host1.StartSending(2, 1, 15, 1); 
		// host2 will send 2 messages with time interval 10 to network 1, node 1. Sequence starts with number 10
		host2.StartSending(1, 1, 9, 10); 
		
		
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
		
		System.out.println("Packet loss: "+ LossyLink.packetloss);
		
		System.out.println("******************************");
		host2.printStatistics();
		host1.printStatistics();


	}
}
