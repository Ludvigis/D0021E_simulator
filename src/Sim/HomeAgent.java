package Sim;
import java.util.HashMap;

public class HomeAgent{
	HashMap<NetworkAddr,NetworkAddr> bindMap;
	public HomeAgent(){
		bindMap = new HashMap<NetworkAddr, NetworkAddr>();
	}
	
	//map home address to care of address
	public void updateBinding(BindingUpdate msg){
		
		bindMap.put(msg.getHOA(), msg.getCOA());
	}
	
	public boolean inMapTable(NetworkAddr HOA){
		
		return bindMap.containsKey(HOA);
	}
	
	public NetworkAddr getCOA(NetworkAddr HOA){
		return bindMap.get(HOA);
	}
	
	public static void main (String [] args){
		NetworkAddr a = new NetworkAddr(2, 1);
		NetworkAddr b = new NetworkAddr(10, 10);
		NetworkAddr c = new NetworkAddr(3, 3);
		HomeAgent h = new HomeAgent();
		h.bindMap.put(a, b);
		h.bindMap.put(a, c);
		System.out.println(h.bindMap.get(a));
	}
}
