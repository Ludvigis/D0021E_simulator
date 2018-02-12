package Sim;

public class Sink {
	
	private double estimatedJitter;
	private double avgDelay;
	private double maxDelay;
	private double minDelay;
	private int numberOfMsg;
	
	
	public void recvMessage(Message msg){
	
		SimEngine.getTime();
	}
	
	public void printStatistics() {
		
	}
}
