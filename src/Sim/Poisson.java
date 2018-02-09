package Sim;

import java.util.Random;

public class Poisson implements Generator{
	private Random rnd;
	private double mean;
	
	public Poisson(double mean) {
		rnd = new Random();
		this.mean = mean;
	}
	
	public double getNextSend() {		//using algorithm described by Knuth to generate
										//random numbers with a poisson distribution
		double l = Math.exp(-mean);
		double k = 0;
		double p = 1;
					
		while(p>l) {
			k = k+1;
			double u = rnd.nextDouble();
			p = p*u;
		}
		return k-1;
	}
	
}
