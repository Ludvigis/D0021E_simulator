package Sim;
import java.util.HashMap;
import java.util.Map.Entry;

public class HomeAgent{
	HashMap<String,String> bindMap;
	public HomeAgent(){
		bindMap = new HashMap<String, String>();
	}
	
	//map home address to care of address
	public void updateBinding(BindingUpdate msg){
		System.out.println("Registed COA");
		bindMap.put(msg.getHOA().networkId()+"."+msg.getHOA().nodeId(), msg.getCOA().networkId()+"."+msg.getCOA().nodeId());
		
	}
	
	public boolean inMapTable(NetworkAddr HOA){
		String addr = HOA.networkId()+"."+HOA.nodeId();
		
	
		return bindMap.containsKey(addr);
	}
	
	public NetworkAddr getCOA(NetworkAddr HOA){
		String addr = bindMap.get(HOA.networkId()+"."+HOA.nodeId());
		
		String[] temp = addr.split("\\.");
		
		return new NetworkAddr( Integer.parseInt(temp[0]),Integer.parseInt(temp[1]));
	}
	
}
