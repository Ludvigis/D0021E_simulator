package Sim;

import java.util.Random;

public class Poisson implements Generator{
	private Random rnd;
	private double avg;
	
	public Poisson(double avg) {
		rnd = new Random();
		this.avg = avg;
	}
	
	public double getNextSend() {		//using algorithm described by Knuth to generate
										//random numbers with a poisson distribution
		double l = Math.exp(-avg);
		int k = 0;
		double p = 1.0;
					
		while(p>l) {
			k = k+1;
			double u = rnd.nextDouble();
			p = p*u;
		}
		return k-1;
	}
	
	
	
}
