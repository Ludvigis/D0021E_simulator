package Sim;

public class BindingACK implements Event {

	SimEnt _to;
	
	public BindingACK(SimEnt to){
		_to = to;
	}
	
	public SimEnt getEntity(){
		return _to;
	}
	
	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub

	}

}
