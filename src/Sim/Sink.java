package Sim;

public class Sink {
	
	
	private double totalDelay;
	private double avgDelay;
	private Double maxDelay = Double.NaN;
	private Double minDelay = Double.NaN;
	private int numberOfMsg;
	private double delta;
	private double lastPacketTime;
	private double jitterSum;
	private double delay;
	
	public void recvMessage(Message msg){
		
		//Calculate average jitter for packets sent.
		double currentTime = SimEngine.getTime();
		double tempDelta = currentTime - lastPacketTime;
		lastPacketTime = currentTime;
		jitterSum += Math.abs(delta - tempDelta) ;
		delta = tempDelta;
		
		// Delay in transit time.
		delay = currentTime - msg.getTimeSent();
		//increment number of messages/packets received.
		++numberOfMsg;
		//Add delay to total delay
		totalDelay += delay;
		//Calculate average delay
		avgDelay = totalDelay / (double)numberOfMsg;
		

		if(delay > maxDelay || maxDelay.isNaN());
			maxDelay = delay;
		if(delay < minDelay || minDelay.isNaN())
			minDelay = delay;
	}
	
	public void printStatistics() {
		System.out.println("Average Delay on package received: " + avgDelay);
		System.out.println("Max Delay on package received: " + maxDelay);
		System.out.println("Min Delay on package received: " + minDelay);
		System.out.println("Average Jitter node to node: " + jitterSum/numberOfMsg);
	}
}
