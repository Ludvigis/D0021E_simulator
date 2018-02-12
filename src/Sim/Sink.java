package Sim;

public class Sink {
	
	private double estimatedJitter;
	private double totalDelay;
	private double avgDelay;
	private double maxDelay;
	private double minDelay = Double.MAX_VALUE;
	private int numberOfMsg = 0;
	
	
	public void recvMessage(Message msg){
		double sentTime = msg.getTimeSent();
		double currentTime = SimEngine.getTime();
		
		System.out.println("Curr time  :" + currentTime + "    " + "sentTime : " + sentTime);
		double delay = currentTime - sentTime;
		System.out.println("Delay:  "   +  delay);
		
		++numberOfMsg;
		totalDelay += delay;
		System.out.println("Total delay "  + totalDelay);
		avgDelay = totalDelay / (double)numberOfMsg;
		
		if(delay > maxDelay)
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
