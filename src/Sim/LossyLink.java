package Sim;

import java.util.Random;

public class LossyLink extends Link{
	int delay;
	double probLoss, jitter, transit;
	private SimEnt _connectorA=null;
	private SimEnt _connectorB=null;
	private Random rnd;
	static int packetloss = 0;
	
	public LossyLink(int delay,int jitter,double probLoss){
		super();
		this.delay = delay;
		this.probLoss = probLoss;
		this.delay = delay;
		this.rnd = new Random();
	}
	
	@Override
	public void setConnector(SimEnt connectTo)
	{
		if (_connectorA == null) 
			_connectorA=connectTo;
		else
			_connectorB=connectTo;
	}
	
	@Override
	public void recv(SimEnt src, Event ev)
	{
		double delay = getDelay();								// The delay is the simulated transit time over the lossy link.
		double delta = delay - this.transit;					// Delta is the difference in delay from previous packet sent.
		this.transit = delay;									// Update saved value for previous packet.
		delta = Math.abs(delta);
		this.jitter += (1.0d/16.0d)* (delta - this.jitter);		// Update estimated jitter continuously 
		
		if (ev instanceof Message)
		{
			if(rnd.nextDouble() <= probLoss){
				System.out.println("Packet loss seq: " + ((Message) ev).seq()); // Print when packet is dropped  
																				// and its sequence number.
				
				LossyLink.packetloss++;											// Update number of packet dropped over the lossy link(s);
				
				return;															// Just return when packet is dropped no need -
																				// to place new event in priority queue.
			} 
			
			
			System.out.println("Link recv msg, passes it through");
			if (src == _connectorA)
			{
				send(_connectorB, ev, delay);
			}
			else
			{
				send(_connectorA, ev, delay);
			}
			System.out.println("Estimated Jitter: " +this.jitter );  // prints the estimated jitter continuously. 
		}
		
		
		
	}
	
	private double getDelay(){		// Returns a random number between zero and delay passed as argument. 
		 return rnd.nextInt(delay); 
	}

}
