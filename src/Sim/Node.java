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
	private int interfaceChangeNumMsg;
	private int newInterface;
	
	//Overloaded constructor with generator argument.
	public Node (int network, int node, Generator generator)
	{
		super();
		_id = new NetworkAddr(network, node);
		this.generator = generator;
		this.sink = new Sink();
	}

	
	public Node (int network, int node,int rate)
	{
		super();
		this.generator = new CBR(rate);
		_id = new NetworkAddr(network, node);
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
	
	public void changeRouterInterface(int newInterface) {
		send(_peer, new InterfaceChange(this,newInterface,(Link)_peer),0);
	}
	
	
	public void moveMobileNodeAfterTime(Router router,int time) {
		
		send(this,new MoveEvent(router),time);
		
	}
	
	
	// This method is called upon that an event destined for this node triggers.
	int numReceivedMsg;
	
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
		}
		
		if(ev instanceof MoveEvent) {
			
			MoveEvent msg = (MoveEvent)ev;
			System.out.println("Move at time " + SimEngine.getTime());
			send(_peer,new DisconnectEvent(this),0);
			//send(_peer,new BindingUpdate(this._id, this._id),0);
			
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
