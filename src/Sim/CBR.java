package Sim;

public class CBR implements Generator{

	double rate;
	
	public CBR(double rate){
		this.rate = rate;
	}
	public double getNextSend() {
		
		return rate;
	}

}
