package Sim;

import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;

// This class implements a simple router

public class Router extends SimEnt{

	private RouteTableEntry [] _routingTable;
	private int _interfaces;
	private int _now=0;
	private HomeAgent ha;
	private int networkID;

	// When created, number of interfaces are defined
	
	Router(int networkID,int interfaces)
	{
		this.ha = new HomeAgent();
		_routingTable = new RouteTableEntry[interfaces];
		_interfaces=interfaces;
		this.networkID = networkID;
		
		//starts the "timer" on the router
		send(this,new TimerEvent(),_now);		//TODO maybe break out to new method.
	}
	
	public void printRouterTable() {
		for(int i = 0; i <_routingTable.length; i++) {
			if(_routingTable[i]!=null) {
				if(_routingTable[i].node() instanceof Node){
					System.out.println("*** Node: " +((Node)_routingTable[i].node()).getAddr().networkId() + "." + ((Node)_routingTable[i].node()).getAddr().nodeId() + " router interface:" + i);
				}else if(_routingTable[i].node() instanceof Router){
					System.out.println("*** Router: " +((Router)_routingTable[i].node()).getNetworkID()  + " router interface:" + i);
				}
				
			}
			
		}
	}
	
	// This method connects link and node to the specified interface 
	
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
	// Disconnect interface specified by networkaddress
	
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
		for(int i=0; i<_interfaces; i++){
			if (_routingTable[i] != null)
			{	
				
				if(_routingTable[i].node() instanceof Node){
					if (((Node) _routingTable[i].node()).getAddr().networkId() == networkAddress)
					{
						return _routingTable[i].link();
					}
				}
				else if(_routingTable[i].node() instanceof Router){
					if (((Router) _routingTable[i].node()).getNetworkID() == networkAddress)
					{
						return _routingTable[i].link();
					}
				}

			}
		}
		return routerInterface;
	}
	
	private int getNetworkID(){
		return this.networkID;
	}
	
	private void sendRouterAdvToAllConnInterfaces() {
		System.out.println("Router "+networkID + "Sending Router advertisement");
		for(int i=0; i<_interfaces; i++){
			if (_routingTable[i] != null)
			{	
				send(_routingTable[i].link(),new RouterAdvertisement(),_now);

			}
		}
	}
	
	
	// When messages are received at the router this method is called
	private int sentAdvertisements = 0;
	public void recv(SimEnt source, Event event)
	{
		if(event instanceof TimerEvent) {
			if(sentAdvertisements < 20) {
				++sentAdvertisements;
				sendRouterAdvToAllConnInterfaces();
				send(this,new TimerEvent(),10);		//TODO use variable for delay...
			}
			
		}
		
		if(event instanceof RouterSolicitation) {
			System.out.println("Router "+networkID + "Received solicitation");
			send(source,new RouterAdvertisement(),_now);
		}
		
		if (event instanceof Message)
		{
			handleMessage((Message)event);
		}
		//router receives interface change msg and carries out the change and sends ACK msg to node.
		if (event instanceof InterfaceChange) {
				handleInterfaceChange((InterfaceChange)event);
		}
		
		// Update message node to node communication just forward.
		if(event instanceof InterfaceChangeUpdate) {
			handleInterfaceChangeUpdate((InterfaceChangeUpdate)event);
		}
		if(event instanceof BindingUpdate){
			handleBindingUpdate((BindingUpdate)event);
		}
		if(event instanceof BindingACK){
			handleBindingACK((BindingACK)event);
		}
	}
	
	private void handleMessage(Message msg){
		SimEnt sendNext;
		//if homeagent has an entry for the destination address
		if(ha.inMapTable(msg.destination())){
			NetworkAddr COA = ha.getCOA(msg.destination());
			sendNext = getInterface(COA.networkId());
			System.out.println("Home Agent handles packet with seq: "+ msg.seq()+" from node: "+msg.source().networkId()+"." + msg.source().nodeId());
			System.out.println("Home Agent tunnels to: " + COA.networkId()+"." + COA.nodeId());	
		}else{
			sendNext = getInterface(msg.destination().networkId());
			System.out.println("Router on network: " + getNetworkID() + " handles packet with seq: " + msg.seq()+" from node: "+msg.source().networkId()+"." + msg.source().nodeId() );
			System.out.println("Router on network: " + getNetworkID() + " sends to node: " + msg.destination().networkId()+"." + msg.destination().nodeId());	
		}
		send (sendNext, msg, _now);	
	}
	
	private void handleInterfaceChange(InterfaceChange msg){
		this.printRouterTable();
		
		disconnectInterface(msg.getNetworkId());
		connectInterface(msg.getNewInterface(),msg.getLink(),msg.getNode());
		
		System.out.println("Changed interface");
		this.printRouterTable();
		//send ack to link
		send(msg.getLink(),new InterfaceChangeACK(msg.getNewInterface()),_now);
	}
	
	private void handleInterfaceChangeUpdate(InterfaceChangeUpdate msg){
		SimEnt sendNext = getInterface(msg.destination().networkId());
		send(sendNext,msg,_now);
	}
	
	private void handleBindingUpdate(BindingUpdate msg){
		System.out.println("Router " + networkID + " got binding update");
		ha.updateBinding(msg);
	}
	
	private void handleBindingACK(BindingACK msg){
		
	}
	
}
