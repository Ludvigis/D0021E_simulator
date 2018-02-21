package Sim;

public class InterfaceChangeACK implements Event{
	private int newInterface;
	
	
	public InterfaceChangeACK(int newInterface) {
		this.newInterface = newInterface;
	}
	
	public int getNewInterface() {
		return this.newInterface;
	}

	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}

}
