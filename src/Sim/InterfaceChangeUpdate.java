package Sim;

public class InterfaceChangeUpdate implements Event{
	private Link link;
	private int newInterface;
	
	public InterfaceChangeUpdate(Link link, int newInterface) {
		this.link = link;
		this.newInterface = newInterface;
	}
	
	public int getNewInterface() {
		return this.newInterface;
	}
	
	public Link getLink() {
		return this.link;
	}

	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}

}
