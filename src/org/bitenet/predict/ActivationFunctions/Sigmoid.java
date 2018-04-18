package org.bitenet.predict.ActivationFunctions;

public class Sigmoid implements NActivationFunction {
	@Override
	public double activate(double in) {
		return 1/(1+Math.exp(-in));
	}
	@Override
	public double derivative(double in) {
		double qq = activate(in);
		return qq*(1-qq);
		
	}
	

}
