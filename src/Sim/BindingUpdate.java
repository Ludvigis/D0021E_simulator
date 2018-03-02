package Sim;

public class BindingUpdate implements Event {
	
	private NetworkAddr COA;
	private NetworkAddr HOA;
	private SimEnt from;
	
	public BindingUpdate(NetworkAddr HOA,NetworkAddr COA, SimEnt from){
		this.HOA = HOA;
		this.COA = COA;
		this.from = from;
		
	}
	
	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub

	}
	
	public SimEnt getEntity(){
		return from;
	}

	public NetworkAddr getCOA() {
		return COA;
	}
	
	public NetworkAddr getHOA() {
		return HOA;
	}

}
