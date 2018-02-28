package Sim;

//Used to message router of an disconnect of a node.
public class DisconnectEvent implements Event {
	
	private Node node;
	
	DisconnectEvent(Node node){
		this.node = node;
	}
	
	//returns node who triggered event.
	public Node getNode(){
		return node;
	}


	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub
		
	}

}
