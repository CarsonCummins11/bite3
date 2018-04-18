package org.bitenet.predict.ActivationFunctions;

public class Cosine implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.cos(in);
	}

	@Override
	public double derivative(double in) {
		return -Math.sin(in);
	}
	

}
