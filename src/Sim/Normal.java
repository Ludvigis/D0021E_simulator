package Sim;

import java.util.Random;

public class Normal implements Generator {
	
	private Random rnd;
	private double mean;
	private double std_deviate;
	
	//Constructor with mean time and deviation.
	public Normal(double mean, double std_deviate){
		this.rnd = new Random();
		this.mean = mean;
		this.std_deviate = std_deviate;
	}
	
	public double getNextSend() {
		// Gives next normal distributed value with mean 0.0 and standard deviate 1.0.
		double dist = rnd.nextGaussian();
		// Returns normal distributed value around mean with the deviate specified.  
		dist *= std_deviate + mean;
		// No negative numbers allowed using loop to NOT build stack frames.
		while(dist < 0){
			dist = getNextSend();
		}
		return dist;
	}

}
