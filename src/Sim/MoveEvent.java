package Sim;

public class MoveEvent implements Event {

	private Router router;
	
	public MoveEvent(Router router) {
		this.router = router; 
	}
	
	public void entering(SimEnt locale) {
		// TODO Auto-generated method stub

	}

	public Router getRouter() {
		return router;
	}

}
