package Sim;

public class Sink {
	
	private double jitter;
	private double transit;
	private double totalDelay;
	private double avgDelay;
	private Double maxDelay = Double.NaN;
	private Double minDelay = Double.NaN;
	private int numberOfMsg = 0;
	
	
	public void recvMessage(Message msg){
		double sentTime = msg.getTimeSent();
		double currentTime = SimEngine.getTime();
		double delay = currentTime - sentTime;
		
		double delta = delay - this.transit;
		this.transit = delay;
		delta = Math.abs(delta);
		this.jitter += (1.0d/16.0d)* (delta - this.jitter);		// Update estimated jitter continuously 
		
		++numberOfMsg;
		totalDelay += delay;
		avgDelay = totalDelay / (double)numberOfMsg;
		
		if(delay > maxDelay || maxDelay.isNaN());
			maxDelay = delay;
		if(delay < minDelay || minDelay.isNaN())
			minDelay = delay;
	}
	
	public void printStatistics() {
		System.out.println("Average Delay: " + avgDelay);
		System.out.println("Max Delay: " + maxDelay);
		System.out.println("Min Delay: " + minDelay);
		System.out.println("Estimated Jitter node to node: " + jitter);
	}
}
