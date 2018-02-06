package Sim;

import java.util.Random;

public class LossyLink extends Link {
	private SimEnt _connectorA = null;
	private SimEnt _connectorB = null;
	private Random rnd;
	private int delay;
	private double probLoss;
	private double transit;
	private double jitter;
	private int numPacketLost;

	LossyLink(int delay, double jitter, double probLoss) {

		this.delay = delay;
		this.probLoss = probLoss;
		this.jitter = jitter;
		rnd = new Random();
	}

	@Override
	public void recv(SimEnt src, Event ev) {
		
		double delay = getDelay();
		double d = Math.abs(delay - this.transit); // explain d 
		
		this.transit = delay;
		this.jitter += (1.0/16.0) * (d - this.jitter);
		System.out.println("Estimated Jitter: " + this.jitter);
		
		if (ev instanceof Message) {
			
			if (rnd.nextDouble() <= probLoss) {
				System.out.println("Packet loss seq: " + ((Message) ev).seq() + " number of packets lost: " + ++numPacketLost);
				return;
			}
	
			System.out.println("Lossy Link recv msg, passes it through");
			if (src == _connectorA) {
				send(_connectorB, ev, delay);
			} else {
				send(_connectorA, ev, delay);
			}
		}
	}

	@Override
	public void setConnector(SimEnt connectTo) {
		if (_connectorA == null)
			_connectorA = connectTo;
		else
			_connectorB = connectTo;
	}
	
	private int getDelay() {
		return rnd.nextInt(delay + 1);
	}
	
	
	

}
