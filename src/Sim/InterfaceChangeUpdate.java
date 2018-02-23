package Sim;

public class InterfaceChangeUpdate implements Event{
	
	private int newInterface;
	private NetworkAddr _destination;
	
	public InterfaceChangeUpdate(NetworkAddr addr, int newInterface) {
		this._destination = addr;
		this.newInterface = newInterface;
	}
	
	public int getNewInterface() {
		return this.newInterface;
	}
	
	public NetworkAddr destination()
	{
		return _destination; 
	}

	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}

}
