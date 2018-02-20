package Sim;

public class InterfaceHasChangedMsg implements Event {

	int oldInterface;
	int newInterface;
	Node node;
	
	public InterfaceHasChangedMsg(int oldInterface,int newInterface, Node node) {
		this.oldInterface = oldInterface;
		this.newInterface = newInterface;
		this.node = node;
	}
	
	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}
	
	

}
