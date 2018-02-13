package Sim;

public class Sink {
	
	private double estimatedJitter;
	private double totalDelay;
	private double avgDelay;
	private double maxDelay = Double.MIN_VALUE;
	private double minDelay = Double.MAX_VALUE;
	private int numberOfMsg = 0;
	
	
	public void recvMessage(Message msg){
		double sentTime = msg.getTimeSent();
		double currentTime = SimEngine.getTime();
		//TODO Estimated jitter ...
		double delay = currentTime - sentTime;
		
		++numberOfMsg;
		totalDelay += delay;
		avgDelay = totalDelay / (double)numberOfMsg;
		
		if( Object.class.equals(null)||delay > maxDelay)
			maxDelay = delay;
		if(delay < minDelay)
			minDelay = delay;
	}
	
	public void printStatistics() {
		System.out.println("Average Delay: " + avgDelay);
		System.out.println("Max Delay: " + maxDelay);
		System.out.println("Min Delay: " + minDelay);
	}
}
