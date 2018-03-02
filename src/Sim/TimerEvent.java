package Sim;

// This class is used to schedule timer events when for instance calling the 
// recv method in SimEnt classes due to other reasons than that a message has
// arrived.

public class TimerEvent implements Event{
	
	boolean RS;
	
	public TimerEvent(boolean RA){
		this.RS = true;
	}
	
	public TimerEvent(){
		this.RS = false;
	}
	
	public boolean isRS(){
		return RS;
	}
			
	public void entering(SimEnt locale)
	{
	}
}
