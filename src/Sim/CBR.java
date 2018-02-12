package Sim;

public class CBR implements Generator{
	double bitrate;
	public CBR(double bitrate) {
		this.bitrate = bitrate;
	}
	
	
	public double getNextSend() {
		return this.bitrate;
	}
	
	public static void main (String [] args){
			
			CBR n = new CBR(100);
			for(int i = 0; i<100000;i++) {
				System.out.println(n.getNextSend());
			}
			
	}

}
