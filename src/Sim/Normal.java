package Sim;

import java.util.Random;

public class Normal implements Generator {
	
	private Random rnd;
	private double mean;
	private double dev;
	
	//Constructor with mean time and deviation.
	public Normal(double mean, double dev){
		this.rnd = new Random();
		this.mean = mean;
		this.dev = dev;
	}
	
	public double getNextSendDistribbution() {
		// Gives next normal distributed value between limits of 0.0 - 1.0
		double dist = rnd.nextGaussian();
		// 
		dist *= dev + mean; 
		return 1.0d;
	}

}
