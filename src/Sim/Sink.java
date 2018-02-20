package Sim;

public class Sink {
	
	
	private double totalDelay;
	private double avgDelay;
	private Double maxDelay = Double.NaN;
	private Double minDelay = Double.NaN;
	private int numberOfMsg;
	private double transitTime;
	private Double lastPacketTransitTime = Double.NaN;
	private Double lastPacketResvTime;
	private double jitterSum;
	private double delta;
	private double sendDelay;
	
	public void recvMessage(Message msg){
		
		//Calculate average jitter for packets sent could add max and min jitter...
		double currentTime = SimEngine.getTime();
		if(lastPacketTransitTime.isNaN()){
			lastPacketTransitTime = currentTime;
			lastPacketResvTime = currentTime;
		}else{
			transitTime = Math.abs(lastPacketResvTime - currentTime);
			lastPacketResvTime = currentTime;
			delta = Math.abs(lastPacketTransitTime - transitTime); 
			jitterSum += delta;
			lastPacketTransitTime = transitTime;
			
			
		}
		
		
		
		// Delay in transit time.
		sendDelay = currentTime - msg.getTimeSent();
		//increment number of messages/packets received.
		++numberOfMsg;
		//Add delay to total delay
		totalDelay += sendDelay;
		//Calculate average delay
		avgDelay = totalDelay / (double)numberOfMsg;
		

		if(sendDelay > maxDelay || maxDelay.isNaN())
			maxDelay = sendDelay;
		if(sendDelay < minDelay || minDelay.isNaN())
			minDelay = sendDelay;
	}
	
	public void printStatistics() {
		System.out.println("Average Delay on package received: " + avgDelay);
		System.out.println("Max Delay on package received: " + maxDelay);
		System.out.println("Min Delay on package received: " + minDelay);
		System.out.println("Average Jitter node to node: " + jitterSum/numberOfMsg);
	}
}
