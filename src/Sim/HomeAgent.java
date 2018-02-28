package Sim;
import java.util.HashMap;

public class HomeAgent{
	HashMap<NetworkAddr,NetworkAddr> bindMap;
	public HomeAgent(){
		bindMap = new HashMap<NetworkAddr, NetworkAddr>();
	}
	
	//map home address to care of address
	public void updateBinding(BindingUpdate msg){
		System.out.println("Registed COA");
		bindMap.put(msg.getHOA(), msg.getCOA());
	}
	
	public boolean inMapTable(NetworkAddr HOA){
		
		return bindMap.containsKey(HOA);
	}
	
	public NetworkAddr getCOA(NetworkAddr HOA){
		return bindMap.get(HOA);
	}

}
