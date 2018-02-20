package Sim;

// This class implements a simple router

public class Router extends SimEnt{

	private RouteTableEntry [] _routingTable;
	private int _interfaces;
	private int _now=0;

	// When created, number of interfaces are defined
	
	Router(int interfaces)
	{
		_routingTable = new RouteTableEntry[interfaces];
		_interfaces=interfaces;
	}
	
	public void printRouterTable() {
		for(int i = 0; i <_routingTable.length; i++) {
			System.out.println(i + " " +_routingTable[i]);
		}
	}
	
	// This method connects links to the router and also informs the 
	// router of the host connects to the other end of the link
	
	public void connectInterface(int interfaceNumber, SimEnt link, SimEnt node)
	{
		if (interfaceNumber<_interfaces)
		{
			_routingTable[interfaceNumber] = new RouteTableEntry(link, node);
		}
		else
			System.out.println("Trying to connect to port not in router");
		
		((Link) link).setConnector(this);
	}
	
	public int disconnectInterface(int networkaddress){
		Link routerInterfaceLink = (Link) getInterface(networkaddress);
		int oldInterface = 0;
		for(int i = 0 ; i < _interfaces; i++){
			if(_routingTable[i] !=  null) {
				if(_routingTable[i].link() ==  routerInterfaceLink){ _routingTable[i] = null; oldInterface = i;}
			}
		}
		
		routerInterfaceLink.removeConnector(this);
		return oldInterface;
	}

	// This method searches for an entry in the routing table that matches
	// the network number in the destination field of a messages. The link
	// represents that network number is returned
	
	private SimEnt getInterface(int networkAddress)
	{
		SimEnt routerInterface=null;
		for(int i=0; i<_interfaces; i++)
			if (_routingTable[i] != null)
			{
				if (((Node) _routingTable[i].node()).getAddr().networkId() == networkAddress)
				{
					routerInterface = _routingTable[i].link();
				}
			}
		return routerInterface;
	}
	
	
	// When messages are received at the router this method is called
	
	public void recv(SimEnt source, Event event)
	{
		if (event instanceof Message)
		{
			System.out.println("Router handles packet with seq: " + ((Message) event).seq()+" from node: "+((Message) event).source().networkId()+"." + ((Message) event).source().nodeId() );
			SimEnt sendNext = getInterface(((Message) event).destination().networkId());
			System.out.println("Router sends to node: " + ((Message) event).destination().networkId()+"." + ((Message) event).destination().nodeId());		
			send (sendNext, event, _now);
	
		}
		if (event instanceof InterfaceChange) {
			
			this.printRouterTable();
			InterfaceChange msg = (InterfaceChange)event;
			int oldInterface = disconnectInterface(msg.getNetworkId());
			connectInterface(msg.getNewInterface(),msg.getLink(),msg.getNode());
			
			System.out.println("Changed interface");
			this.printRouterTable();
			//send location changed to peer
			send(msg.getLink(),new InterfaceHasChangedMsg(oldInterface, msg.getNewInterface(), msg.getNode()),_now);
		}
	}
}
