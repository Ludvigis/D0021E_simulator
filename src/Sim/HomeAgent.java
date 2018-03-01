package Sim;
import java.util.HashMap;
import java.util.Map.Entry;

public class HomeAgent{
	HashMap<NetworkAddr,NetworkAddr> bindMap;
	public HomeAgent(){
		bindMap = new HashMap<NetworkAddr, NetworkAddr>();
	}
	
	//map home address to care of address
	public void updateBinding(BindingUpdate msg){
		System.out.println("Registed COA");
		bindMap.put(msg.getHOA(), msg.getCOA());
		
		for (Entry<NetworkAddr, NetworkAddr> entry : bindMap.entrySet()) {
			System.out.println("Item : " + entry.getKey().networkId()+" "+ entry.getValue().nodeId() + " Count : " + entry.getValue().networkId()+" "+ entry.getValue().nodeId());
		}
	}
	
	public boolean inMapTable(NetworkAddr HOA){
		
		return bindMap.containsKey(HOA);
	}
	
	public NetworkAddr getCOA(NetworkAddr HOA){
		return bindMap.get(HOA);
	}
}
