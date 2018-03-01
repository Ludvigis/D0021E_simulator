package Sim;

public class RouterAdvertisement implements Event {
	private int networkPrefix;
	private Router srcRouter;
	
	public RouterAdvertisement(Router r){
		networkPrefix = r.getNetworkID();
		srcRouter = r;
	}
	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub

	}
	
	public Router getRouter(){
		return srcRouter;
	}
	
	
	public int getPrefix(){
		return networkPrefix;
	}

}
