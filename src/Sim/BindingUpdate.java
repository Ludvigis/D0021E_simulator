package Sim;

public class BindingUpdate implements Event {
	
	private NetworkAddr COA;
	private NetworkAddr HOA;
	
	public BindingUpdate(NetworkAddr HOA,NetworkAddr COA){
		this.HOA = HOA;
		this.COA = COA;
		
	}
	
	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub

	}

	public NetworkAddr getCOA() {
		return COA;
	}
	
	public NetworkAddr getHOA() {
		return HOA;
	}

}
