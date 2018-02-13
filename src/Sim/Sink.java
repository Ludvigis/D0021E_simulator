package Sim;

public class Sink {
	
	private double estimatedJitter;
	private double totalDelay;
	private double avgDelay;
	private Double maxDelay = Double.NaN;
	private Double minDelay = Double.NaN;
	private int numberOfMsg = 0;
	
	
	public void recvMessage(Message msg){
		double sentTime = msg.getTimeSent();
		double currentTime = SimEngine.getTime();
		//TODO Estimated jitter ...
		double delay = currentTime - sentTime;
		
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
	}
}
