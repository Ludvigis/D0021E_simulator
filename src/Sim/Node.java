package Sim;

// This class implements a node (host) it has an address, a peer that it communicates with
// and it count messages send and received.

public class Node extends SimEnt {
	private NetworkAddr _id;
	private SimEnt _peer;
	private int _sentmsg=0;
	private int _seq = 0;
	private Generator generator;
	private Sink sink;
	private Double avgDelay = Double.NaN;
	private Double maxDelay = Double.NaN;
	private Double minDelay = Double.NaN;
	private double totalDelay;
	private int numReceivedMsg;
	private int interfaceChangeNumMsg;
	private int newInterface;
	private SimEnt HA;
	private NetworkAddr HOA;

	// boolean to represent if the address currently used is valid
	// set to true when created and false when moved to new network.
	private boolean _validNetwork;
	
	//Overloaded constructor with generator argument.
	public Node (int network, int node, Generator generator)
	{
		super();
		_id = new NetworkAddr(network, node);
		this.generator = generator;
		this.sink = new Sink();
		this._validNetwork = true;
		this.HOA = new NetworkAddr(network,node);
		
	}
	
	public void setHA(Router r){
		this.HA = r;
	}
	


	//Constructor with CBR
	public Node (int network, int node,int rate)
	{
		super();
		this.generator = new CBR(rate);
		_id = new NetworkAddr(network, node);
		this._validNetwork = true;
		this.HOA = new NetworkAddr(network,node);
	}	

	
	// Sets the peer to communicate with. This node is single homed
	
	public void setPeer (SimEnt peer)
	{
		_peer = peer;
		
		if(_peer instanceof Link )
		{
			 ((Link) _peer).setConnector(this);
		}
	}
	
	
	public NetworkAddr getAddr()
	{
		return _id;
	}
	
//**********************************************************************************	
	// Just implemented to generate some traffic for demo.
	// In one of the labs you will create some traffic generators
	
	private int _stopSendingAfter = 0; //messages
	private int _toNetwork = 0;
	private int _toHost = 0;
	
	public void StartSending(int network, int node, int number, int startSeq)
	{
		_stopSendingAfter = number;
		_toNetwork = network;
		_toHost = node;
		_seq = startSeq;
		send(this, new TimerEvent(),0);	
	}
	
//**********************************************************************************	
	
	// message a change of interface on router.
	public void changeRouterInterface(int newInterface) {
		send(_peer, new InterfaceChange(this,newInterface,(Link)_peer),0);
	}
	
	//set time for node to move to new network.
	public void moveMobileNodeAfterTime(Router router,int time) {
		
		send(this,new MoveEvent(router),time);
		
	}
	
	// Register COA at home agent.
	public void registerCOAatHA(){
		
		send(this.HA,new BindingUpdate(HOA,_id),0);
	}
	
	
	// This method is called upon that an event destined for this node triggers.
	
	public void recv(SimEnt src, Event ev)
	{
		if (ev instanceof TimerEvent)
		{
			if (_stopSendingAfter > _sentmsg)
			{
				double nextSend = generator.getNextSend();
				_sentmsg++;
				send(_peer, new Message(_id, new NetworkAddr(_toNetwork, _toHost),_seq),0);
				send(this, new TimerEvent(),nextSend);
				System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" sent message with seq: "+_seq + " at time "+SimEngine.getTime());
				_seq++;

				totalDelay += nextSend;
				maxDelay = (nextSend > maxDelay || maxDelay.isNaN())? nextSend:maxDelay;
				minDelay = (nextSend < minDelay || minDelay.isNaN())? nextSend:minDelay;
				avgDelay = totalDelay / _sentmsg;
			}
		}
		if (ev instanceof Message)
		{
			numReceivedMsg++;
			if(numReceivedMsg==interfaceChangeNumMsg) {
				changeRouterInterface(this.newInterface);
			}
			
			System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" receives message with seq: "+((Message) ev).seq() + " at time "+SimEngine.getTime());
			sink.recvMessage((Message)ev);
		}
		//Received interface change...
		if (ev instanceof InterfaceChangeUpdate) {
			InterfaceChangeUpdate msg = (InterfaceChangeUpdate)ev;
		
			System.out.println("Mobile Node is now on new interface: " + msg.getNewInterface());
		}
		
		if (ev instanceof InterfaceChangeACK ) {
			System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" received interface change ack");
			send(_peer, new InterfaceChangeUpdate(new NetworkAddr(_toNetwork,_toHost),((InterfaceChangeACK) ev).getNewInterface()),0);
		}
		if(ev instanceof RouterAdvertisement) {
			System.out.println("Node "+_id.networkId()+ "." + _id.nodeId() +" received router advertisement");
			if(!_validNetwork){
				
				//change network id
				NetworkAddr addr = new NetworkAddr(((RouterAdvertisement)ev).getPrefix(), _id.nodeId());
				_id = addr;
				_validNetwork = true;
				System.out.println("Node moved to network id :" + addr.networkId());
				Router r = ((RouterAdvertisement)ev).getRouter();
				//connect to router router selects appropriate interface number
				r.connectRandomInterface(_peer, this);
				//set SimEnt to be connector
				((Link)_peer).setConnector(r);
				//register at HA
				registerCOAatHA();
				
				
			}
		}
		
		//first step in moving to new network
		if(ev instanceof MoveEvent) {
			
			MoveEvent msg = (MoveEvent)ev;
			System.out.println("Move at time " + SimEngine.getTime());
			//send(_peer,new DisconnectEvent(this),0);
			((Router)HA).handleDisconnectEvent(new DisconnectEvent(this));
			this._validNetwork = false;
			Router r = msg.getRouter();
			r.connectRandomInterface(_peer,this);
			((Link)_peer).setConnector(r);
		}
		
		
	}
	
	public void changeInterfaceAfterRecvMsgs(int numMessages, int newInterface) {
		this.interfaceChangeNumMsg = numMessages;
		this.newInterface = newInterface;
	}
	
	public void printStatistics() {
		System.out.println("Node " + _id.networkId() + "." + _id.nodeId() + " using " + generator.getGeneratorName() + "\n");
		sink.printStatistics();
		printSendStatistics();
		System.out.println();
	}
	
	public void printSendStatistics() {
		System.out.println("Average time between sending: " + avgDelay);
		System.out.println("Max time between sending: " + maxDelay);
		System.out.println("Min time between sending: " + minDelay);

	}
}
