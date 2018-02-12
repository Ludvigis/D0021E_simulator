package Sim;

public class CBR implements Generator{

	double bitrate;
	public CBR(double bitrate) {
		this.bitrate = bitrate;
	}
	
	
	public double getNextSend() {
		return this.bitrate;
	}
	


}
