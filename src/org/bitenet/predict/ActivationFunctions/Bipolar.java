package org.bitenet.predict.ActivationFunctions;

public class Bipolar implements NActivationFunction {

	@Override
	public double activate(double in) {
		return in>0?1:-1;
	}

	@Override
	public double derivative(double in) {
		
		return 0;
	}
	

}
