package org.bitenet.predict.activationfunctions;

public class Absolute implements NActivationFunction {

	@Override
	public double activate(double in) {
		return Math.abs(in);
	}

	@Override
	public double derivative(double in) {
		
		return in<0?-1:1;
	}
	

}
